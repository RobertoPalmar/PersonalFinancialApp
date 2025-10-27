package com.rpalmar.financialapp.providers.api.apis

import com.rpalmar.financialapp.models.api.BcvDollarApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface BCVApi {

    @GET("rates/")
    suspend fun getBcvDollarExchangeRate():Response<BcvDollarApiResponse>

}