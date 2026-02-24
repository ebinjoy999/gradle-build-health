package io.github.ebinjoy999.gradle_build_health

import io.github.ebinjoy999.gradle_build_health.internal.Utils
import org.gradle.api.Project
import java.io.File

/**
 * Exports build metrics to JSON format when enabled.
 */
class JsonExporter {

    /**
     * Exports build data to JSON file.
     * Output location: <root>/build/build-health/<project-name>/build-health.json
     */
    fun export(project: Project, buildData: BuildData) {
        val outputDir = File(project.rootProject.buildDir, "build-health/${buildData.projectName}")
        outputDir.mkdirs()

        val outputFile = File(outputDir, "build-health.json")
        val json = buildJson(buildData)
        outputFile.writeText(json)

        project.logger.lifecycle("Build Health JSON exported to: ${outputFile.absolutePath}")
    }

    private fun buildJson(buildData: BuildData): String {
        val sb = StringBuilder()
        sb.appendLine("{")
        
        // Metadata
        sb.appendLine("""  "timestamp": "${Utils.currentTimestamp()}",""")
        sb.appendLine("""  "projectName": "${escape(buildData.projectName)}",""")
        sb.appendLine("""  "buildSuccess": ${buildData.buildSuccess},""")
        
        // Timing
        sb.appendLine("""  "timing": {""")
        sb.appendLine("""    "totalMs": ${buildData.timingData.totalDurationMs},""")
        sb.appendLine("""    "configurationMs": ${buildData.timingData.configurationDurationMs},""")
        sb.appendLine("""    "executionMs": ${buildData.timingData.executionDurationMs}""")
        sb.appendLine("""  },""")
        
        // Cache stats
        sb.appendLine("""  "cache": {""")
        sb.appendLine("""    "hitPercentage": ${buildData.cacheHitPercentage},""")
        sb.appendLine("""    "hits": ${buildData.cacheHits},""")
        sb.appendLine("""    "misses": ${buildData.cacheMisses},""")
        sb.appendLine("""    "totalTasks": ${buildData.totalTasks}""")
        sb.appendLine("""  },""")
        
        // Non-incremental tasks
        sb.appendLine("""  "nonIncrementalTasks": [""")
        val nonIncremental = buildData.nonIncrementalTasks
        nonIncremental.forEachIndexed { index, task ->
            val comma = if (index < nonIncremental.size - 1) "," else ""
            sb.appendLine("""    "${escape(task.taskPath)}"$comma""")
        }
        sb.appendLine("""  ],""")
        
        // All tasks
        sb.appendLine("""  "tasks": [""")
        buildData.taskMetrics.forEachIndexed { index, task ->
            val comma = if (index < buildData.taskMetrics.size - 1) "," else ""
            sb.appendLine("""    {""")
            sb.appendLine("""      "path": "${escape(task.taskPath)}",""")
            sb.appendLine("""      "durationMs": ${task.durationMs},""")
            sb.appendLine("""      "cacheStatus": "${task.cacheStatus.name}",""")
            sb.appendLine("""      "incremental": ${task.wasIncremental},""")
            sb.appendLine("""      "didWork": ${task.didWork}""")
            sb.appendLine("""    }$comma""")
        }
        sb.appendLine("""  ]""")
        
        sb.appendLine("}")
        return sb.toString()
    }

    private fun escape(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
