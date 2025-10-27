package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.database.ExchangeRateEntity
import com.rpalmar.financialapp.providers.database.DAOs.ExchangeRateDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateRepository @Inject constructor(
    private val exchangeRateDAO: ExchangeRateDAO
):BaseEntityRepository<ExchangeRateEntity, ExchangeRateDAO>(exchangeRateDAO){

    suspend fun getCurrentExchangeRateByCurrencyID(currencyID:Long): ExchangeRateEntity?{
        return exchangeRateDAO.getCurrentExchangeRatePerCurrencyID(currencyID)
    }
}