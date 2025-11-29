package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")  // DummyJSON uses "title" instead of "name"
    val title: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("price")
    val price: Double = 0.0,

    @SerializedName("image")
    val imageUrl: String? = null,

    @SerializedName("thumbnail")  // DummyJSON uses "thumbnail" for image
    val thumbnail: String? = null,

    @SerializedName("category")
    val category: String? = null,

    @SerializedName("rating")
    val rating: Double? = null,

    @SerializedName("stock")
    val stock: Int? = null,

    @SerializedName("weight")
    val weight: String? = null,

    @SerializedName("unit")
    val unit: String? = null,

    @SerializedName("discount")
    val discount: Double? = null,

    @SerializedName("discountPercentage")  // DummyJSON field
    val discountPercentage: Double? = null,

    @SerializedName("isFavorite")
    val isFavorite: Boolean = false
) {
    // Helper property to get the actual name from either field
    val productName: String
        get() = title ?: name ?: "Unknown Product"
}

data class ProductListResponse(
    @SerializedName("products")
    val products: List<Product>,

    @SerializedName("total")
    val total: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("limit")
    val limit: Int
)

