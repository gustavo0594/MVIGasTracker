package com.example.mvigastracker.di

import android.app.Application
import androidx.room.Room
import com.example.mvigastracker.data.db.AppDatabase
import com.example.mvigastracker.data.db.GasRecordCallback
import com.example.mvigastracker.data.db.dao.GasRecordDao
import com.example.mvigastracker.data.repositories.GasRecordRepositoryImp
import com.example.mvigastracker.domain.repositories.GasRecordRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module(includes = [RepositoryModule::class])
class DataModule {

    @Provides
    @Singleton
    fun providesAppDataBase(application: Application): AppDatabase =
        Room.databaseBuilder(
            context = application.applicationContext,
            klass = AppDatabase::class.java,
            name = "gas_database"
        )
            .addCallback(GasRecordCallback(application))
            .build()


    @Provides
    @Singleton
    fun providesGasRecordDao(appDatabase: AppDatabase): GasRecordDao =
        appDatabase.gasRecordDao()


}

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsGasRepository(repositoryImp: GasRecordRepositoryImp): GasRecordRepository
}