package com.example.groceries_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.ui.screens.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()
    
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount.asStateFlow()
    
    fun addToCart(item: CartItem) {
        viewModelScope.launch {
            val currentItems = _cartItems.value.toMutableList()
            val existingItem = currentItems.find { it.id == item.id }
            
            if (existingItem != null) {
                // Update quantity if item already exists
                val index = currentItems.indexOf(existingItem)
                currentItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
            } else {
                // Add new item
                currentItems.add(item)
            }
            
            _cartItems.value = currentItems
            calculateTotal()
        }
    }
    
    fun removeFromCart(itemId: String) {
        viewModelScope.launch {
            _cartItems.value = _cartItems.value.filter { it.id != itemId }
            calculateTotal()
        }
    }
    
    fun updateQuantity(itemId: String, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                removeFromCart(itemId)
            } else {
                _cartItems.value = _cartItems.value.map {
                    if (it.id == itemId) it.copy(quantity = newQuantity)
                    else it
                }
                calculateTotal()
            }
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            _cartItems.value = emptyList()
            _totalAmount.value = 0.0
        }
    }
    
    private fun calculateTotal() {
        _totalAmount.value = _cartItems.value.sumOf { it.price * it.quantity }
    }
    
    fun getCartItemCount(): Int {
        return _cartItems.value.sumOf { it.quantity }
    }
}
