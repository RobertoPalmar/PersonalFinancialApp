package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMainCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(): CurrencyDomain? {
        try {
            //GET BASE CURRENCY
            val mainCurrency = currencyRepository.getMainCurrency();

            //VALIDATE CURRENCY
            if(mainCurrency == null){
                Log.e("GetMainCurrencyUseCase", "No se encontró moneda base")
                return null
            }

            //MAP TO DOMAIN
            val currencyDomain = mainCurrency.toDomain();

            //RETURN DATA
            Log.i("GetMainCurrencyUseCase", "💳 Entity retrieved: $currencyDomain")
            return currencyDomain;
        } catch (ex: Exception) {
            Log.e("GetMainCurrencyUseCase", ex.message.toString());
            return null;
        }
    }
}