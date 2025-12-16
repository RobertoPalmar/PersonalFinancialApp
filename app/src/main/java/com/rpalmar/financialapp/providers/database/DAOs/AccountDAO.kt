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

    @Query("SELECT COUNT(*) FROM accounts_table WHERE currencyId = :currencyID")
    fun getAccountsCountByCurrency(currencyID:Long):Int

    @Transaction
    @Query(
        """
            SELECT 
                account.*, 
                currency.id AS currency_id, 
                currency.name AS currency_name, 
                currency.ISO AS currency_ISO, 
                currency.symbol AS currency_symbol, 
                currency.mainCurrency AS currency_mainCurrency,
                currency.currentExchangeRate AS currency_currentExchangeRate,
                currency.isDelete AS currency_isDelete
            FROM accounts_table AS account
            INNER JOIN currency_table AS currency ON account.currencyId = currency.id
            WHERE account.id = :accountID
        """
    )
    fun getAccountWithCurrencyByID(accountID: Long): AccountWithCurrencyRelation?


    @Transaction
    @Query(
        """
            SELECT 
                account.*, 
                currency.id AS currency_id, 
                currency.name AS currency_name, 
                currency.ISO AS currency_ISO, 
                currency.symbol AS currency_symbol, 
                currency.mainCurrency AS currency_mainCurrency,
                currency.currentExchangeRate AS currency_currentExchangeRate,
                currency.isDelete AS currency_isDelete
            FROM accounts_table AS account
            INNER JOIN currency_table AS currency ON account.currencyId = currency.id
            WHERE account.isDelete = 0
        """
    )
    fun getAccountListWithCurrency(): Flow<List<AccountWithCurrencyRelation>>


    @Transaction
    @Query(
        """
            SELECT 
                account.*, 
                currency.id AS currency_id, 
                currency.name AS currency_name, 
                currency.ISO AS currency_ISO, 
                currency.symbol AS currency_symbol, 
                currency.mainCurrency AS currency_mainCurrency,
                currency.currentExchangeRate AS currency_currentExchangeRate,
                currency.isDelete AS currency_isDelete
            FROM accounts_table AS account
            INNER JOIN currency_table AS currency ON account.currencyId = currency.id
        """
    )
    fun getAccountListWithCurrencyWithDelete(): Flow<List<AccountWithCurrencyRelation>>


    @Transaction
    @Query(
        """
            SELECT 
                account.*, 
                currency.id AS currency_id, 
                currency.name AS currency_name, 
                currency.ISO AS currency_ISO, 
                currency.symbol AS currency_symbol, 
                currency.mainCurrency AS currency_mainCurrency,
                currency.currentExchangeRate AS currency_currentExchangeRate,
                currency.isDelete AS currency_isDelete
            FROM accounts_table AS account
            INNER JOIN currency_table AS currency ON account.currencyId = currency.id
            WHERE account.isDelete = 0
        """
    )
    fun getAccountListPaginated(): PagingSource<Int, AccountWithCurrencyRelation>

    @Query("DELETE FROM accounts_table")
    suspend fun deleteAll();

    @Query("UPDATE accounts_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)

    @Query("UPDATE accounts_table SET balance = balance + :amount WHERE id = :accountId")
    suspend fun updateBalance(accountId: Long, amount: Double)
}