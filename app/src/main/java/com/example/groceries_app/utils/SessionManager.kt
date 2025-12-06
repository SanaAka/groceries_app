package com.example.groceries_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.groceries_app.data.api.RetrofitClient

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREFS_NAME, 
        Context.MODE_PRIVATE
    )
    
    fun saveAuthTokens(accessToken: String, refreshToken: String) {
        prefs.edit().apply {
            putString(Constants.KEY_ACCESS_TOKEN, accessToken)
            putString(Constants.KEY_REFRESH_TOKEN, refreshToken)
            putBoolean(Constants.KEY_IS_LOGGED_IN, true)
            apply()
        }
        // Update RetrofitClient with new token
        RetrofitClient.setAuthToken(accessToken)
    }
    
    fun saveUserInfo(uuid: String, username: String, email: String) {
        prefs.edit().apply {
            putString(Constants.KEY_USER_UUID, uuid)
            putString(Constants.KEY_USERNAME, username)
            putString(Constants.KEY_EMAIL, email)
            apply()
        }
    }
    
    fun getAccessToken(): String? {
        return prefs.getString(Constants.KEY_ACCESS_TOKEN, null)
    }
    
    fun getRefreshToken(): String? {
        return prefs.getString(Constants.KEY_REFRESH_TOKEN, null)
    }
    
    fun getUserUuid(): String? {
        return prefs.getString(Constants.KEY_USER_UUID, null)
    }
    
    fun getUsername(): String? {
        return prefs.getString(Constants.KEY_USERNAME, null)
    }
    
    fun getEmail(): String? {
        return prefs.getString(Constants.KEY_EMAIL, null)
    }
    
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false)
    }
    
    fun clearSession() {
        prefs.edit().clear().apply()
        RetrofitClient.setAuthToken(null)
    }
    
    companion object {
        @Volatile
        private var instance: SessionManager? = null
        
        fun getInstance(context: Context): SessionManager {
            return instance ?: synchronized(this) {
                instance ?: SessionManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
