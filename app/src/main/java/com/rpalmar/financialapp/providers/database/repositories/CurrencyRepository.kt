package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.providers.database.DAOs.CurrencyDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val currencyDAO: CurrencyDAO
): BaseEntityRepository<CurrencyEntity, CurrencyDAO>(currencyDAO) {

    suspend fun getAll():List<CurrencyEntity>{
        return currencyDAO.getAll()
    }

    suspend fun deleteAll(){
        currencyDAO.deleteAll()
    }
}