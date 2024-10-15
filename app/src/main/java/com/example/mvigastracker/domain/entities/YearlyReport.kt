package com.example.mvigastracker.domain.entities

data class YearlyReport(
    val totalKilometers: Int,
    val totalPayment: Int,
    val paymentPerKm:Int,
    val distanceDifference: Int
)
