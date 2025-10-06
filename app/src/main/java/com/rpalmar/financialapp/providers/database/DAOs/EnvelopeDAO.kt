package com.rpalmar.financialapp.providers.database.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.EnvelopeEntity

@Dao
interface EnvelopeDAO: BaseDao<EnvelopeEntity> {
    @Query("SELECT * FROM envelope_table")
    fun getAll():List<EnvelopeEntity>

    @Query("DELETE FROM envelope_table")
    suspend fun deleteAll();

    @Query("UPDATE envelope_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)
}