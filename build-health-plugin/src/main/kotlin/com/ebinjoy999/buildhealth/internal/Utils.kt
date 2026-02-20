package com.ebinjoy999.buildhealth.internal

/**
 * Internal utility functions for the Build Health plugin.
 */
internal object Utils {

    /**
     * Formats milliseconds into a human-readable duration string.
     * Examples: "2m 43s", "41s", "1h 5m 30s"
     */
    fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> "${hours}h ${minutes % 60}m ${seconds % 60}s"
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }

    /**
     * Formats milliseconds into a short duration string for task display.
     * Examples: "28s", "1m 30s"
     */
    fun formatTaskDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60

        return when {
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            seconds > 0 -> "${seconds}s"
            else -> "${millis}ms"
        }
    }

    /**
     * Returns the current timestamp in ISO 8601 format for JSON export.
     */
    fun currentTimestamp(): String {
        val now = java.time.Instant.now()
        return now.toString()
    }
}
