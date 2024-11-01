package com.example.mvigastracker.ui.home.mappers

import com.example.mvigastracker.core.dateFormat
import com.example.mvigastracker.core.currencyFormat
import com.example.mvigastracker.core.toMonthStringRes
import com.example.mvigastracker.domain.entities.FuelMonthlyValue
import com.example.mvigastracker.domain.entities.GasRecord
import com.example.mvigastracker.domain.entities.HomeData
import com.example.mvigastracker.domain.entities.AnnualReport
import com.example.mvigastracker.ui.home.entities.HomeChart
import com.example.mvigastracker.ui.home.entities.HomeChartItem
import com.example.mvigastracker.ui.home.entities.HomeViewState
import com.example.mvigastracker.ui.home.entities.UIFuelUp
import com.example.mvigastracker.ui.home.entities.UIAnnualReport

fun HomeData.toViewState(): HomeViewState =
    HomeViewState(
        paymentChartData = monthlyRecordsByYear.toPaymentChart(),
        distanceChartData = monthlyRecordsByYear.toDistanceChart(),
        lastFuelUp = recentFuelUps.lastOrNull()?.toUIFuelUp(),
        recentFuelUps = recentFuelUps.map { it.toUIFuelUp() },
        annualReport = annualReport.toUI(),
    )


private fun List<FuelMonthlyValue>.toPaymentChart(): HomeChart {
    var maxValue = 0
    val dataset = mutableListOf<HomeChartItem>()
    forEach {
        if (it.payment > maxValue) {
            maxValue = it.payment
        }
        dataset.add(
            HomeChartItem(
                value = it.payment.toFloat(),
                label = it.month.toMonthStringRes()
            )
        )
    }
    return HomeChart(
        dataSet = dataset,
        scale = (maxValue / 5).toFloat()
    )
}

private fun List<FuelMonthlyValue>.toDistanceChart(): HomeChart {
    var maxValue = 0
    val dataset = mutableListOf<HomeChartItem>()
    forEach {
        if (it.kilometers > maxValue) {
            maxValue = it.kilometers
        }
        dataset.add(
            HomeChartItem(
                value = it.kilometers.toFloat(),
                label = it.month.toMonthStringRes()
            )
        )
    }
    return HomeChart(
        dataSet = dataset,
        scale = (maxValue / 5).toFloat()
    )
}

private fun GasRecord.toUIFuelUp(): UIFuelUp =
    UIFuelUp(
        date = date.dateFormat(),
        kilometers = "$kilometers km",
        totalAmount = totalPayment.currencyFormat()
    )

private fun AnnualReport.toUI(): UIAnnualReport =
    UIAnnualReport(
        totalDistance = "$totalKilometers km",
        differenceFromLastEntry = "+ $distanceDifference from last entry",
        totalAmount = totalPayment.currencyFormat(),
        paymentPerKm = "$paymentPerKm per km / $paymentPerMonth by Month"
    )
