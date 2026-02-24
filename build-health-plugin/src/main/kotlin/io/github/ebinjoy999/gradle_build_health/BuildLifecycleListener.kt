package io.github.ebinjoy999.gradle_build_health

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

/**
 * Holds timing data for the build lifecycle.
 */
data class BuildTimingData(
    val configurationStartMs: Long,
    val configurationEndMs: Long,
    val executionStartMs: Long,
    val executionEndMs: Long
) {
    val configurationDurationMs: Long
        get() = configurationEndMs - configurationStartMs

    val executionDurationMs: Long
        get() = executionEndMs - executionStartMs

    val totalDurationMs: Long
        get() = executionEndMs - configurationStartMs
}

/**
 * Listens to build lifecycle events to trigger summary rendering.
 */
class BuildLifecycleListener(
    private val onBuildFinished: (BuildTimingData, BuildResult) -> Unit
) : BuildListener {

    @Volatile
    private var configurationStartMs: Long = 0L

    @Volatile
    private var configurationEndMs: Long = 0L

    @Volatile
    private var executionStartMs: Long = 0L

    fun recordConfigurationStart() {
        configurationStartMs = System.currentTimeMillis()
    }

    fun recordConfigurationEnd() {
        configurationEndMs = System.currentTimeMillis()
    }

    fun recordExecutionStart() {
        executionStartMs = System.currentTimeMillis()
    }

    override fun settingsEvaluated(settings: Settings) {
        // Configuration is in progress
    }

    override fun projectsLoaded(gradle: Gradle) {
        // Projects have been loaded, still configuring
    }

    override fun projectsEvaluated(gradle: Gradle) {
        // All projects evaluated - configuration phase ends
        recordConfigurationEnd()
        recordExecutionStart()
    }

    override fun buildFinished(result: BuildResult) {
        val executionEndMs = System.currentTimeMillis()

        // Handle edge case where configuration timing wasn't captured
        val effectiveConfigStart = if (configurationStartMs == 0L) executionEndMs else configurationStartMs
        val effectiveConfigEnd = if (configurationEndMs == 0L) effectiveConfigStart else configurationEndMs
        val effectiveExecStart = if (executionStartMs == 0L) effectiveConfigEnd else executionStartMs

        val timingData = BuildTimingData(
            configurationStartMs = effectiveConfigStart,
            configurationEndMs = effectiveConfigEnd,
            executionStartMs = effectiveExecStart,
            executionEndMs = executionEndMs
        )

        onBuildFinished(timingData, result)
    }
}
