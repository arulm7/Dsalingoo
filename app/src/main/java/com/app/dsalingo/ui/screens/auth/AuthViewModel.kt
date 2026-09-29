package com.app.dsalingo.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dsalingo.data.network.LoginRequest
import com.app.dsalingo.data.network.RegisterRequest
import com.app.dsalingo.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val response = userRepository.login(LoginRequest(email, password))
            _isLoading.value = false
            if (response.status == "success") {
                onSuccess()
            } else {
                _error.value = response.message
            }
        }
    }

    fun register(username: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val response = userRepository.register(RegisterRequest(username, email, password))
            _isLoading.value = false
            if (response.status == "success") {
                onSuccess()
            } else {
                _error.value = response.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
