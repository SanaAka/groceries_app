package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("icon")
    val iconUrl: String? = null,

    @SerializedName("color")
    val color: String? = null,

    @SerializedName("productCount")
    val productCount: Int = 0
)

data class CategoryListResponse(
    @SerializedName("categories")
    val categories: List<Category>,

    @SerializedName("total")
    val total: Int
)

