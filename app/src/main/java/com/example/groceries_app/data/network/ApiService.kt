package com.example.groceries_app.data.network

import com.example.groceries_app.data.model.ApiResponse
import com.example.groceries_app.data.model.AuthResponse
import com.example.groceries_app.data.model.CategoryListResponse
import com.example.groceries_app.data.model.CreateOrderRequest
import com.example.groceries_app.data.model.LoginRequest
import com.example.groceries_app.data.model.Order
import com.example.groceries_app.data.model.OrderListResponse
import com.example.groceries_app.data.model.Product
import com.example.groceries_app.data.model.ProductListResponse
import com.example.groceries_app.data.model.SignUpRequest
import com.example.groceries_app.data.model.User
import com.example.groceries_app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth endpoints
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<Unit>>

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ApiResponse<User>>

    // Product endpoints
    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("sort") sort: String? = null
    ): Response<ProductListResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") productId: Int): Response<ApiResponse<Product>>

    @GET("products/featured")
    suspend fun getFeaturedProducts(
        @Query("limit") limit: Int = 10
    ): Response<ProductListResponse>

    @GET("products/best-selling")
    suspend fun getBestSellingProducts(
        @Query("limit") limit: Int = 10
    ): Response<ProductListResponse>

    // Category endpoints
    @GET("categories")
    suspend fun getCategories(): Response<CategoryListResponse>

    @GET("categories/{id}/products")
    suspend fun getProductsByCategory(
        @Path("id") categoryId: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ProductListResponse>

    // Favorites endpoints
    @GET("favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String
    ): Response<ProductListResponse>

    @POST("favorites/{productId}")
    suspend fun addToFavorites(
        @Header("Authorization") token: String,
        @Path("productId") productId: Int
    ): Response<ApiResponse<Unit>>

    @DELETE("favorites/{productId}")
    suspend fun removeFromFavorites(
        @Header("Authorization") token: String,
        @Path("productId") productId: Int
    ): Response<ApiResponse<Unit>>

    // Order endpoints
    @POST("orders")
    suspend fun createOrder(
        @Header("Authorization") token: String,
        @Body request: CreateOrderRequest
    ): Response<ApiResponse<Order>>

    @GET("orders")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<OrderListResponse>

    @GET("orders/{id}")
    suspend fun getOrderById(
        @Header("Authorization") token: String,
        @Path("id") orderId: Int
    ): Response<ApiResponse<Order>>

    @PUT("orders/{id}/cancel")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Path("id") orderId: Int
    ): Response<ApiResponse<Order>>

    // Search endpoint
    @GET("search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<ProductListResponse>
}

