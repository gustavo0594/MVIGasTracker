package com.example.mvigastracker.data.repositories

import com.example.mvigastracker.data.db.dao.GasRecordDao
import com.example.mvigastracker.data.mappers.toEntity
import com.example.mvigastracker.data.mappers.toRecord
import com.example.mvigastracker.domain.entities.FuelMonthlyValue
import com.example.mvigastracker.domain.entities.GasRecord
import com.example.mvigastracker.domain.entities.AnnualReport
import com.example.mvigastracker.domain.repositories.GasRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GasRecordRepositoryImp @Inject constructor(
    private val gasRecordDao: GasRecordDao
) : GasRecordRepository {

    override suspend fun insertNewRecord(record: GasRecord) {
        gasRecordDao.insert(record.toEntity())
    }

    override suspend fun updateRecord(record: GasRecord) {
        gasRecordDao.update(record.toEntity())
    }

    override fun getAll(): Flow<List<GasRecord>> =
        gasRecordDao.getAll()
            .map { items -> items.map { it.toRecord() } }

    override suspend fun getById(id: Int): GasRecord? =
        gasRecordDao.getById(id)?.toRecord()

    override fun getAllByYear(year: String): Flow<List<GasRecord>> =
        gasRecordDao.getAllByYear(year)
            .map { items -> items.map { it.toRecord() } }

    override fun getPaymentByMonth(year: String): Flow<List<FuelMonthlyValue>> =
        gasRecordDao.getPaymentCostByMonth(year)
            .map { items -> items.map { it.toRecord() } }

    override fun getAnnualReport(year: String): Flow<AnnualReport> =
        gasRecordDao.getAnnualReport(year)
            .map { result -> result.toRecord() }
}