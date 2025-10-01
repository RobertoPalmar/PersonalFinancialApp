package com.rpalmar.financialapp.providers.database.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import com.rpalmar.financialapp.providers.database.DAOs.TransactionDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDAO: TransactionDAO
): BaseEntityRepository<TransactionEntity, TransactionDAO>(transactionDAO) {

    suspend fun getAll():List<TransactionEntity>{
        return transactionDAO.getAll()
    }

    fun getByAccountID(accountID:Long, pageSize:Int = 20):Flow<PagingData<TransactionWithCurrencyRelation>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { transactionDAO.getTransactionsByAccount(accountID)}
        ).flow
    }

    suspend fun deleteAll(){
        transactionDAO.deleteAll()
    }

}