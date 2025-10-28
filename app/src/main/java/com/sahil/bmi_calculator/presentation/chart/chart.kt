package com.sahil.bmi_calculator.presentation.chart

import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.sahil.bmi_calculator.data.dto.WeightHistory

fun List<WeightHistory>.toChartEntries(): List<FloatEntry> {
    return this.mapIndexedNotNull { index, history ->
        val bmi = history.bmiVal.toFloatOrNull()
        bmi?.let {
            FloatEntry(
                x = index.toFloat(),
                y = it
            )
        }
    }
}