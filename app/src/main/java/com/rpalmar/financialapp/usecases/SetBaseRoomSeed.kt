package com.rpalmar.financialapp.usecases

import android.util.Log
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import com.rpalmar.financialapp.providers.database.repositories.ExchangeRateRepository
import com.rpalmar.financialapp.providers.database.seeds.currencySeeds
import com.rpalmar.financialapp.providers.database.seeds.exchangeRateSeeds
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetBaseRoomSeed @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) {
    suspend operator fun invoke(): Boolean {
        try {
            //INSERT BASE CURRENCIES AND EXCHANGE
            currencyRepository.insertRange(currencySeeds);
            exchangeRateRepository.insertRange(exchangeRateSeeds)

            return true;
        } catch (ex: Exception) {
            Log.e("SetBaseRoomSeed", ex.message.toString());
            return false;
        }
    }
}