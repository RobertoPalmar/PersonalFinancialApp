package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    val TAG = "UpdateCurrencyUseCase"

    suspend operator fun invoke(currency: CurrencyDomain): Boolean?{
        try {
            //MAP TO ENTITY
            val categoryEntity = currency.toEntity();

            //UPDATE ENTITY
            currencyRepository.update(categoryEntity);

            Log.i(TAG, "💳 Entity updated");
            return true;
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }
}
