package com.example.mvigastracker.domain.entities

data class AnnualReport(
    val totalKilometers: Int,
    val totalPayment: Int,
    val paymentPerKm:Int,
    val distanceDifference: Int,
    val paymentPerMonth: Int
)
