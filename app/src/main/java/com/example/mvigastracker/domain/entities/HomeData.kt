package com.example.mvigastracker.domain.entities

data class HomeData(
    val annualReport: AnnualReport,
    val recentFuelUps: List<GasRecord>,
    val monthlyRecordsByYear: List<FuelMonthlyValue>,
)