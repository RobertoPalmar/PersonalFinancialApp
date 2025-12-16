package com.rpalmar.financialapp.providers.database.DAOs

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDAO: BaseDao<TransactionEntity> {
    @Query("SELECT * FROM transaction_table WHERE id = :id")
    fun getByID(id: Long): TransactionEntity?

    @Query("SELECT * FROM transaction_table")
    fun getAll():List<TransactionEntity>

    @Transaction
    @Query(
        """
            SELECT 
                t.*,
                c.id AS currency_id,
                c.name AS currency_name,
                c.ISO AS currency_ISO,
                c.symbol AS currency_symbol,
                c.mainCurrency AS currency_mainCurrency,
                c.currentExchangeRate AS currency_currentExchangeRate,
                c.isDelete AS currency_isDelete,
                cat.id AS category_id,
                cat.type AS category_type,
                cat.name AS category_name,
                cat.style AS category_style,
                cat.isDelete AS category_isDelete,
                cat.isBaseCategory AS category_isBaseCategory
            FROM transaction_table AS t
            INNER JOIN currency_table AS c ON t.currencyId = c.id
            LEFT JOIN category_table AS cat ON t.categoryID = cat.id
            WHERE t.id = :id
        """
    )
    fun getTransactionWithCurrencyByID(id: Long): TransactionWithCurrencyRelation?

    @Transaction
    @Query(
        """
            SELECT 
                t.*,
                c.id AS currency_id,
                c.name AS currency_name,
                c.ISO AS currency_ISO,
                c.symbol AS currency_symbol,
                c.mainCurrency AS currency_mainCurrency,
                c.currentExchangeRate AS currency_currentExchangeRate,
                c.isDelete AS currency_isDelete,
                cat.id AS category_id,
                cat.type AS category_type,
                cat.name AS category_name,
                cat.style AS category_style,
                cat.isDelete AS category_isDelete,
                cat.isBaseCategory AS category_isBaseCategory
            FROM transaction_table AS t
            INNER JOIN currency_table AS c ON t.currencyId = c.id
            LEFT JOIN category_table AS cat ON t.categoryID = cat.id
            WHERE t.transactionDate BETWEEN :startDate AND :endDate 
            AND t.isDelete = 0
        """
    )
    fun getTransactionWithCurrencyByDateRange(startDate:Date, endDate:Date):List<TransactionWithCurrencyRelation>

    @Transaction
    @Query(
        """
            SELECT 
                t.*,
                c.id AS currency_id,
                c.name AS currency_name,
                c.ISO AS currency_ISO,
                c.symbol AS currency_symbol,
                c.mainCurrency AS currency_mainCurrency,
                c.currentExchangeRate AS currency_currentExchangeRate,
                c.isDelete AS currency_isDelete,
                cat.id AS category_id,
                cat.type AS category_type,
                cat.name AS category_name,
                cat.style AS category_style,
                cat.isDelete AS category_isDelete,
                cat.isBaseCategory AS category_isBaseCategory
            FROM transaction_table AS t
            INNER JOIN currency_table AS c ON t.currencyId = c.id
            LEFT JOIN category_table AS cat ON t.categoryID = cat.id
            WHERE t.transactionDate BETWEEN :startDate AND :endDate AND t.sourceID = :accountID 
            AND t.isDelete = 0
        """
    )
    fun getTransactionWithCurrencyByDateRangeAndAccountID(startDate:Date, endDate:Date, accountID:Long):List<TransactionWithCurrencyRelation>

    @Transaction
    @Query(
        """
            SELECT 
                t.*,
                c.id AS currency_id,
                c.name AS currency_name,
                c.ISO AS currency_ISO,
                c.symbol AS currency_symbol,
                c.mainCurrency AS currency_mainCurrency,
                c.currentExchangeRate AS currency_currentExchangeRate,
                c.isDelete AS currency_isDelete,
                cat.id AS category_id,
                cat.type AS category_type,
                cat.name AS category_name,
                cat.style AS category_style,
                cat.isDelete AS category_isDelete,
                cat.isBaseCategory AS category_isBaseCategory
            FROM transaction_table AS t
            INNER JOIN currency_table AS c ON t.currencyId = c.id
            LEFT JOIN category_table AS cat ON t.categoryID = cat.id
            WHERE t.sourceID = :accountID AND t.sourceType = 'ACCOUNT' 
            AND t.isDelete = 0
            LIMIT :rows OFFSET (:page * :rows)
        """
    )
    fun getTransactionsByAccountPaginatedFlow(
        accountID: Long,
        page: Int,
        rows: Int
    ): Flow<List<TransactionWithCurrencyRelation>>

    @Transaction
    @Query(
        """
            SELECT 
                t.*,
                c.id AS currency_id,
                c.name AS currency_name,
                c.ISO AS currency_ISO,
                c.symbol AS currency_symbol,
                c.mainCurrency AS currency_mainCurrency,
                c.currentExchangeRate AS currency_currentExchangeRate,
                c.isDelete AS currency_isDelete,
                cat.id AS category_id,
                cat.type AS category_type,
                cat.name AS category_name,
                cat.style AS category_style,
                cat.isDelete AS category_isDelete,
                cat.isBaseCategory AS category_isBaseCategory
            FROM transaction_table AS t
            INNER JOIN currency_table AS c ON t.currencyId = c.id
            LEFT JOIN category_table AS cat ON t.categoryID = cat.id
            WHERE t.sourceID = :accountID AND t.sourceType = 'ACCOUNT' 
            AND t.isDelete = 0
            ORDER BY t.transactionDate DESC
        """
    )
    fun getTransactionsByAccountPaginated(
        accountID: Long
    ): PagingSource<Int, TransactionWithCurrencyRelation>

    @Transaction
    @Query(
        """
            SELECT 
                t.*,
                c.id AS currency_id,
                c.name AS currency_name,
                c.ISO AS currency_ISO,
                c.symbol AS currency_symbol,
                c.mainCurrency AS currency_mainCurrency,
                c.currentExchangeRate AS currency_currentExchangeRate,
                c.isDelete AS currency_isDelete,
                cat.id AS category_id,
                cat.type AS category_type,
                cat.name AS category_name,
                cat.style AS category_style,
                cat.isDelete AS category_isDelete,
                cat.isBaseCategory AS category_isBaseCategory
            FROM transaction_table AS t
            INNER JOIN currency_table AS c ON t.currencyId = c.id
            LEFT JOIN category_table AS cat ON t.categoryID = cat.id
            INNER JOIN accounts_table AS a ON t.sourceID = a.id
            WHERE a.isDelete = 0
            AND t.isDelete = 0
            ORDER BY t.transactionDate DESC
        """
    )
    fun getLastTransactionsPaginated(): PagingSource<Int, TransactionWithCurrencyRelation>

    @Transaction
    @Query(
        """
            SELECT 
                t.*,
                c.id AS currency_id,
                c.name AS currency_name,
                c.ISO AS currency_ISO,
                c.symbol AS currency_symbol,
                c.mainCurrency AS currency_mainCurrency,
                c.currentExchangeRate AS currency_currentExchangeRate,
                c.isDelete AS currency_isDelete,
                cat.id AS category_id,
                cat.type AS category_type,
                cat.name AS category_name,
                cat.style AS category_style,
                cat.isDelete AS category_isDelete,
                cat.isBaseCategory AS category_isBaseCategory
            FROM transaction_table AS t
            INNER JOIN currency_table AS c ON t.currencyId = c.id
            LEFT JOIN category_table AS cat ON t.categoryID = cat.id
            WHERE t.sourceID = :envelopeID AND t.sourceType = 'ENVELOPE'
            AND t.isDelete = 0
            ORDER BY t.transactionDate DESC
        """
    )
    fun getTransactionsByEnvelopePaginated(
        envelopeID: Long
    ): PagingSource<Int, TransactionWithCurrencyRelation>

    @Query("DELETE FROM transaction_table")
    suspend fun deleteAll();

    @Query("UPDATE transaction_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)
}