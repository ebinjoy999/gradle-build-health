package com.shoplite.core.network

import com.shoplite.core.model.Category
import com.shoplite.core.model.Product
import kotlinx.coroutines.delay

/**
 * Simulated network API client.
 * 
 * In a real app, this would use Retrofit or Ktor.
 * Here we use artificial delays to simulate network latency.
 */
class ProductApiClient {

    /**
     * Fetches all products from the "server".
     * Simulates network delay.
     */
    suspend fun getProducts(): List<Product> {
        // Simulate network latency
        delay(100)
        return DummyData.products
    }

    /**
     * Fetches products by category.
     */
    suspend fun getProductsByCategory(categoryId: Long): List<Product> {
        delay(80)
        return DummyData.products.filter { it.category.id == categoryId }
    }

    /**
     * Searches products by name.
     */
    suspend fun searchProducts(query: String): List<Product> {
        delay(50)
        return DummyData.products.filter { 
            it.name.contains(query, ignoreCase = true) 
        }
    }

    /**
     * Fetches all categories.
     */
    suspend fun getCategories(): List<Category> {
        delay(30)
        return DummyData.categories
    }
}
