package com.shoplite.core.data

import com.shoplite.core.model.Category
import com.shoplite.core.model.Product
import com.shoplite.core.network.ProductApiClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repository for product data.
 * 
 * This is the central data access point for feature modules.
 * It combines data from core-network and exposes it as Flows.
 * 
 * Both feature-home and feature-search depend on this module,
 * making it a critical node in the dependency graph.
 */
class ProductRepository {

    private val apiClient = ProductApiClient()

    /**
     * Returns a Flow of all products.
     */
    fun getProducts(): Flow<List<Product>> = flow {
        val products = apiClient.getProducts()
        emit(products)
    }

    /**
     * Returns a Flow of products filtered by category.
     */
    fun getProductsByCategory(categoryId: Long): Flow<List<Product>> = flow {
        val products = apiClient.getProductsByCategory(categoryId)
        emit(products)
    }

    /**
     * Returns a Flow of products matching the search query.
     */
    fun searchProducts(query: String): Flow<List<Product>> = flow {
        if (query.isBlank()) {
            emit(emptyList())
        } else {
            val products = apiClient.searchProducts(query)
            emit(products)
        }
    }

    /**
     * Returns a Flow of all categories.
     */
    fun getCategories(): Flow<List<Category>> = flow {
        val categories = apiClient.getCategories()
        emit(categories)
    }
}
