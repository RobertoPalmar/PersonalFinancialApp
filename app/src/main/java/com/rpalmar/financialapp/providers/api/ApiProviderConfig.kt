package com.rpalmar.financialapp.providers.api

import com.rpalmar.financialapp.models.ExchangeRateApi

data class ApiProviderConfig(
    val name:String,
    val baseUrl: String,
    val apiKey: String? = null,
    val headerName: String? = null  // Ej: "apikey", "Authorization"
){
    companion object{
        val apiConfigMap: Map<ExchangeRateApi, ApiProviderConfig> = mapOf(
            ExchangeRateApi.BCV_API to ApiProviderConfig(
                name = "BCV API",
                baseUrl = "https://bcv-api.rafnixg.dev/",
                apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkZXZwYWxtYXJAZ21haWwuY29tIiwiZXhwIjoxNzYwMjc5ODA0fQ.tqB6CL4tY47DfCeBEmD7t86bZqtEo4qkj4obRFuP3HA",
                headerName = "Authorization"
            ),
            ExchangeRateApi.FRANKFURTER_API to ApiProviderConfig(
                name = "FRANKFURTER API",
                baseUrl = "https://api.frankfurter.dev/v1/",
                apiKey = null,
                headerName = null
            )
        )
    }
}


