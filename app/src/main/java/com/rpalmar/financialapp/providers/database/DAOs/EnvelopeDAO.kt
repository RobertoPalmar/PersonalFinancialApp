package com.rpalmar.financialapp.providers.database.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.rpalmar.financialapp.models.database.EnvelopeEntity

import androidx.room.Transaction
import com.rpalmar.financialapp.models.database.relations.EnvelopeWithCurrencyRelation

import kotlinx.coroutines.flow.Flow

@Dao
interface EnvelopeDAO: BaseDao<EnvelopeEntity> {
    @Query("SELECT * FROM envelope_table WHERE id = :id")
    fun getByID(id: Long): EnvelopeEntity?

    @Transaction
    @Query(
        """
            SELECT 
                envelope.*,
                currency.id AS currency_id,
                currency.name AS currency_name,
                currency.ISO AS currency_ISO,
                currency.symbol AS currency_symbol,
                currency.mainCurrency AS currency_mainCurrency,
                currency.rateMode AS currency_rateMode,
                exchange.rate AS exchangeRate
            FROM envelope_table AS envelope
            INNER JOIN currency_table AS currency ON envelope.currencyId = currency.id
            LEFT JOIN (
                SELECT er1.*
                FROM exchange_rate_table AS er1
                INNER JOIN (
                    SELECT currencyId, MAX(createAt) AS maxDate
                    FROM exchange_rate_table
                    GROUP BY currencyId
                ) AS latest
                ON er1.currencyId = latest.currencyId AND er1.createAt = latest.maxDate
            ) AS exchange ON exchange.currencyId = currency.id
            WHERE envelope.id = :id AND envelope.isDelete = 0
        """
    )
    fun getEnvelopeWithCurrencyByID(id: Long): EnvelopeWithCurrencyRelation?

    @Transaction
    @Query(
        """
            SELECT 
                envelope.*,
                currency.id AS currency_id,
                currency.name AS currency_name,
                currency.ISO AS currency_ISO,
                currency.symbol AS currency_symbol,
                currency.mainCurrency AS currency_mainCurrency,
                currency.rateMode AS currency_rateMode,
                exchange.rate AS exchangeRate
            FROM envelope_table AS envelope
            INNER JOIN currency_table AS currency ON envelope.currencyId = currency.id
            LEFT JOIN (
                SELECT er1.*
                FROM exchange_rate_table AS er1
                INNER JOIN (
                    SELECT currencyId, MAX(createAt) AS maxDate
                    FROM exchange_rate_table
                    GROUP BY currencyId
                ) AS latest
                ON er1.currencyId = latest.currencyId AND er1.createAt = latest.maxDate
            ) AS exchange ON exchange.currencyId = currency.id
            WHERE envelope.isDelete = 0
        """
    )
    fun getEnvelopeListWithCurrency(): Flow<List<EnvelopeWithCurrencyRelation>>

    @Transaction
    @Query(
        """
            SELECT 
                envelope.*,
                currency.id AS currency_id,
                currency.name AS currency_name,
                currency.ISO AS currency_ISO,
                currency.symbol AS currency_symbol,
                currency.mainCurrency AS currency_mainCurrency,
                currency.rateMode AS currency_rateMode,
                exchange.rate AS exchangeRate
            FROM envelope_table AS envelope
            INNER JOIN currency_table AS currency ON envelope.currencyId = currency.id
            LEFT JOIN (
                SELECT er1.*
                FROM exchange_rate_table AS er1
                INNER JOIN (
                    SELECT currencyId, MAX(createAt) AS maxDate
                    FROM exchange_rate_table
                    GROUP BY currencyId
                ) AS latest
                ON er1.currencyId = latest.currencyId AND er1.createAt = latest.maxDate
            ) AS exchange ON exchange.currencyId = currency.id
        """
    )
    fun getEnvelopeListWithCurrencyWithDelete(): Flow<List<EnvelopeWithCurrencyRelation>>

    @Query("SELECT * FROM envelope_table")
    fun getAll():List<EnvelopeEntity>

    @Query("DELETE FROM envelope_table")
    suspend fun deleteAll();

    @Query("UPDATE envelope_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)

    @Query("UPDATE envelope_table SET balance = balance + :amount WHERE id = :envelopeId")
    suspend fun updateBalance(envelopeId: Long, amount: Double)
}