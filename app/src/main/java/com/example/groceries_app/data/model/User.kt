package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String? = null,

    @SerializedName("avatar")
    val avatarUrl: String? = null,

    @SerializedName("address")
    val address: Address? = null
)

data class Address(
    @SerializedName("street")
    val street: String,

    @SerializedName("city")
    val city: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("zipCode")
    val zipCode: String,

    @SerializedName("country")
    val country: String
)

