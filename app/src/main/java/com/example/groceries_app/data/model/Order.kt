package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id")
    val id: Int,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("items")
    val items: List<OrderItem>,

    @SerializedName("totalAmount")
    val totalAmount: Double,

    @SerializedName("status")
    val status: OrderStatus,

    @SerializedName("deliveryAddress")
    val deliveryAddress: Address,

    @SerializedName("paymentMethod")
    val paymentMethod: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("deliveryDate")
    val deliveryDate: String? = null
)

data class OrderItem(
    @SerializedName("productId")
    val productId: Int,

    @SerializedName("productName")
    val productName: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("imageUrl")
    val imageUrl: String? = null
)

enum class OrderStatus {
    @SerializedName("pending")
    PENDING,

    @SerializedName("confirmed")
    CONFIRMED,

    @SerializedName("processing")
    PROCESSING,

    @SerializedName("shipped")
    SHIPPED,

    @SerializedName("delivered")
    DELIVERED,

    @SerializedName("cancelled")
    CANCELLED
}

data class CreateOrderRequest(
    @SerializedName("items")
    val items: List<CartItemRequest>,

    @SerializedName("deliveryAddress")
    val deliveryAddress: Address,

    @SerializedName("paymentMethod")
    val paymentMethod: String,

    @SerializedName("promoCode")
    val promoCode: String? = null
)

data class CartItemRequest(
    @SerializedName("productId")
    val productId: Int,

    @SerializedName("quantity")
    val quantity: Int
)

data class OrderListResponse(
    @SerializedName("orders")
    val orders: List<Order>,

    @SerializedName("total")
    val total: Int
)

