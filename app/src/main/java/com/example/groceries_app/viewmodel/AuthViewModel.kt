package com.example.groceries_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceries_app.data.model.AuthResponse
import com.example.groceries_app.data.network.Resource
import com.example.groceries_app.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val loginState: StateFlow<Resource<AuthResponse>?> = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow<Resource<AuthResponse>?>(null)
    val signUpState: StateFlow<Resource<AuthResponse>?> = _signUpState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                _loginState.value = result
            }
        }
    }

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.signUp(name, email, password).collect { result ->
                _signUpState.value = result
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun resetSignUpState() {
        _signUpState.value = null
    }
}

