package com.example.groceries_app.examples

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.model.*
import com.example.groceries_app.data.repository.NectarRepository
import com.example.groceries_app.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Example ViewModel showing how to use the NectarRepository
 * to interact with your nectar-api backend
 */
class NectarExampleViewModel(private val context: Context) : ViewModel() {
    
    private val repository = NectarRepository.getInstance()
    private val sessionManager = SessionManager.getInstance(context)
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // Example 1: User Registration
    fun registerUser(phoneNumber: String, email: String, password: String, name: String, gender: String, dob: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val request = RegisterRequest(
                phoneNumber = phoneNumber,
                email = email,
                password = password,
                name = name,
                gender = gender,
                dob = dob
            )
            
            repository.register(request)
                .onSuccess { userResponse ->
                    // Save user info
                    sessionManager.saveUserInfo(
                        userResponse.uuid,
                        userResponse.email,
                        userResponse.email
                    )
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }
    
    // Example 2: User Login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val request = LoginRequest(email, password)
            
            repository.login(request)
                .onSuccess { authResponse ->
                    // Save tokens
                    sessionManager.saveAuthTokens(
                        authResponse.accessToken,
                        authResponse.refreshToken
                    )
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }
    
    // Example 3: Fetch All Products
    fun fetchProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllProducts()
                .onSuccess { productList ->
                    _products.value = productList
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }
    
    // Example 4: Create an Order
    fun createOrder(orderNumber: String, totalPrice: java.math.BigDecimal, status: Status, items: List<OrderItemRequest>) {
        viewModelScope.launch {
            val token = sessionManager.getAccessToken() ?: run {
                _error.value = "Not logged in"
                return@launch
            }
            
            _isLoading.value = true
            val request = OrderRequest(
                orderNumber = orderNumber,
                totalPrice = totalPrice,
                status = status,
                items = items
            )
            
            repository.createOrder(request, token)
                .onSuccess { order ->
                    // Order created successfully
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }
    
    // Example 5: Get User Orders
    fun fetchUserOrders() {
        viewModelScope.launch {
            val token = sessionManager.getAccessToken() ?: run {
                _error.value = "Not logged in"
                return@launch
            }
            
            _isLoading.value = true
            repository.getAllOrders(token)
                .onSuccess { orders ->
                    // Handle orders
                    _error.value = null
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            _isLoading.value = false
        }
    }
    
    // Example 6: Logout
    fun logout() {
        sessionManager.clearSession()
    }
}
