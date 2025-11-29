package com.example.groceries_app.data.repository

import com.example.groceries_app.data.model.AuthResponse
import com.example.groceries_app.data.model.LoginRequest
import com.example.groceries_app.data.model.SignUpRequest
import com.example.groceries_app.data.model.User
import com.example.groceries_app.data.model.*
import com.example.groceries_app.data.network.ApiService
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    fun login(email: String, password: String): Flow<Resource<AuthResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun signUp(name: String, email: String, password: String): Flow<Resource<AuthResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.signUp(SignUpRequest(name, email, password))

            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error("Sign up failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun logout(token: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.logout("Bearer $token")

            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Logout failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getProfile(token: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getProfile("Bearer $token")

            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error("Failed to fetch profile"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)
}

