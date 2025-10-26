package com.sahil.bmi_calculator.presentation.HomeScreen

import com.sahil.bmi_calculator.data.dto.UserDetails

data class HomeScreenState(
    val loading: Boolean = false,
    val error: String?= null,
    val success: UserDetails?=null
)