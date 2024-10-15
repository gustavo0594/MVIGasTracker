package com.example.mvigastracker.domain.repositories

import com.example.mvigastracker.domain.entities.FuelMonthlyValue
import com.example.mvigastracker.domain.entities.GasRecord
import com.example.mvigastracker.domain.entities.YearlyReport
import kotlinx.coroutines.flow.Flow

interface GasRecordRepository {

    suspend fun insertNewRecord(record: GasRecord)
    suspend fun updateRecord(record: GasRecord)
    fun getAll(): Flow<List<GasRecord>>
    suspend fun getById(id: Int): GasRecord?
    fun getAllByYear(year: String): Flow<List<GasRecord>>
    fun getPaymentByMonth(year: String): Flow<List<FuelMonthlyValue>>
    fun getYearlyReport(year: String): Flow<YearlyReport>
}