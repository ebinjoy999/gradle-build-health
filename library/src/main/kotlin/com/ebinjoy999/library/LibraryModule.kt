package com.ebinjoy999.library

/**
 * Sample library class for multi-module demo.
 * Contains utility functions to demonstrate plugin metrics.
 */
class LibraryModule {

    /**
     * Returns a greeting message.
     */
    fun getGreeting(): String {
        return "Hello from Library Module!"
    }

    /**
     * Performs a sample calculation.
     */
    fun calculateSum(numbers: List<Int>): Int {
        return numbers.sum()
    }

    /**
     * Returns library version info.
     */
    fun getVersionInfo(): String {
        return "Library v1.0.0"
    }
}

/**
 * Data class to demonstrate more compilation work.
 */
data class LibraryData(
    val id: Long,
    val name: String,
    val description: String,
    val tags: List<String>
)

/**
 * Extension functions for demo purposes.
 */
fun String.toLibraryData(id: Long): LibraryData {
    return LibraryData(
        id = id,
        name = this,
        description = "Auto-generated from string",
        tags = emptyList()
    )
}

/**
 * Utility object with helper functions.
 */
object LibraryUtils {
    
    fun formatMessage(message: String): String {
        return "[Library] $message"
    }
    
    fun validateInput(input: String): Boolean {
        return input.isNotBlank() && input.length <= 100
    }
}
