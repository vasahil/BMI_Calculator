package com.sahil.bmi_calculator.presentation.LoginScreen

data class LoginState(
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)