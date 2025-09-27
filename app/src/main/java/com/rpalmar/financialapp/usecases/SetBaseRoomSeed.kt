package com.rpalmar.financialapp.usecases

import android.util.Log
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import com.rpalmar.financialapp.providers.database.seeds.currencySeeds
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetBaseRoomSeed @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(): Boolean {
        try {
            //INSERT BASE CURRENCIES
            currencyRepository.insertRange(currencySeeds);

            return true;
        } catch (ex: Exception) {
            Log.e("SetBaseRoomSeed", ex.message.toString());
            return false;
        }
    }
}