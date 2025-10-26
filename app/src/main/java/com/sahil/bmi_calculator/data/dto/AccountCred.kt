package com.sahil.bmi_calculator.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccountCred(
        val email:String,
        val password:String
)