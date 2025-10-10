package com.rpalmar.financialapp.providers.database.DAOs

import androidx.paging.PagingSource
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

    @Query("SELECT * FROM accounts_table WHERE isDelete = 0")
    fun getAll(): List<AccountEntity>

    @Transaction
    @Query("SELECT * FROM accounts_table WHERE id = :id AND isDelete = 0")
    fun getAccountWithCurrencyByID(id:Long): AccountWithCurrencyRelation?

    @Transaction
    @Query("SELECT * FROM accounts_table WHERE isDelete = 0")
    fun getAccountListWithCurrency():Flow<List<AccountWithCurrencyRelation>>

    @Transaction
    @Query(" SELECT * FROM accounts_table WHERE isDelete = 0")
    fun getAccountListPaginated(): PagingSource<Int, AccountWithCurrencyRelation>

    @Query("DELETE FROM accounts_table")
    suspend fun deleteAll();

    @Query("UPDATE accounts_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)

    @Query("UPDATE accounts_table SET balance = balance + :amount WHERE id = :accountId")
    suspend fun updateBalance(accountId: Long, amount: Double)
}