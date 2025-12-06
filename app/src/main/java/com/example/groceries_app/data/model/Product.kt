package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName

// Matches nectar-api ProductResponse
data class Product(
    @SerializedName("uuid")
    val uuid: String = "",
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("price")
    val price: Double = 0.0,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    
    @SerializedName("isActive")
    val isActive: Boolean = true,
    
    @SerializedName("category")
    val category: String? = null,
    
    @SerializedName("isFavorite")
    val isFavorite: Boolean = false
)

// Response from GET /api/v1/products
data class ProductsResponse(
    @SerializedName("products")
    val products: List<Product>
)

// Matches nectar-api ProductRequest
data class ProductRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    
    @SerializedName("categoryName")
    val categoryName: String? = null
)

// Matches nectar-api ProductUpdate
data class ProductUpdate(
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("price")
    val price: Double? = null,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    
    @SerializedName("isActive")
    val isActive: Boolean? = null,
    
    @SerializedName("categoryName")
    val categoryName: String? = null
)

// For backwards compatibility with old UI code
data class ProductListResponse(
    @SerializedName("products")
    val products: List<Product>,

    @SerializedName("total")
    val total: Int? = null,

    @SerializedName("page")
    val page: Int? = null,

    @SerializedName("limit")
    val limit: Int? = null
)

// Category data model
data class Category(
    @SerializedName("uuid")
    val uuid: String = "",
    
    @SerializedName("name")
    val name: String = "",
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
    
    @SerializedName("audit")
    val audit: Audit? = null
)

data class Audit(
    @SerializedName("createdBy")
    val createdBy: String? = null,
    
    @SerializedName("updatedBy")
    val updatedBy: String? = null,
    
    @SerializedName("createdAt")
    val createdAt: String? = null,
    
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class CategoriesResponse(
    @SerializedName("categories")
    val categories: List<Category>
)

