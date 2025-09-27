package com.Auth

data class User(
    val uid: String,
    val email: String,
    val name: String,
    val profileImage: String? = null
){
    fun toMap(): Map<String, Any?> = mapOf(
        "uid" to uid,
        "email" to email,
        "name" to name,
        "profileImage" to profileImage
    )
}

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

data class RegisterFormState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoadings: Boolean = false,
    val error: String? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "name" to name,
        "email" to email,
        "password" to password,
        "confirmPassword" to confirmPassword,
        "isLoadings" to isLoadings,
        "error" to error
    )
}