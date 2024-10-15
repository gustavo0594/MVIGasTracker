package com.example.mvigastracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvigastracker.data.db.dao.GasRecordDao
import com.example.mvigastracker.data.db.entities.GasRecordEntity

@Database(entities = [GasRecordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gasRecordDao(): GasRecordDao
}