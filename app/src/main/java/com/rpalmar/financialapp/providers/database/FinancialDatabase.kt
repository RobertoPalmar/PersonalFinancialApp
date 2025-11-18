package com.rpalmar.financialapp.providers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CategoryEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.models.database.ExchangeRateEntity
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.providers.database.DAOs.AccountDAO
import com.rpalmar.financialapp.providers.database.DAOs.CategoryDAO
import com.rpalmar.financialapp.providers.database.DAOs.CurrencyDAO
import com.rpalmar.financialapp.providers.database.DAOs.EnvelopeDAO
import com.rpalmar.financialapp.providers.database.DAOs.ExchangeRateDAO
import com.rpalmar.financialapp.providers.database.DAOs.TransactionDAO

@Database(
    entities = [
        AccountEntity::class,
        CurrencyEntity::class,
        EnvelopeEntity::class,
        TransactionEntity::class,
        ExchangeRateEntity::class,
        CategoryEntity::class
    ],
    version = 11,
    exportSchema = false
)

@TypeConverters(RoomConverter::class)
abstract class FinancialDatabase: RoomDatabase() {
    abstract fun accountDAO(): AccountDAO
    abstract fun currencyDAO(): CurrencyDAO
    abstract fun envelopeDAO(): EnvelopeDAO
    abstract fun transactionDAO(): TransactionDAO
    abstract fun exchangeRateDAO(): ExchangeRateDAO
    abstract fun categoryDAO(): CategoryDAO
}