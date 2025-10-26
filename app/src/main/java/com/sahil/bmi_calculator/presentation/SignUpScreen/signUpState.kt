package com.sahil.bmi_calculator.presentation.SignUpScreen

data class SignUpState (
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
)