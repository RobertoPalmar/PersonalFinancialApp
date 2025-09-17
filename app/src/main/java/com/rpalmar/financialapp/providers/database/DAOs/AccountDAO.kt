package com.rpalmar.financialapp.providers.database.DAOs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.relations.AccountWithCurrencyRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDAO: BaseDao<AccountEntity> {
    @Query("SELECT * FROM accounts_table WHERE id = :id")
    fun getByID(id: Long): AccountEntity?

    @Query("SELECT * FROM accounts_table")
    fun getAll(): List<AccountEntity>

    @Transaction
    @Query("SELECT * FROM accounts_table")
    fun getAllAccountsWithCurrency():Flow<List<AccountWithCurrencyRelation>>

    @Query("DELETE FROM accounts_table")
    suspend fun deleteAll();
}