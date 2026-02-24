package io.github.ebinjoy999.gradle_build_health

import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState
import java.util.concurrent.ConcurrentHashMap

/**
 * Represents collected metrics for a single task.
 */
data class TaskMetrics(
    val taskPath: String,
    val durationMs: Long,
    val cacheStatus: CacheStatus,
    val wasIncremental: Boolean,
    val didWork: Boolean
)

/**
 * Cache status for a task execution.
 */
enum class CacheStatus {
    HIT,
    MISS,
    DISABLED,
    NOT_CACHEABLE,
    SKIPPED,
    UP_TO_DATE,
    NO_SOURCE,
    FROM_CACHE
}

/**
 * Collects metrics from task execution (duration, cache status, incremental status).
 */
class TaskMetricsCollector : TaskExecutionListener {

    private val taskStartTimes = ConcurrentHashMap<String, Long>()
    private val collectedMetrics = mutableListOf<TaskMetrics>()

    @Synchronized
    fun getMetrics(): List<TaskMetrics> = collectedMetrics.toList()

    @Synchronized
    fun clear() {
        taskStartTimes.clear()
        collectedMetrics.clear()
    }

    override fun beforeExecute(task: Task) {
        taskStartTimes[task.path] = System.currentTimeMillis()
    }

    override fun afterExecute(task: Task, state: TaskState) {
        val startTime = taskStartTimes.remove(task.path) ?: return
        val duration = System.currentTimeMillis() - startTime

        val cacheStatus = determineCacheStatus(state)
        val wasIncremental = determineIfIncremental(task, state)

        val metrics = TaskMetrics(
            taskPath = task.path,
            durationMs = duration,
            cacheStatus = cacheStatus,
            wasIncremental = wasIncremental,
            didWork = state.didWork
        )

        synchronized(this) {
            collectedMetrics.add(metrics)
        }
    }

    private fun determineCacheStatus(state: TaskState): CacheStatus {
        return when {
            state.skipped -> CacheStatus.SKIPPED
            state.noSource -> CacheStatus.NO_SOURCE
            state.upToDate -> CacheStatus.UP_TO_DATE
            !state.didWork -> CacheStatus.FROM_CACHE
            state.didWork -> CacheStatus.MISS
            else -> CacheStatus.NOT_CACHEABLE
        }
    }

    private fun determineIfIncremental(task: Task, state: TaskState): Boolean {
        // A task is considered non-incremental if it did work and wasn't from cache
        // More sophisticated detection would require inspecting task inputs
        if (state.skipped || state.upToDate || state.noSource || !state.didWork) {
            return true // These are effectively "incremental" as they didn't do full work
        }
        
        // Check if this is a compile task that ran fully (non-incremental)
        // This is a heuristic - compile tasks that do work are often non-incremental
        val isCompileTask = task.name.contains("compile", ignoreCase = true) ||
                task.name.contains("kapt", ignoreCase = true)
        
        return !isCompileTask || !state.didWork
    }
}
