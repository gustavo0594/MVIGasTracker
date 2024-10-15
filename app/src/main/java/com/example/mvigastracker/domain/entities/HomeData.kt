package com.example.mvigastracker.domain.entities

data class HomeData(
    val yearlyReport: YearlyReport,
    val recentFuelUps: List<GasRecord>,
    val monthlyRecordsByYear: List<FuelMonthlyValue>,
)