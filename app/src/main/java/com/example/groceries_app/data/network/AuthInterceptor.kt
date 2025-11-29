package com.example.groceries_app.data.network

import android.content.Context
import com.example.gsshop.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenManager = TokenManager(context)
        val token = tokenManager.getAuthToken()

        val requestBuilder = chain.request().newBuilder()

        // Add authorization header if token exists
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        // Add common headers
        requestBuilder
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")

        return chain.proceed(requestBuilder.build())
    }
}

