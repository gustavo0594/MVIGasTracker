package com.example.mvigastracker.data.mappers

import com.example.mvigastracker.data.db.entities.GasRecordEntity
import com.example.mvigastracker.data.db.entities.MonthlyValuesEntity
import com.example.mvigastracker.data.db.entities.AnnualReportEntity
import com.example.mvigastracker.domain.entities.FuelMonthlyValue
import com.example.mvigastracker.domain.entities.GasRecord
import com.example.mvigastracker.domain.entities.AnnualReport

fun GasRecord.toEntity(): GasRecordEntity =
    GasRecordEntity(
        id = id,
        recordDate = date,
        kilometers = kilometers,
        totalPayment = totalPayment,
    )

fun GasRecordEntity.toRecord(): GasRecord =
    GasRecord(
        id = id,
        date = recordDate,
        kilometers = kilometers,
        totalPayment = totalPayment,
    )

fun MonthlyValuesEntity.toRecord(): FuelMonthlyValue =
    FuelMonthlyValue(
        month = month,
        payment = payment,
        kilometers = kilometers,
    )

fun AnnualReportEntity.toRecord(): AnnualReport =
    AnnualReport(
        totalKilometers = totalKilometers,
        totalPayment = totalPayment,
        paymentPerKm = paymentPerKm,
        distanceDifference = distanceDifference,
        paymentPerMonth = paymentPerMonth
    )