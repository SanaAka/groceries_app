package com.example.groceries_app.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FavoriteProduct(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val category: String?
)

class FavoritesViewModel(context: Context) : ViewModel() {
    private val prefs: SharedPreferences = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    private val _favorites = MutableStateFlow<List<FavoriteProduct>>(emptyList())
    val favorites: StateFlow<List<FavoriteProduct>> = _favorites.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    private fun loadFavorites() {
        val json = prefs.getString("favorites", null)
        if (json != null) {
            val type = object : TypeToken<List<FavoriteProduct>>() {}.type
            _favorites.value = gson.fromJson(json, type)
        }
    }
    
    private fun saveFavorites() {
        val json = gson.toJson(_favorites.value)
        prefs.edit().putString("favorites", json).apply()
    }
    
    fun isFavorite(productId: String): Boolean {
        return _favorites.value.any { it.id == productId }
    }
    
    fun toggleFavorite(product: FavoriteProduct) {
        val currentFavorites = _favorites.value.toMutableList()
        val existingIndex = currentFavorites.indexOfFirst { it.id == product.id }
        
        if (existingIndex >= 0) {
            // Remove from favorites
            currentFavorites.removeAt(existingIndex)
        } else {
            // Add to favorites
            currentFavorites.add(product)
        }
        
        _favorites.value = currentFavorites
        saveFavorites()
    }
    
    fun addFavorite(product: FavoriteProduct) {
        if (!isFavorite(product.id)) {
            val currentFavorites = _favorites.value.toMutableList()
            currentFavorites.add(product)
            _favorites.value = currentFavorites
            saveFavorites()
        }
    }
    
    fun removeFavorite(productId: String) {
        val currentFavorites = _favorites.value.toMutableList()
        currentFavorites.removeAll { it.id == productId }
        _favorites.value = currentFavorites
        saveFavorites()
    }
    
    fun clearAllFavorites() {
        _favorites.value = emptyList()
        saveFavorites()
    }
}
