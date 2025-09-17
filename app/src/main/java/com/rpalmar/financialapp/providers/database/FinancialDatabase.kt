package com.rpalmar.financialapp.providers.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.providers.database.DAOs.AccountDAO
import com.rpalmar.financialapp.providers.database.DAOs.CurrencyDAO
import com.rpalmar.financialapp.providers.database.DAOs.EnvelopeDAO
import com.rpalmar.financialapp.providers.database.DAOs.TransactionDAO

@Database(
    entities = [
        AccountEntity::class,
        CurrencyEntity::class,
        EnvelopeEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)


@TypeConverters(RoomConverter::class)
abstract class FinancialDatabase: RoomDatabase() {
    abstract fun accountDAO(): AccountDAO
    abstract fun currencyDAO(): CurrencyDAO
    abstract fun envelopeDAO(): EnvelopeDAO
    abstract fun transactionDAO(): TransactionDAO
}