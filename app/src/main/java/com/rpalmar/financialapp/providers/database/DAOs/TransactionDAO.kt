package com.rpalmar.financialapp.providers.database.DAOs

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO: BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transaction_table")
    fun getAll():List<TransactionEntity>

    @Transaction
    @Query("SELECT * FROM transaction_table " +
            "WHERE sourceID = :accountID AND sourceType = 'ACCOUNT'" +
            "LIMIT :rows OFFSET (:page * :rows)")
    fun getTransactionsByAccountPaginatedFlow(accountID:Long, page:Int, rows:Int):Flow<List<TransactionWithCurrencyRelation>>

    @Transaction
    @Query("""
        SELECT * 
        FROM transaction_table
        WHERE sourceID = :accountID AND sourceType = 'ACCOUNT' 
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsByAccountPaginated(accountID: Long): PagingSource<Int, TransactionWithCurrencyRelation>

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll();

    @Query("UPDATE transaction_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)
}