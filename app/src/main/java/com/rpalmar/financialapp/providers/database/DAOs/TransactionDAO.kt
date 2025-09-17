package com.rpalmar.financialapp.providers.database.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.TransactionEntity

@Dao
interface TransactionDAO: BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transaction_table")
    fun getAll():List<TransactionEntity>

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll();
}