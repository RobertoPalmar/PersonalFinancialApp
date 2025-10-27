package com.rpalmar.financialapp.providers.database.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.relations.CurrencyWithExchangeRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDAO : BaseDao<CurrencyEntity> {
    @Query(
        """
            SELECT 
                c.*,
                er.rate AS exchangeRate
            FROM currency_table AS c
            LEFT JOIN (
                SELECT er1.*
                FROM exchange_rate_table AS er1
                INNER JOIN (
                    SELECT currencyId, MAX(createAt) AS maxDate
                    FROM exchange_rate_table
                    GROUP BY currencyId
                ) AS latest
                ON er1.currencyId = latest.currencyId AND er1.createAt = latest.maxDate
            ) AS er ON er.currencyId = c.id
        """
    )
    fun getAll():Flow<List<CurrencyWithExchangeRelation>>

    @Query(
        """
            SELECT 
                c.*,
                er.rate AS exchangeRate
            FROM currency_table AS c
            LEFT JOIN (
                SELECT er1.*
                FROM exchange_rate_table AS er1
                INNER JOIN (
                    SELECT currencyId, MAX(createAt) AS maxDate
                    FROM exchange_rate_table
                    GROUP BY currencyId
                ) AS latest
                ON er1.currencyId = latest.currencyId AND er1.createAt = latest.maxDate
            ) AS er ON er.currencyId = c.id
            WHERE c.mainCurrency = 1
            LIMIT 1
        """
    )
    fun getMainCurrency(): CurrencyWithExchangeRelation?

    @Query("DELETE FROM currency_table")
    suspend fun deleteAll();

}