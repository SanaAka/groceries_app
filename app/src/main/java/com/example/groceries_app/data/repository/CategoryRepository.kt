package com.example.groceries_app.data.repository

import com.example.groceries_app.data.model.CategoryListResponse
import com.example.groceries_app.data.model.ProductListResponse
import com.example.groceries_app.data.network.ApiService
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CategoryRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    fun getCategories(): Flow<Resource<CategoryListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getCategories()

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to fetch categories"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getProductsByCategory(
        categoryId: Int,
        page: Int = 1,
        limit: Int = 20
    ): Flow<Resource<ProductListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getProductsByCategory(categoryId, page, limit)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to fetch products"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)
}

