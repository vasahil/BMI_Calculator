package com.sahil.bmi_calculator.presentation.chart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.sahil.bmi_calculator.data.dto.WeightHistory

@Composable
fun WeightGraph(history: List<WeightHistory>) {

    if (history.isEmpty()) {
        Text("Not enough data")
        return
    }

    val entries = remember(history) {
        history.mapIndexedNotNull { index, record ->
            record.bmiVal.toFloatOrNull()?.let {
                FloatEntry(
                    x = index.toFloat(),
                    y = it
                )
            }
        }
    }

    val model = entryModelOf(entries)

    Chart(
        modifier = Modifier.fillMaxWidth(),
        chart = lineChart(),
        model = model,
        startAxis = rememberStartAxis(
            valueFormatter = AxisValueFormatter { value, _ ->
                "%.1f".format(value)
            }
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = AxisValueFormatter { value, _ ->
                val index = value.toInt()
                if (index in history.indices)
                    "W${index + 1}"
                else ""
            }
        )
    )
}
