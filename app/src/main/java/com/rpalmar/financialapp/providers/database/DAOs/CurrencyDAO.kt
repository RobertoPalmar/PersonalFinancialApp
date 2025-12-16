package com.rpalmar.financialapp.providers.database.DAOs

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDAO : BaseDao<CurrencyEntity> {
    @Query("SELECT * FROM currency_table WHERE id = :id")
    fun getByID(id: Long): CurrencyEntity?

    @Query("SELECT * FROM currency_table WHERE isDelete = 0")
    fun getAll(): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency_table WHERE isDelete = 0")
    fun getCurrencyListPaginated():PagingSource<Int, CurrencyEntity>

    @Query("SELECT * FROM currency_table WHERE mainCurrency = 1")
    fun getMainCurrency(): CurrencyEntity?

    @Query("SELECT * FROM currency_table WHERE mainCurrency = 1")
    fun getMainCurrencyAsFlow(): Flow<CurrencyEntity?>

    @Query("DELETE FROM currency_table")
    suspend fun deleteAll();

    @Query("UPDATE currency_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)

}