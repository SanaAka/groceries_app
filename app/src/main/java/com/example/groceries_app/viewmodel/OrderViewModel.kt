package com.example.groceries_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.model.OrderRequest
import com.example.groceries_app.data.model.OrderResponse
import com.example.groceries_app.data.model.Status
import com.example.groceries_app.data.repository.NectarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: NectarRepository = NectarRepository.getInstance()
) : ViewModel() {

    private val _isCreatingOrder = MutableStateFlow(false)
    val isCreatingOrder: StateFlow<Boolean> = _isCreatingOrder.asStateFlow()

    private val _createdOrder = MutableStateFlow<OrderResponse?>(null)
    val createdOrder: StateFlow<OrderResponse?> = _createdOrder.asStateFlow()

    private val _orderError = MutableStateFlow<String?>(null)
    val orderError: StateFlow<String?> = _orderError.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderResponse>>(emptyList())
    val orders: StateFlow<List<OrderResponse>> = _orders.asStateFlow()

    fun createOrder(orderRequest: OrderRequest, token: String) {
        viewModelScope.launch {
            _isCreatingOrder.value = true
            _orderError.value = null

            repository.createOrder(orderRequest, token)
                .onSuccess { order ->
                    _createdOrder.value = order
                    _orderError.value = null
                }
                .onFailure { exception ->
                    _orderError.value = exception.message ?: "Failed to create order"
                }

            _isCreatingOrder.value = false
        }
    }

    fun loadOrders(token: String) {
        viewModelScope.launch {
            repository.getAllOrders(token)
                .onSuccess { ordersList ->
                    _orders.value = ordersList
                }
                .onFailure { exception ->
                    _orderError.value = exception.message ?: "Failed to load orders"
                }
        }
    }

    fun clearOrderState() {
        _createdOrder.value = null
        _orderError.value = null
    }
}
