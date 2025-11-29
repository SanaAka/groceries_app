package com.example.groceries_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.model.ProductListResponse
import com.example.groceries_app.data.model.Product
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _productsState = MutableStateFlow<Resource<ProductListResponse>>(Resource.Loading())
    val productsState: StateFlow<Resource<ProductListResponse>> = _productsState.asStateFlow()

    private val _productDetailState = MutableStateFlow<Resource<Product>?>(null)
    val productDetailState: StateFlow<Resource<Product>?> = _productDetailState.asStateFlow()

    private val _featuredProductsState = MutableStateFlow<Resource<ProductListResponse>>(Resource.Loading())
    val featuredProductsState: StateFlow<Resource<ProductListResponse>> = _featuredProductsState.asStateFlow()

    private val _bestSellingProductsState = MutableStateFlow<Resource<ProductListResponse>>(Resource.Loading())
    val bestSellingProductsState: StateFlow<Resource<ProductListResponse>> = _bestSellingProductsState.asStateFlow()

    fun getProducts(
        page: Int = 1,
        limit: Int = 20,
        category: String? = null,
        search: String? = null,
        sort: String? = null
    ) {
        viewModelScope.launch {
            repository.getProducts(page, limit, category, search, sort).collect { result ->
                _productsState.value = result
            }
        }
    }

    fun getProductById(productId: Int) {
        viewModelScope.launch {
            repository.getProductById(productId).collect { result ->
                _productDetailState.value = result
            }
        }
    }

    fun getFeaturedProducts(limit: Int = 10) {
        viewModelScope.launch {
            repository.getFeaturedProducts(limit).collect { result ->
                _featuredProductsState.value = result
            }
        }
    }

    fun getBestSellingProducts(limit: Int = 10) {
        viewModelScope.launch {
            repository.getBestSellingProducts(limit).collect { result ->
                _bestSellingProductsState.value = result
            }
        }
    }

    fun searchProducts(query: String, page: Int = 1) {
        viewModelScope.launch {
            repository.searchProducts(query, page).collect { result ->
                _productsState.value = result
            }
        }
    }
}

