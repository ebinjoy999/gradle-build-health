package com.shoplite.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplite.core.data.ProductRepository
import com.shoplite.core.model.Product
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * Sealed class representing the UI state for the Search screen.
 */
sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val products: List<Product>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
    data object Empty : SearchUiState()
}

/**
 * ViewModel for the Search screen.
 * 
 * This is a SEPARATE ViewModel from HomeViewModel, creating
 * parallel Kotlin compilation tasks during the build.
 * 
 * This demonstrates how feature modules each have their own
 * compile tasks that appear in the BuildHealth summary.
 */
class SearchViewModel : ViewModel() {

    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var searchJob: Job? = null

    /**
     * Updates the search query and triggers a debounced search.
     */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        
        // Cancel previous search
        searchJob?.cancel()
        
        if (query.isBlank()) {
            _uiState.value = SearchUiState.Idle
            return
        }

        // Debounce search
        searchJob = viewModelScope.launch {
            delay(300) // 300ms debounce
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String) {
        _uiState.value = SearchUiState.Loading
        
        repository.searchProducts(query)
            .catch { e ->
                _uiState.value = SearchUiState.Error(e.message ?: "Search failed")
            }
            .collect { products ->
                _uiState.value = if (products.isEmpty()) {
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(products)
                }
            }
    }

    /**
     * Clears the current search.
     */
    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.value = SearchUiState.Idle
        searchJob?.cancel()
    }
}
