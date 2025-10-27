package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrenciesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(): Flow<List<CurrencyDomain>>? {
        try {
            //GET CURRENCIES
            val currencies = currencyRepository.getAll();

            //MAP TO DOMAIN
            val currencyDomainList = currencies.map { list -> list.map { it.toDomain() } };

            //RETURN DATA
            Log.i("GetCurrenciesUseCase", "💳 Currencies obtain: ${currencyDomainList.map { it.toString() }}")
            return currencyDomainList;
        } catch (ex: Exception) {
            Log.e("GetCurrenciesUseCase", ex.message.toString());
            return null;
        }
    }
}