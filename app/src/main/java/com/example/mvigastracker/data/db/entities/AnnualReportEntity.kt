package com.example.mvigastracker.data.db.entities

data class AnnualReportEntity(
    val totalKilometers: Int,
    val totalPayment: Int,
    val paymentPerKm: Int,
    val distanceDifference: Int,
    val paymentPerMonth: Int
)
