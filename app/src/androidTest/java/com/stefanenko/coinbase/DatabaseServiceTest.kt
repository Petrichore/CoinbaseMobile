package com.stefanenko.coinbase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.stefanenko.coinbase.data.database.dao.CurrencyDao
import com.stefanenko.coinbase.database.Database
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DatabaseServiceTest {

    private lateinit var currencyDao: CurrencyDao
    private lateinit var db: Database

    @Before
    fun setUpDatabase(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Database::class.java).build()
        currencyDao = db.getCurrencyDao()
    }

    @After
    fun cleanUpResources(){
        db.close()
    }

    @Test
    fun addExchangeRateTest(){
       // currencyDao.addCurrencyExchangeRateToFavorite(mockk())
    }
}