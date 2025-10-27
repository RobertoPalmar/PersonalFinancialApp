package com.rpalmar.financialapp.providers.api.repositories

import android.util.Log
import com.rpalmar.financialapp.models.api.BcvDollarApiResponse
import com.rpalmar.financialapp.models.api.FrankfurterApiResponse
import com.rpalmar.financialapp.providers.api.apis.BCVApi
import com.rpalmar.financialapp.providers.api.apis.FrankfurterApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val bcvApi: BCVApi,
    private val frankfurterApi: FrankfurterApi
){
    private val TAG = "ApiRepository"

    suspend fun getBCVExchangeRate():BcvDollarApiResponse?{
        try{
            //GET EXCHANGE RATE
            val bcvExchangeRate = bcvApi.getBcvDollarExchangeRate();

            //VALIDATE RESPONSE
            if(bcvExchangeRate.isSuccessful)
                return bcvExchangeRate.body()
            else
                return null;
        }catch (ex: Exception){
            Log.e(TAG, "❌ Error getting BCV exchange rate from [BCV API]: ${ex.message}")
            return null;
        }
    }

    suspend fun getGeneralExchangeRate(): FrankfurterApiResponse?{
        try{
            //GET EXCHANGE RATE
            val usdExchangeRate = frankfurterApi.getGeneralUSDExchangeRate();

            //VALIDATE RESPONSE
            if(usdExchangeRate.isSuccessful)
                return usdExchangeRate.body()
            else
                return null;
        }catch (ex: Exception){
            Log.e(TAG, "❌ Error getting General exchange rate from [FRANKFURTER API]: ${ex.message}")
            return null;
        }
    }
}