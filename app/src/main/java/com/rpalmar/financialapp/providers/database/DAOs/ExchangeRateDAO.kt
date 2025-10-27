package com.rpalmar.financialapp.providers.database.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.rpalmar.financialapp.models.database.ExchangeRateEntity

@Dao
interface ExchangeRateDAO: BaseDao<ExchangeRateEntity> {

    @Query("""
        SELECT e.*
        FROM exchange_rate_table e
        INNER JOIN (
            SELECT currencyId, MAX(createAt) AS maxDate
            FROM exchange_rate_table
            GROUP BY currencyId
        ) latest
        ON e.currencyId = latest.currencyId AND e.createAt = latest.maxDate
    """)
    suspend fun getLastExchangeRateList():List<ExchangeRateEntity>

    @Query("SELECT * FROM exchange_rate_table WHERE currencyId = :currencyID ORDER BY createAt DESC LIMIT 1")
    suspend fun getCurrentExchangeRatePerCurrencyID(currencyID:Long):ExchangeRateEntity?
}