package com.example.mvigastracker.ui.home.entities

import androidx.annotation.StringRes

data class HomeChart(
    val dataSet: List<HomeChartItem>,
    val scale: Float,
)

data class HomeChartItem(
    val value: Float,
    @StringRes val label: Int
)
