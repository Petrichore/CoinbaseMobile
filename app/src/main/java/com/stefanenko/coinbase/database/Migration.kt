package com.stefanenko.coinbase.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("alter table exchange_rate add base_currency_name TEXT NOT NULL DEFAULT 'USD'")
        }
    }
}