package com.shoplite.core.model

/**
 * Represents a product in the ShopLite catalog.
 * 
 * This is a CORE model class. Any change here will cascade rebuilds
 * through the entire dependency graph:
 * 
 * core-model -> core-network -> core-data -> feature-home
 *                                         -> feature-search
 *                                         -> app
 * 
 * Try modifying this file and running ./gradlew build to see
 * the BuildHealth plugin report the rebuild cascade.
 */
data class Product(
    val id: Long,
    val name: String,
    val price: Double,
    val category: Category,
    val description: String = "",
    val imageUrl: String = "",
    val inStock: Boolean = true
)
