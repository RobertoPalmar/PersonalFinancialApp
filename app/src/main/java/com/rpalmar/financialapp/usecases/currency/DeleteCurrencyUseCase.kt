package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    suspend operator fun invoke(currencyID: Long): Boolean? {
        try {
            val currencyToDelete = currencyRepository.getByID(currencyID)

            //VALIDATE CURRENCY EXIST
            if(currencyToDelete == null){
                Log.i("DeleteCurrencyUseCase", "Entity not found");
                return false;
            }

            //VALIDATE IS MAIN CURRENCY
            //UNSET MAIN CURRENCY
            val isMainCurrency = currencyToDelete.mainCurrency
            if(isMainCurrency)
                currencyRepository.update(currencyToDelete.copy(mainCurrency = false))

            //DELETE ENTITY
            currencyRepository.softDelete(currencyToDelete.id);

            //VALIDATE IS MAIN CURRENCY
            //SET ANOTHER DEFAULT MAIN CURRENCY
            if(isMainCurrency){
                var newMainCurrency = currencyRepository.getAll().first().first()
                currencyRepository.update(newMainCurrency.copy(mainCurrency = true))
            }

            Log.i("DeleteCurrencyUseCase", "💳 Entity deleted")
            return true;
        } catch (ex: Exception) {
            Log.e("DeleteCurrencyUseCase", ex.message.toString());
            return null;
        }
    }
}