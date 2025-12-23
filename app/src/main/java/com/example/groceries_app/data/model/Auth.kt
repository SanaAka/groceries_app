package com.example.groceries_app.data.model

import com.google.gson.annotations.SerializedName

// Matches nectar-api RegisterRequest
data class RegisterRequest(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("gender")
    val gender: String, // MALE, FEMALE, or OTHER
    
    @SerializedName("dob")
    val dob: String? = null // LocalDate as string (yyyy-MM-dd)
)

// Matches nectar-api LoginRequest  
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

// Matches nectar-api AuthResponse
data class AuthResponse(
    @SerializedName("tokenType")
    val tokenType: String,
    
    @SerializedName("accessToken")
    val accessToken: String,
    
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

// Matches nectar-api UserResponse
data class UserResponse(
    @SerializedName("uuid")
    val uuid: String,
    
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("profileImage")
    val profileImage: String? = null,
    
    @SerializedName("gender")
    val gender: String,
    
    @SerializedName("dob")
    val dob: String? = null,
    
    @SerializedName("cityOrProvince")
    val cityOrProvince: String? = null,
    
    @SerializedName("khanOrDistrict")
    val khanOrDistrict: String? = null,
    
    @SerializedName("sangkatOrCommune")
    val sangkatOrCommune: String? = null,
    
    @SerializedName("village")
    val village: String? = null,
    
    @SerializedName("street")
    val street: String? = null,
    
    @SerializedName("isDeleted")
    val isDeleted: Boolean = false,
    
    @SerializedName("isBlocked")
    val isBlocked: Boolean = false,
    
    @SerializedName("roles")
    val roles: List<String> = emptyList(),

    @SerializedName("audit")
    val audit: AuditInfo? = null
)

data class AuditInfo(
    @SerializedName("createdBy")
    val createdBy: String,

    @SerializedName("updatedBy")
    val updatedBy: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String
)

