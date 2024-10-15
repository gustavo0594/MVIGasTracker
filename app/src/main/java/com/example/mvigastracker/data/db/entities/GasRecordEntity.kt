package com.example.mvigastracker.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gas_record")
data class GasRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val recordDate: String,
    val kilometers: Int,
    val totalPayment: Int
)