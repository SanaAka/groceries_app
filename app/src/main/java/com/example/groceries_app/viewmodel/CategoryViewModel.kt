package com.example.groceries_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.model.CategoryListResponse
import com.example.groceries_app.data.model.ProductListResponse
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<Resource<CategoryListResponse>>(Resource.Loading())
    val categoriesState: StateFlow<Resource<CategoryListResponse>> = _categoriesState.asStateFlow()

    private val _categoryProductsState = MutableStateFlow<Resource<ProductListResponse>>(Resource.Loading())
    val categoryProductsState: StateFlow<Resource<ProductListResponse>> = _categoryProductsState.asStateFlow()

    fun getCategories() {
        viewModelScope.launch {
            repository.getCategories().collect { result ->
                _categoriesState.value = result
            }
        }
    }

    fun getProductsByCategory(categoryId: Int, page: Int = 1, limit: Int = 20) {
        viewModelScope.launch {
            repository.getProductsByCategory(categoryId, page, limit).collect { result ->
                _categoryProductsState.value = result
            }
        }
    }
}

