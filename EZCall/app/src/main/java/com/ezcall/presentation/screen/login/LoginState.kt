package com.ezcall.presentation.screen.login

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val errorMessage: String) : LoginState()
}
