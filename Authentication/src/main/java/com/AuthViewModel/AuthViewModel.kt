package com.AuthViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.Auth.AuthRepository
import com.Auth.AuthState
import com.Auth.RegisterFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _loginState = MutableStateFlow(LoginFormState())
    val loginState: StateFlow<LoginFormState> = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow(RegisterFormState())
    val registerState: StateFlow<RegisterFormState> = _registerState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.getAuthState().collect { state ->
                _authState.value = state
            }
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = _loginState.value.copy(isLoadings = true, error = null)

        when (val ressult = authRepository.login(email, password)) {
            is Result.Success -> {
                _loginState.value = _loginState.value.copy(isLoading = false, error = null)
            }
            is Result.Failure -> {
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    error = ressult.exception.message ?: "Error al iniciar sesión"
                )
            }
        }
    }

    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _registerState.value = _registerState.value.copy(isLoadings = true, error = null)

        when (val result = authRepository.register(name, email, password)) {
            is Result.Failure -> {
                _registerState.value = _registerState.value.copy(
                    isLoadings = false,
                    error = result.exception.message ?: "Error al registrar el usuario"
                )
            }
        }
    }

    fun logout() = viewModelScope.launch {
        authRepository.logout()
    }

    fun updateLoginEmail(email: String) {
        _loginState.value = _loginState.value.copy(email = email)
    }

    fun updateLoginPassword(password: String) {
        _loginState.value = _loginState.value.copy(password = password)

    }

    fun updateRegisterName(name: String) {
        _registerState.value = _registerState.value.copy(name = name)
    }

    fun updateRegisterEmail(email: String) {
        _registerState.value = _registerState.value.copy(email = email)
    }

    fun updateRegisterPassword(password: String) {
        _registerState.value = _registerState.value.copy(password = password)
    }

    fun updateRegisterConfirmPassword(confirmPassword: String) {
        _registerState.value = _registerState.value.copy(confirmPassword = confirmPassword)
    }

    fun clearLoginError() {
        _loginState.value = _loginState.value.copy(error = null)
    }

    fun clearRegisterError() {
        _registerState.value = _registerState.value.copy(error = null)
    }
}