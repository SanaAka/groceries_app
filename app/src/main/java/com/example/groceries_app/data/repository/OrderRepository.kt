package com.example.groceries_app.data.repository

import com.example.groceries_app.data.model.Address
import com.example.groceries_app.data.model.CartItemRequest
import com.example.groceries_app.data.model.CreateOrderRequest
import com.example.groceries_app.data.model.Order
import com.example.groceries_app.data.model.OrderListResponse
import com.example.groceries_app.data.model.*
import com.example.groceries_app.data.network.ApiService
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class OrderRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    fun createOrder(
        token: String,
        items: List<CartItemRequest>,
        deliveryAddress: Address,
        paymentMethod: String,
        promoCode: String? = null
    ): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())
            val request = CreateOrderRequest(items, deliveryAddress, paymentMethod, promoCode)
            val response = apiService.createOrder("Bearer $token", request)

            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error("Failed to create order: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getOrders(token: String, page: Int = 1): Flow<Resource<OrderListResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getOrders("Bearer $token", page)

            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to fetch orders"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getOrderById(token: String, orderId: Int): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getOrderById("Bearer $token", orderId)

            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error("Failed to fetch order details"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)

    fun cancelOrder(token: String, orderId: Int): Flow<Resource<Order>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.cancelOrder("Bearer $token", orderId)

            if (response.isSuccessful && response.body()?.data != null) {
                emit(Resource.Success(response.body()!!.data!!))
            } else {
                emit(Resource.Error("Failed to cancel order"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        }
    }.flowOn(Dispatchers.IO)
}

