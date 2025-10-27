package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.RateMode
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.interfaces.IDomain

data class CurrencyDomain(
    val id: Long = 0,
    val name:String,
    val ISO:String,
    val symbol:String,
    val mainCurrency: Boolean,
    val exchangeRate: Double,
    val rateMode: RateMode
): IDomain {
    override fun toEntity(): CurrencyEntity{
        return TODO("Provide the return value")
    }
}