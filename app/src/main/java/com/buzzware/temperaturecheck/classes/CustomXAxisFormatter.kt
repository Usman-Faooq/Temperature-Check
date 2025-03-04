package com.buzzware.temperaturecheck.classes

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomXAxisFormatter(private val labels: List<String>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return if (value.toInt() in labels.indices) labels[value.toInt()] else ""
    }
}
