package com.example.mvigastracker.data.db.entities

data class YearlyReportEntity(
    val totalKilometers: Int,
    val totalPayment: Int,
    val paymentPerKm: Int,
    val distanceDifference: Int
)
