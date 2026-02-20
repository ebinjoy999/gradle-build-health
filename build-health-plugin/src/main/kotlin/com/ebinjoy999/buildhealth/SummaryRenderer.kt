package com.ebinjoy999.buildhealth

import com.ebinjoy999.buildhealth.internal.Utils
import org.gradle.api.logging.Logger

/**
 * Renders the build health summary to console output.
 */
class SummaryRenderer(
    private val logger: Logger
) {

    companion object {
        private const val SEPARATOR = "────────────────────────────────────────"
    }

    /**
     * Renders the complete build health summary.
     */
    fun render(buildData: BuildData, slowestTaskCount: Int, showJsonTip: Boolean) {
        val sb = StringBuilder()
        
        sb.appendLine()
        sb.appendLine(SEPARATOR)
        sb.appendLine("⚙️ Build Health Summary")
        sb.appendLine(SEPARATOR)
        
        // Timing section
        renderTiming(sb, buildData)
        
        // Slowest tasks section
        renderSlowestTasks(sb, buildData, slowestTaskCount)
        
        // Cache section
        renderCacheStats(sb, buildData)
        
        // JSON tip (only if not already enabled)
        if (showJsonTip) {
            renderJsonTip(sb)
        }
        
        sb.appendLine(SEPARATOR)
        
        logger.lifecycle(sb.toString())
    }

    private fun renderTiming(sb: StringBuilder, buildData: BuildData) {
        val totalTime = Utils.formatDuration(buildData.timingData.totalDurationMs)
        val configTime = Utils.formatDuration(buildData.timingData.configurationDurationMs)
        
        sb.appendLine("⏱ Total: $totalTime")
        
        // Only show configuration warning if it's significant (> 10% of total or > 10s)
        val configRatio = if (buildData.timingData.totalDurationMs > 0) {
            buildData.timingData.configurationDurationMs.toDouble() / buildData.timingData.totalDurationMs
        } else 0.0
        
        if (configRatio > 0.1 || buildData.timingData.configurationDurationMs > 10000) {
            sb.appendLine("⚠ Configuration: $configTime")
        }
        
        sb.appendLine()
    }

    private fun renderSlowestTasks(sb: StringBuilder, buildData: BuildData, count: Int) {
        val slowestTasks = buildData.getSlowestTasks(count)
        
        if (slowestTasks.isEmpty()) {
            return
        }
        
        sb.appendLine("🐢 Slowest Tasks:")
        
        for (task in slowestTasks) {
            val duration = Utils.formatTaskDuration(task.durationMs)
            val annotation = getTaskAnnotation(task)
            sb.appendLine("  * ${task.taskPath} → $duration$annotation")
        }
        
        sb.appendLine()
    }

    private fun getTaskAnnotation(task: TaskMetrics): String {
        val annotations = mutableListOf<String>()
        
        when (task.cacheStatus) {
            CacheStatus.MISS -> annotations.add("cache miss")
            CacheStatus.FROM_CACHE -> annotations.add("from cache")
            CacheStatus.UP_TO_DATE -> annotations.add("up-to-date")
            else -> {}
        }
        
        if (!task.wasIncremental && task.didWork) {
            annotations.add("non-incremental")
        }
        
        return if (annotations.isNotEmpty()) {
            " (${annotations.joinToString(", ")})"
        } else {
            ""
        }
    }

    private fun renderCacheStats(sb: StringBuilder, buildData: BuildData) {
        if (buildData.totalTasks == 0) {
            return
        }
        
        sb.appendLine("📦 Cache:")
        sb.appendLine("  * Hits: ${buildData.cacheHitPercentage}%")
        
        if (buildData.cacheMisses > 0) {
            sb.appendLine("  * Misses: ${buildData.cacheMisses} tasks")
        }
        
        val nonIncremental = buildData.nonIncrementalTasks
        if (nonIncremental.isNotEmpty()) {
            sb.appendLine("  * Non-incremental: ${nonIncremental.size} tasks")
        }
        
        sb.appendLine()
    }

    private fun renderJsonTip(sb: StringBuilder) {
        sb.appendLine("ℹ Tip: Enable JSON export with:")
        sb.appendLine("  buildHealth { exportJson = true }")
        sb.appendLine("  or")
        sb.appendLine("  ./gradlew build -PbuildHealth.exportJson=true")
        sb.appendLine()
    }
}
