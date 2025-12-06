package com.example.groceries_app.data.repository

import com.example.groceries_app.data.api.RetrofitClient
import com.example.groceries_app.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NectarRepository {
    private val api = RetrofitClient.nectarApi
    
    // Auth operations
    suspend fun register(request: RegisterRequest): Result<UserResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.register(request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun login(request: LoginRequest): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.login(request)
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                // Save token for future requests
                RetrofitClient.setAuthToken(authResponse.accessToken)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun refreshToken(refreshToken: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                // Update token
                RetrofitClient.setAuthToken(authResponse.accessToken)
                Result.success(authResponse)
            } else {
                Result.failure(Exception("Token refresh failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getCurrentUser(token: String): Result<UserResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCurrentUser("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get user: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Product operations
    suspend fun getAllProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllProducts()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.products)
            } else {
                Result.failure(Exception("Failed to get products: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProductByUuid(uuid: String): Result<Product> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProductByUuid(uuid)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get product: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getAllCategories(): Result<List<Category>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllCategories()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.categories)
            } else {
                Result.failure(Exception("Failed to get categories: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createProduct(token: String, request: ProductRequest): Result<Product> = withContext(Dispatchers.IO) {
        try {
            val response = api.createProduct("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create product: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Order operations
    suspend fun getAllOrders(token: String): Result<List<OrderResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllOrders("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.orders)
            } else {
                Result.failure(Exception("Failed to get orders: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun createOrder(request: OrderRequest, token: String): Result<OrderResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.createOrder("Bearer $token", request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to create order: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        @Volatile
        private var instance: NectarRepository? = null
        
        fun getInstance(): NectarRepository {
            return instance ?: synchronized(this) {
                instance ?: NectarRepository().also { instance = it }
            }
        }
    }
}
