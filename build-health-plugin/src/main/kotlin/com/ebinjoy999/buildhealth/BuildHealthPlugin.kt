package com.ebinjoy999.buildhealth

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.BuildResult

/**
 * Main entry point for the Build Health plugin.
 * Automatically collects build metrics and prints a summary at build end.
 */
class BuildHealthPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        // Only apply to root project to avoid duplicate summaries
        if (project != project.rootProject) {
            return
        }

        // Create extension for user configuration
        val extension = project.extensions.create(
            BuildHealthExtension.EXTENSION_NAME,
            BuildHealthExtension::class.java
        )

        // Set defaults
        extension.exportJson.convention(false)
        extension.slowestTaskCount.convention(5)

        // Create metrics collector
        val metricsCollector = TaskMetricsCollector()

        // Create lifecycle listener with callback for build finished
        val lifecycleListener = BuildLifecycleListener { timingData, buildResult ->
            handleBuildFinished(project, extension, metricsCollector, timingData, buildResult)
        }

        // Record configuration start time
        lifecycleListener.recordConfigurationStart()

        // Register listeners
        project.gradle.addListener(metricsCollector)
        project.gradle.addListener(lifecycleListener)
    }

    private fun handleBuildFinished(
        project: Project,
        extension: BuildHealthExtension,
        metricsCollector: TaskMetricsCollector,
        timingData: BuildTimingData,
        buildResult: BuildResult
    ) {
        // Skip summary for non-build tasks (help, tasks, dependencies, etc.)
        if (shouldSkipSummary(project)) {
            return
        }

        val taskMetrics = metricsCollector.getMetrics()

        // Collect all data for rendering/export
        val buildData = BuildData(
            projectName = project.name,
            timingData = timingData,
            taskMetrics = taskMetrics,
            buildSuccess = buildResult.failure == null
        )

        // Resolve configuration
        val exportJson = BuildHealthExtension.resolveExportJson(extension, project)
        val slowestTaskCount = extension.slowestTaskCount.getOrElse(5)

        // Render summary
        val renderer = SummaryRenderer(project.logger)
        renderer.render(buildData, slowestTaskCount, showJsonTip = !exportJson)

        // Export JSON if enabled
        if (exportJson) {
            val jsonExporter = JsonExporter()
            jsonExporter.export(project, buildData)
        }
    }

    private fun shouldSkipSummary(project: Project): Boolean {
        val taskNames = project.gradle.startParameter.taskNames
        if (taskNames.isEmpty()) {
            return true
        }

        val nonBuildTasks = setOf(
            "help", "tasks", "dependencies", "projects",
            "properties", "buildEnvironment", "components",
            "dependentComponents", "model", "dependencyInsight"
        )

        // Skip if all requested tasks are non-build tasks
        return taskNames.all { taskName ->
            nonBuildTasks.any { nonBuild ->
                taskName.endsWith(nonBuild, ignoreCase = true)
            }
        }
    }
}

/**
 * Aggregated build data for rendering and export.
 */
data class BuildData(
    val projectName: String,
    val timingData: BuildTimingData,
    val taskMetrics: List<TaskMetrics>,
    val buildSuccess: Boolean
) {
    val totalTasks: Int
        get() = taskMetrics.size

    val cacheHits: Int
        get() = taskMetrics.count {
            it.cacheStatus == CacheStatus.FROM_CACHE ||
            it.cacheStatus == CacheStatus.UP_TO_DATE
        }

    val cacheMisses: Int
        get() = taskMetrics.count { it.cacheStatus == CacheStatus.MISS }

    val cacheHitPercentage: Int
        get() {
            val cacheable = cacheHits + cacheMisses
            return if (cacheable > 0) (cacheHits * 100) / cacheable else 0
        }

    val nonIncrementalTasks: List<TaskMetrics>
        get() = taskMetrics.filter { !it.wasIncremental && it.didWork }

    fun getSlowestTasks(count: Int): List<TaskMetrics> {
        return taskMetrics
            .filter { it.didWork }
            .sortedByDescending { it.durationMs }
            .take(count)
    }
}
