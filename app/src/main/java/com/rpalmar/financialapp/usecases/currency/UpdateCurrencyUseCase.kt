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
            val currencyEntity = currency.toEntity();

            //VALIDATE IF IS SET MAIN CURRENCY
            if(currencyEntity.mainCurrency){
                val currentMainCurrency = currencyRepository.getMainCurrency()

                //IF CURRENT MAIN CURRENCY IF FOUND AND IF OTHER, UPDATE IT TO FALSE
                if(currentMainCurrency!!.id != currencyEntity.id)
                    currencyRepository.update(currentMainCurrency.copy(mainCurrency = false))
            }

            //UPDATE ENTITY
            currencyRepository.update(currencyEntity);

            Log.i(TAG, "💳 Entity updated");
            return true;
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }
}
