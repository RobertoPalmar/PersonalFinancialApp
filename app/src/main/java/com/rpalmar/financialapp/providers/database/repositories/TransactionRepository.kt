package com.rpalmar.financialapp.providers.database.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import com.rpalmar.financialapp.providers.database.DAOs.TransactionDAO
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDAO: TransactionDAO
): BaseEntityRepository<TransactionEntity, TransactionDAO>(transactionDAO) {

    fun getByID(id:Long): TransactionEntity?{
        return transactionDAO.getByID(id)
    }

    fun getTransactionWithCurrencyByID(id: Long): TransactionWithCurrencyRelation? {
        return transactionDAO.getTransactionWithCurrencyByID(id)
    }

    fun getTransactionWithCurrencyByDateRange(startDate: Date, endDate:Date):List<TransactionWithCurrencyRelation>{
        return transactionDAO.getTransactionWithCurrencyByDateRange(startDate, endDate);
    }

    fun getTransactionWithCurrencyByDateRangeAndAccountID(startDate: Date, endDate:Date, accountID:Long):List<TransactionWithCurrencyRelation>{
        return transactionDAO.getTransactionWithCurrencyByDateRangeAndAccountID(startDate, endDate, accountID);
    }

    fun getAll():List<TransactionEntity>{
        return transactionDAO.getAll()
    }

    fun getByAccountIDPaginated(accountID:Long, pageSize:Int = 20):Flow<PagingData<TransactionWithCurrencyRelation>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { transactionDAO.getTransactionsByAccountPaginated(accountID)}
        ).flow
    }

    fun getLastPaginated(pageSize:Int = 20): Flow<PagingData<TransactionWithCurrencyRelation>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { transactionDAO.getLastTransactionsPaginated()}
        ).flow
    }

    suspend fun deleteAll(){
        transactionDAO.deleteAll()
    }

}