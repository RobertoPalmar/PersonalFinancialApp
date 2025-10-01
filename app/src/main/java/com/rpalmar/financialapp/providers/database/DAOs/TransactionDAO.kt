package com.rpalmar.financialapp.providers.database.DAOs

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.database.relations.AccountWithCurrencyRelation
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO: BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transaction_table")
    fun getAll():List<TransactionEntity>

    @Transaction
    @Query("SELECT * FROM transaction_table " +
            "WHERE (originSourceID = :accountID AND originSourceType = 'ACCOUNT') " +
            "OR (destinationSourceID = :accountID AND destinationSourceType = 'ACCOUNT')" +
            "LIMIT :rows OFFSET (:page * :rows)")
    fun getAllTransactionWithCurrencyPerAccount(accountID:Long, page:Int, rows:Int):Flow<List<TransactionWithCurrencyRelation>>

    @Transaction
    @Query("""
        SELECT * 
        FROM transaction_table
        WHERE 
            (originSourceID = :accountID AND originSourceType = 'ACCOUNT') 
            OR (destinationSourceID = :accountID AND destinationSourceType = 'ACCOUNT')
        ORDER BY transactionDate DESC
    """)
    fun getTransactionsByAccount(accountID: Long): PagingSource<Int, TransactionWithCurrencyRelation>

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll();
}