package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

// Status enum matching nectar-api
enum class Status {
    @SerializedName("PENDING") PENDING,
    @SerializedName("PAID") PAID,
    @SerializedName("SHIPPED") SHIPPED,
    @SerializedName("COMPLETED") COMPLETED,
    @SerializedName("CANCELLED") CANCELLED
}

// Order response from API
data class OrderResponse(
    @SerializedName("uuid")
    val uuid: String,
    
    @SerializedName("orderNumber")
    val orderNumber: String,
    
    @SerializedName("totalPrice")
    val totalPrice: BigDecimal,
    
    @SerializedName("status")
    val status: Status,
    
    @SerializedName("items")
    val items: List<OrderItemResponse>,
    
    @SerializedName("audit")
    val audit: AuditResponse? = null
)

// Order item response from API
data class OrderItemResponse(
    @SerializedName("uuid")
    val uuid: String,
    
    @SerializedName("productName")
    val productName: String,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: BigDecimal
)

// Audit metadata
data class AuditResponse(
    @SerializedName("createdAt")
    val createdAt: String? = null,
    
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    
    @SerializedName("createdBy")
    val createdBy: String? = null,
    
    @SerializedName("updatedBy")
    val updatedBy: String? = null
)

// Order request to create an order
data class OrderRequest(
    @SerializedName("orderNumber")
    val orderNumber: String,
    
    @SerializedName("totalPrice")
    val totalPrice: BigDecimal,
    
    @SerializedName("status")
    val status: Status,
    
    @SerializedName("items")
    val items: List<OrderItemRequest>
)

// Order item request
data class OrderItemRequest(
    @SerializedName("productUuid")
    val productUuid: String,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: BigDecimal
)

// Wrapper for GET /orders response
data class OrdersListResponse(
    @SerializedName("orders")
    val orders: List<OrderResponse>
)

