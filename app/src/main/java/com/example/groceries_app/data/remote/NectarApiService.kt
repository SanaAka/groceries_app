package com.example.groceries_app.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface NectarApiService {
    @GET("/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("/categories")
    suspend fun getCategories(): List<CategoryDto>

    @GET("/products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductDto
}

