package com.example.groceries_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.model.Category
import com.example.groceries_app.data.model.Product
import com.example.groceries_app.data.repository.NectarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NectarProductState {
    object Loading : NectarProductState()
    data class Success(val products: List<Product>) : NectarProductState()
    data class Error(val message: String) : NectarProductState()
}

sealed class CategoryState {
    object Loading : CategoryState()
    data class Success(val categories: List<Category>) : CategoryState()
    data class Error(val message: String) : CategoryState()
}

class NectarProductViewModel(
    private val repository: NectarRepository = NectarRepository.getInstance()
) : ViewModel() {

    private val _productsState = MutableStateFlow<NectarProductState>(NectarProductState.Loading)
    val productsState: StateFlow<NectarProductState> = _productsState.asStateFlow()

    private val _categoriesState = MutableStateFlow<CategoryState>(CategoryState.Loading)
    val categoriesState: StateFlow<CategoryState> = _categoriesState.asStateFlow()

    private val _productDetailState = MutableStateFlow<Product?>(null)
    val productDetailState: StateFlow<Product?> = _productDetailState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Load products and categories when ViewModel is created
        loadProducts()
        loadCategories()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _productsState.value = NectarProductState.Loading
            _isLoading.value = true
            
            repository.getAllProducts()
                .onSuccess { products ->
                    _productsState.value = NectarProductState.Success(products)
                    _error.value = null
                }
                .onFailure { exception ->
                    val errorMsg = exception.message ?: "Failed to load products"
                    _productsState.value = NectarProductState.Error(errorMsg)
                    _error.value = errorMsg
                }
            
            _isLoading.value = false
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            _categoriesState.value = CategoryState.Loading
            
            repository.getAllCategories()
                .onSuccess { categories ->
                    _categoriesState.value = CategoryState.Success(categories)
                }
                .onFailure { exception ->
                    val errorMsg = exception.message ?: "Failed to load categories"
                    _categoriesState.value = CategoryState.Error(errorMsg)
                }
        }
    }

    fun getProductByUuid(uuid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getProductByUuid(uuid)
                .onSuccess { product ->
                    _productDetailState.value = product
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load product details"
                }
            
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearProductDetail() {
        _productDetailState.value = null
    }
}
