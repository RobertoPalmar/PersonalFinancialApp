package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    val TAG = "CreateCurrencyUseCase"

    suspend operator fun invoke(currency: CurrencyDomain): Boolean {
        try {
            //MAP TO ENTITY
            val newCurrencyEntity = currency.toEntity();

            //SAVE NEW ENTITY
            currencyRepository.insert(newCurrencyEntity);

            Log.i(TAG, "💳 Entity created: $newCurrencyEntity");
            return true;
        }catch (ex: Exception){
            Log.e(TAG, ex.message.toString());
            return false;
        }
    }
}
