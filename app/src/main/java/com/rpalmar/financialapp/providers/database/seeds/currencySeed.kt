package com.rpalmar.financialapp.providers.database.seeds

import com.rpalmar.financialapp.models.ExchangeRateType
import com.rpalmar.financialapp.models.RateMode
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.ExchangeRateHistoryEntity
import java.util.Date
val currencySeeds = listOf(
    CurrencyEntity(
        id = 1,
        name = "Dólar estadounidense",
        ISO = "USD",
        symbol = "$",
        mainCurrency = true,
        currentExchangeRate = 1.0,
    ),
    CurrencyEntity(
        id = 2,
        name = "Bolívar venezolano",
        ISO = "VES",
        symbol = "Bs",
        mainCurrency = false,
        currentExchangeRate = 175.438596,
    ),
    CurrencyEntity(
        id = 3,
        name = "Euro",
        ISO = "EUR",
        symbol = "€",
        mainCurrency = false,
        currentExchangeRate = 0.854700855,
    )
)

val exchangeRateSeeds = listOf(
    // USD es la base
    ExchangeRateHistoryEntity(
        id = 1,
        currencyID = 1, // USD
        rate = 1.0,
        createAt = Date()
    ),
    ExchangeRateHistoryEntity(
        id = 2,
        currencyID = 2, // VES
        rate = 175.438596,
        createAt = Date()
    ),
    ExchangeRateHistoryEntity(
        id = 3,
        currencyID = 3, // EUR
        rate = 0.854700855,
        createAt = Date()
    )
)
