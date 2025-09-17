package com.rpalmar.financialapp.providers.database.DAOs

import androidx.room.Dao
import androidx.room.Query
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity

@Dao
interface CurrencyDAO : BaseDao<CurrencyEntity> {
    @Query("SELECT * FROM currency_table")
    fun getAll():List<CurrencyEntity>

    @Query("DELETE FROM currency_table")
    suspend fun deleteAll();
}