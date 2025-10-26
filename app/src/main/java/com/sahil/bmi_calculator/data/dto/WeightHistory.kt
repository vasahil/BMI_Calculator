package com.sahil.bmi_calculator.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class WeightHistory(
    val weight: String? = "",
    val bmiVal: String = "",
    val timeStamp: Long = 0L
)
