package com.stefanenko.coinbase.di

import android.app.Application
import androidx.room.Room
import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.database.Database
import com.stefanenko.coinbase.database.Migration
import com.stefanenko.coinbase.database.Migration.MIGRATION_2_3
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(context: Application) {

    private val database = Room.databaseBuilder(context, Database::class.java, "Coinbase")
        .addMigrations(MIGRATION_2_3)
        .addMigrations(Migration.MIGRATION_1_2)
        .build()

    @Provides
    fun provideCurrencyDao(): CurrencyDao {
        return database.getCurrencyDao()
    }
}