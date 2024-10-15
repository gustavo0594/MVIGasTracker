package com.example.mvigastracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mvigastracker.data.db.entities.GasRecordEntity
import com.example.mvigastracker.data.db.entities.MonthlyValuesEntity
import com.example.mvigastracker.data.db.entities.YearlyReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GasRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordEntity: GasRecordEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(recordEntity: GasRecordEntity)

    @Query("SELECT * FROM gas_record ORDER BY kilometers ASC")
    fun getAll(): Flow<List<GasRecordEntity>>

    @Query("SELECT * FROM gas_record WHERE id=:id")
    suspend fun getById(id: Int): GasRecordEntity?

    @Query("SELECT * FROM gas_record WHERE strftime('%Y', recordDate) = :year ORDER BY kilometers ASC")
    fun getAllByYear(year: String): Flow<List<GasRecordEntity>>

    @Query(
        """
SELECT strftime('%Y-%m', recordDate) AS month, SUM(totalPayment) AS payment, (MAX(kilometers) - MIN(kilometers)) as kilometers
FROM gas_record
WHERE strftime('%Y', recordDate) = :year
GROUP BY month
ORDER BY month ASC;
    """
    )
    fun getPaymentCostByMonth(year: String): Flow<List<MonthlyValuesEntity>>

    @Query(
        """
WITH DistanceDifference AS(
      SELECT  (f1.kilometers - (SELECT f2.kilometers
                              FROM gas_record f2
                              WHERE f2.kilometers < f1.kilometers AND strftime('%Y', recordDate) = :year
                              ORDER BY f2.kilometers DESC
                              LIMIT 1)) AS kilometersDifference
        FROM gas_record f1
        WHERE strftime('%Y', recordDate) = :year
        ORDER BY f1.kilometers DESC
        LIMIT 1
)

Select (MAX(kilometers) - MIN(kilometers))  as totalKilometers, SUM(totalPayment) as totalPayment, (SUM(totalPayment)/(MAX(kilometers) - MIN(kilometers))) as paymentPerKm, (Select * from DistanceDifference ) AS distanceDifference
FROM gas_record
WHERE strftime('%Y', recordDate) = :year
    """
    )
    fun getYearlyReport(year: String): Flow<YearlyReportEntity>

}
