package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCurrencyByIDUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    val TAG = "GetCurrencyByIDUseCase"

    operator fun invoke(currencyID: Long): CurrencyDomain? {
        try {
            //GET CURRENCY
            val currencyEntity = currencyRepository.getByID(currencyID);

            //VALIDATE CURRENCY
            if(currencyEntity == null){
                Log.e(TAG, "Error al obtener la currency")
                return null;
            }

            //MAP TO DOMAIN
            val currencyDomain = currencyEntity.toDomain()

            //RETURN DATA
            Log.i(TAG, "💳 Currency Obtain: $currencyDomain")
            return currencyDomain;
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }
}
