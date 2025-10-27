package com.rpalmar.financialapp.providers.api.apis

import com.rpalmar.financialapp.models.api.FrankfurterApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface FrankfurterApi {

    @GET("latest?base=USD")
    suspend fun getGeneralUSDExchangeRate():Response<FrankfurterApiResponse>
}