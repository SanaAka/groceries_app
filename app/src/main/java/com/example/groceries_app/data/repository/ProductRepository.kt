package com.example.groceries_app.data.repository

import com.example.groceries_app.data.model.Product
import com.example.groceries_app.data.model.ProductListResponse
import com.example.groceries_app.data.model.*
import com.example.groceries_app.data.network.ApiService
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    fun getProducts(
        page: Int = 1,
        limit: Int = 20,
        category: String? = null,
        search: String? = null,
        sort: String? = null
    ): Flow<Resource<ProductListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getProducts(page, limit, category, search, sort)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to fetch products: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getProductById(productId: Int): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getProductById(productId)

            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error("Failed to fetch product details"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getFeaturedProducts(limit: Int = 10): Flow<Resource<ProductListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getFeaturedProducts(limit)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to fetch featured products"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getBestSellingProducts(limit: Int = 10): Flow<Resource<ProductListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getBestSellingProducts(limit)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to fetch best selling products"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun searchProducts(query: String, page: Int = 1): Flow<Resource<ProductListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.searchProducts(query, page)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Search failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)
}

