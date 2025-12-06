package com.example.groceries_app.utils

object Constants {
    // API Configuration
    // For Android Emulator: use 10.0.2.2 to access localhost
    // For Physical Device: use your computer's IP address (e.g., 192.168.1.100)
    const val BASE_URL = "http://10.0.2.2:8080/"
    
    // SharedPreferences keys
    const val PREFS_NAME = "nectar_prefs"
    const val KEY_ACCESS_TOKEN = "access_token"
    const val KEY_REFRESH_TOKEN = "refresh_token"
    const val KEY_USER_UUID = "user_uuid"
    const val KEY_USERNAME = "username"
    const val KEY_EMAIL = "email"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    
    // API Timeouts
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
