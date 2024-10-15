package com.example.mvigastracker.domain.entities

data class GasRecord(
    val id: Int = 0,
    val date: String = "",
    val kilometers: Int = 0,
    val totalPayment: Int = 0,
)
