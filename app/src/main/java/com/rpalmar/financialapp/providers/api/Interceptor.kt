package com.rpalmar.financialapp.providers.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val apiKey: String?,
    private val headerName: String?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        //IF TOKEN IS NULL OR EMPTY, RETURN THE REQUEST
        if (apiKey.isNullOrBlank() || headerName.isNullOrBlank()) {
            return chain.proceed(original)
        }

        //ELSE, ADD THE TOKEN TO THE REQUEST
        val newRequest = original.newBuilder()
            .addHeader(headerName, apiKey)
            .build()

        return chain.proceed(newRequest)
    }
}