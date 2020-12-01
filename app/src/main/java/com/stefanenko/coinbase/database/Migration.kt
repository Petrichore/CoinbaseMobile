package com.stefanenko.coinbase.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("alter table exchange_rate add base_currency_name TEXT NOT NULL DEFAULT 'USD'")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "create table exchange_rate_backup (id_currency Integer NOT NULL primary key, name TEXT NOT NULL, base_currency_name TEXT NOT NULL, exchange_rate Double NOT NULL, addDate  TEXT NOT NULL, addTime TEXT NOT NULL)"
            )
            database.execSQL("insert into exchange_rate_backup select id_currency, name, base_currency_name, exchange_rate, add_date, add_time from exchange_rate")
            database.execSQL("drop table exchange_rate")

            database.execSQL(
                "create table exchange_rate(name TEXT NOT NULL primary key, base_currency_name TEXT NOT NULL, exchange_rate Double NOT NULL, addDate  TEXT NOT NULL, addTime TEXT NOT NULL)"
            )
            database.execSQL("create table fav_exchange_rates (name TEXT NOT NULL primary key)")

            database.execSQL("insert into exchange_rate select name, base_currency_name, exchange_rate, add_date, add_time from exchange_rate_backup")
            database.execSQL("insert into fav_exchange_rates select name from exchange_rate_backup")

            database.execSQL("drop table exchange_rate_backup")
        }
    }
}