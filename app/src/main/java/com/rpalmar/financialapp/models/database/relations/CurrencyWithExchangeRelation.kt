package com.rpalmar.financialapp.models.database.relations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.domain.CurrencyDomain

data class CurrencyWithExchangeRelation(
    @Embedded val currency: CurrencyEntity,
    @ColumnInfo(name = "exchangeRate") val exchangeRate:Double
){
    fun toDomain(): CurrencyDomain{
        return CurrencyDomain(
            id = currency.id,
            name = currency.name,
            ISO = currency.ISO,
            symbol = currency.symbol,
            mainCurrency = currency.mainCurrency,
            rateMode = currency.rateMode,
            exchangeRate = exchangeRate
        )
    }
}