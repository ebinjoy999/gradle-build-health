package com.shoplite.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplite.core.data.ProductRepository
import com.shoplite.core.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Sealed class representing the UI state for the Home screen.
 */
sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val products: List<Product>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

/**
 * ViewModel for the Home screen.
 * 
 * This creates a separate Kotlin compilation unit,
 * contributing to parallel compile tasks in the build.
 */
class HomeViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            repository.getProducts()
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
                }
                .collect { products ->
                    _uiState.value = HomeUiState.Success(products)
                }
        }
    }

    fun refresh() {
        loadProducts()
    }
}
