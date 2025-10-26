package com.sahil.bmi_calculator.common

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun calculate(weight: String?, height: String?): String? {
    val weightFloat = weight?.toFloatOrNull()
    val heightFloat = height?.toFloatOrNull()

    if (weightFloat == null || weightFloat <= 0f) return null
    if (heightFloat == null || heightFloat <= 0f) return null

    val heightM = heightFloat / 100f
    val bmi = weightFloat / (heightM * heightM)

    return String.format("%.2f", bmi)
}