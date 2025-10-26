package com.sahil.bmi_calculator.presentation.DetailScreen

data class DetailState (
    val loading: Boolean = false,
    val success: String?=null,
    val error: String?=null
)