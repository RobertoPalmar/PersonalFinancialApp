package com.rpalmar.financialapp.providers.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    fun create(config: ApiProviderConfig): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(config.apiKey, config.headerName))
            .build()

        return Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}