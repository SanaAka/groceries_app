package com.example.groceries_app.data.repository

import com.example.groceries_app.data.model.ProductListResponse
import com.example.groceries_app.data.network.ApiService
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoriteRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    fun getFavorites(token: String): Flow<Resource<ProductListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getFavorites("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to fetch favorites"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun addToFavorites(token: String, productId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.addToFavorites("Bearer $token", productId)

            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Failed to add to favorites"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun removeFromFavorites(token: String, productId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.removeFromFavorites("Bearer $token", productId)

            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Failed to remove from favorites"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)
}

