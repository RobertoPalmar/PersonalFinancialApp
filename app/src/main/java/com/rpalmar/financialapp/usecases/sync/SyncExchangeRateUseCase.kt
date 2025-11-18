package com.rpalmar.financialapp.usecases.sync

import android.util.Log
import com.rpalmar.financialapp.models.ExchangeRateApi
import com.rpalmar.financialapp.models.ExchangeRateType
import com.rpalmar.financialapp.models.database.ExchangeRateEntity
import com.rpalmar.financialapp.providers.api.repositories.ApiRepository
import com.rpalmar.financialapp.providers.database.repositories.ExchangeRateRepository
import com.rpalmar.financialapp.providers.database.repositories.SharedPreferencesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncExchangeRateUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    suspend operator fun invoke(): Boolean {
        if (shouldSkipSync()) {
            Log.i("SyncExchangeRateUseCase", "✅ Sync skipped, last sync was less than an hour ago")
            return true
        }

        try {
            var newExchangeRateList = mutableListOf<ExchangeRateEntity>()

            //GET LAST EXCHANGE RATE FOR BS
            val bcvExchangeRate = apiRepository.getBCVExchangeRate();

            //VALIDATE RESPONSE
            if (bcvExchangeRate == null)
                Log.e("SyncExchangeRateUseCase", "❌ Error getting BCV rate");
            else
                //ADD NEW EXCHANGE RATE
                newExchangeRateList.add(
                    ExchangeRateEntity(
                        rate = bcvExchangeRate.dollar,
                        currencyID = 2, //BS
                        source = ExchangeRateApi.BCV_API.toString(),
                        type = ExchangeRateType.API,
                        createAt = bcvExchangeRate.date
                    )
                )


            //GET LAST EXCHANGE RATE FOR USD
            val usdExchangeRate = apiRepository.getGeneralExchangeRate();

            //VALIDATE RESPONSE
            if (usdExchangeRate == null)
                Log.e("SyncExchangeRateUseCase", "❌ Error getting General rate");
            else
                //ADD NEW EXCHANGE RATE
                newExchangeRateList.add(
                    ExchangeRateEntity(
                        rate = usdExchangeRate.rates["EUR"]!!,
                        currencyID = 3, //EURO
                        source = ExchangeRateApi.FRANKFURTER_API.toString(),
                        type = ExchangeRateType.API,
                        createAt = usdExchangeRate.date
                    )
                )

            //IF THERE ARE NEW RATES, SAVE THIS
            if (newExchangeRateList.size > 0) {
                //INSERT NEW RATES
                Log.i("SyncExchangeRateUseCase", "✅ Saving new exchange rates")
                exchangeRateRepository.insertRange(newExchangeRateList);
                sharedPreferencesRepository.updateLastSyncTimestamp(System.currentTimeMillis())
                return true;
            } else {
                //NOTIFY NOT NEW RATES SAVE
                Log.e("SyncExchangeRateUseCase", "❌ No new exchange rates to save")
                return false;
            }
        } catch (ex: Exception) {
            Log.e("SyncExchangeRateUseCase", ex.message.toString());
            return false;
        }
    }

    private fun shouldSkipSync(): Boolean {
        val lastSyncTimestamp = sharedPreferencesRepository.getLastSyncTimestamp()
        val oneHourInMillis = TimeUnit.HOURS.toMillis(6)
        return System.currentTimeMillis() - lastSyncTimestamp < oneHourInMillis
    }
}