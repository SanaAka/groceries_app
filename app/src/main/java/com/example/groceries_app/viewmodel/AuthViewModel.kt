package com.example.groceries_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.model.LoginRequest
import com.example.groceries_app.data.model.RegisterRequest
import com.example.groceries_app.data.repository.NectarRepository
import com.example.groceries_app.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val repository: NectarRepository = NectarRepository.getInstance(),
    private val sessionManager: SessionManager? = null
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true

            val request = LoginRequest(email = email, password = password)
            repository.login(request)
                .onSuccess { authResponse ->
                    // Save session
                    sessionManager?.saveAuthTokens(authResponse.accessToken, authResponse.refreshToken)
                    
                    // Fetch user info and save
                    repository.getCurrentUser(authResponse.accessToken)
                        .onSuccess { userResponse ->
                            sessionManager?.saveUserInfo(userResponse.uuid, userResponse.name, userResponse.email)
                        }
                    
                    _authState.value = AuthState.Success("Login successful")
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Login failed")
                }

            _isLoading.value = false
        }
    }

    fun signUp(email: String, password: String, name: String, gender: String = "OTHER") {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true

            val request = RegisterRequest(
                phoneNumber = "+0000000000", // Default placeholder since backend still requires it
                email = email,
                password = password,
                name = name,
                gender = gender
            )
            
            repository.register(request)
                .onSuccess { userResponse ->
                    _authState.value = AuthState.Success("Registration successful. Please login.")
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Registration failed")
                }

            _isLoading.value = false
        }
    }

    fun logout() {
        sessionManager?.clearSession()
        _authState.value = AuthState.Idle
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

