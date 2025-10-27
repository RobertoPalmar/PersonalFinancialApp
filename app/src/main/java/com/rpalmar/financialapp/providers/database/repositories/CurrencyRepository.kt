package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.relations.CurrencyWithExchangeRelation
import com.rpalmar.financialapp.providers.database.DAOs.CurrencyDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val currencyDAO: CurrencyDAO
): BaseEntityRepository<CurrencyEntity, CurrencyDAO>(currencyDAO) {

    fun getAll():Flow<List<CurrencyWithExchangeRelation>>{
        return currencyDAO.getAll()
    }

    suspend fun deleteAll(){
        currencyDAO.deleteAll()
    }

    fun getMainCurrency(): CurrencyWithExchangeRelation?{
        return currencyDAO.getMainCurrency()
    }
}