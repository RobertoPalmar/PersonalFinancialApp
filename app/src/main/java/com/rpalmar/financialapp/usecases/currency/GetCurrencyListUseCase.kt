package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrencyListUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    operator fun invoke(): Flow<List<CurrencyDomain>>? {
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

    fun getPaginated(pageSize: Int = 20): Flow<PagingData<CurrencyDomain>> {
        return flow {
            val currencyFlow = currencyRepository.getPaginated(pageSize)
                .map { pagingData ->
                    pagingData.map { currency ->
                        //MAP TO DOMAIN
                        currency.toDomain()
                    }
                }

            //RETURN FLOW WITH DATA
            emitAll(currencyFlow)
        }
    }
}