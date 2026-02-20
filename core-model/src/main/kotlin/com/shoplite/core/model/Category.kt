package com.shoplite.core.model

/**
 * Represents a product category.
 * 
 * Changes to this class will trigger recompilation across:
 * - core-network
 * - core-data  
 * - feature-home
 * - feature-search
 * - app
 */
data class Category(
    val id: Long,
    val name: String,
    val description: String = ""
)
