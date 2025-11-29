package com.example.groceries_app.data.remote

class Repository(private val api: NectarApiService = RetrofitClient.api) {
    suspend fun getProducts() = api.getProducts()
    suspend fun getCategories() = api.getCategories()
    suspend fun getProduct(id: Int) = api.getProduct(id)
}

