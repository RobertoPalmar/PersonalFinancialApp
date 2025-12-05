package com.rpalmar.financialapp.usecases

import android.util.Log
import com.rpalmar.financialapp.providers.database.repositories.CategoryRepository
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import com.rpalmar.financialapp.providers.database.repositories.ExchangeRateRepository
import com.rpalmar.financialapp.providers.database.seeds.categorySeeds
import com.rpalmar.financialapp.providers.database.seeds.currencySeeds
import com.rpalmar.financialapp.providers.database.seeds.exchangeRateSeeds
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetBaseRoomSeed @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): Boolean {
        try {
            //INSERT BASE CURRENCIES AND EXCHANGE
            currencyRepository.insertRange(currencySeeds);
            exchangeRateRepository.insertRange(exchangeRateSeeds)
            categoryRepository.insertRange(categorySeeds)

            return true;
        } catch (ex: Exception) {
            Log.e("SetBaseRoomSeed", ex.message.toString());
            return false;
        }
    }
}