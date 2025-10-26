package com.sahil.bmi_calculator.presentation.ProfileScreen

data class ProfileState(
    val loading: Boolean = false,
    val success: String? = null,
    val error: String? = null
)
