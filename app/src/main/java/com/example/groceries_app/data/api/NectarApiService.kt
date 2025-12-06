package com.example.groceries_app.data.api

import com.example.groceries_app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface NectarApiService {
    
    // Auth endpoints
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>
    
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<AuthResponse>
    
    // User endpoints
    @GET("api/v1/users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<UserResponse>
    
    // Product endpoints
    @GET("api/v1/products")
    suspend fun getAllProducts(): Response<ProductsResponse>
    
    @GET("api/v1/products/{uuid}")
    suspend fun getProductByUuid(@Path("uuid") uuid: String): Response<Product>
    
    @POST("api/v1/products")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body request: ProductRequest
    ): Response<Product>
    
    @PUT("api/v1/products/{uuid}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("uuid") uuid: String,
        @Body request: ProductUpdate
    ): Response<Product>
    
    @DELETE("api/v1/products/{uuid}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("uuid") uuid: String
    ): Response<Unit>
    
    // Category endpoints
    @GET("api/v1/categories")
    suspend fun getAllCategories(): Response<CategoriesResponse>
    
    // Order endpoints
    @GET("api/v1/orders")
    suspend fun getAllOrders(
        @Header("Authorization") token: String
    ): Response<OrdersListResponse>
    
    @POST("api/v1/orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: OrderRequest
    ): Response<OrderResponse>
}
