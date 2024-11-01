package com.example.mvigastracker.ui.home.entities

data class HomeViewState(
    val paymentChartData: HomeChart,
    val distanceChartData: HomeChart,
    val lastFuelUp: UIFuelUp?,
    val recentFuelUps: List<UIFuelUp>,
    val annualReport: UIAnnualReport,
)

data class UIFuelUp(
    val date: String,
    val kilometers: String,
    val totalAmount: String,
)

data class UIAnnualReport(
    val totalDistance: String,
    val differenceFromLastEntry: String,
    val totalAmount: String,
    val paymentPerKm: String,
)


