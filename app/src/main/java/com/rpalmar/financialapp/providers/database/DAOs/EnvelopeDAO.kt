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
    @Query("SELECT * FROM envelope_table WHERE id = :id AND isDelete = 0")
    fun getEnvelopeWithCurrencyByID(id:Long): EnvelopeWithCurrencyRelation?

    @Transaction
    @Query("SELECT * FROM envelope_table WHERE isDelete = 0")
    fun getEnvelopeListWithCurrency(): Flow<List<EnvelopeWithCurrencyRelation>>

    @Query("SELECT * FROM envelope_table")
    fun getAll():List<EnvelopeEntity>

    @Query("DELETE FROM envelope_table")
    suspend fun deleteAll();

    @Query("UPDATE envelope_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)

    @Query("UPDATE envelope_table SET balance = balance + :amount WHERE id = :envelopeId")
    suspend fun updateBalance(envelopeId: Long, amount: Double)
}