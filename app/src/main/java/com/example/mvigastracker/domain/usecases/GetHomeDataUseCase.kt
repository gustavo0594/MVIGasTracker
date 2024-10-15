package com.example.mvigastracker.domain.usecases

import com.example.mvigastracker.core.zip
import com.example.mvigastracker.domain.entities.HomeData
import com.example.mvigastracker.domain.repositories.GasRecordRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val gasRecordRepository: GasRecordRepository
) {

    operator fun invoke(input: GetHomeDataUseCaseInput): Flow<HomeData> =
        zip(
            gasRecordRepository.getPaymentByMonth(input.year),
            gasRecordRepository.getYearlyReport(input.year),
            gasRecordRepository.getAllByYear(input.year)
        ) { paymentsByMonth, yearlyReport, gasRecords ->
            HomeData(
                yearlyReport = yearlyReport,
                recentFuelUps = gasRecords.takeLast(3),
                monthlyRecordsByYear = paymentsByMonth,
            )
        }

}

data class GetHomeDataUseCaseInput(
    val year: String
)