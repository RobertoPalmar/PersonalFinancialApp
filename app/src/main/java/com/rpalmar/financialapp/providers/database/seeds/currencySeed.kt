package com.rpalmar.financialapp.providers.database.seeds

import com.rpalmar.financialapp.models.ExchangeRateType
import com.rpalmar.financialapp.models.RateMode
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.ExchangeRateEntity
import java.util.Date
val currencySeeds = listOf(
    CurrencyEntity(
        id = 1,
        name = "Dólar estadounidense",
        ISO = "USD",
        symbol = "$",
        mainCurrency = true,
        rateMode = RateMode.MANUAL
    ),
    CurrencyEntity(
        id = 2,
        name = "Bolívar venezolano",
        ISO = "VES",
        symbol = "Bs",
        mainCurrency = false,
        rateMode = RateMode.AUTO
    ),
    CurrencyEntity(
        id = 3,
        name = "Euro",
        ISO = "EUR",
        symbol = "€",
        mainCurrency = false,
        rateMode = RateMode.AUTO
    )
)

val exchangeRateSeeds = listOf(
    // USD es la base
    ExchangeRateEntity(
        id = 1,
        currencyID = 1, // USD
        rate = 1.0,
        source = "manual",
        type = ExchangeRateType.MANUAL,
        createAt = Date()
    ),
    ExchangeRateEntity(
        id = 2,
        currencyID = 2, // VES
        rate = 175.438596,
        source = "api",
        type = ExchangeRateType.API,
        createAt = Date()
    ),
    ExchangeRateEntity(
        id = 3,
        currencyID = 3, // EUR
        rate = 0.854700855,
        source = "api",
        type = ExchangeRateType.API,
        createAt = Date()
    )
)
