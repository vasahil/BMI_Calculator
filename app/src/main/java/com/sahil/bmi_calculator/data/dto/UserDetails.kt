package com.sahil.bmi_calculator.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val name: String? = "",
    val lastName: String = "",
    val age: String? = "",
    val weight: String? = "",
    val height: String? = "",
    val gender: String = ""
)