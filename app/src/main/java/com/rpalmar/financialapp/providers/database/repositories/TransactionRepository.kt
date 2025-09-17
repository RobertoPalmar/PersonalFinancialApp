package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.providers.database.DAOs.TransactionDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDAO: TransactionDAO
): BaseEntityRepository<TransactionEntity, TransactionDAO>(transactionDAO) {

    suspend fun getAll():List<TransactionEntity>{
        return transactionDAO.getAll()
    }

    suspend fun deleteAll(){
        transactionDAO.deleteAll()
    }

}