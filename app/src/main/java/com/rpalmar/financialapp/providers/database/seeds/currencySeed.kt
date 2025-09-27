package com.rpalmar.financialapp.providers.database.seeds

import com.rpalmar.financialapp.models.database.CurrencyEntity
import java.util.Date

val currencySeeds = listOf(
    CurrencyEntity(
        id = 1,
        name = "Dólar estadounidense",
        ISO = "USD",
        symbol = "$",
        exchangeRate = 1.0,
        currencyPriority = 1,
        createAt = Date()
    ),
    CurrencyEntity(
        id = 2,
        name = "Bolívar venezolano",
        ISO = "VES",
        symbol = "Bs",
        exchangeRate = 0.0038,
        currencyPriority = 2,
        createAt = Date()
    ),
    CurrencyEntity(
        id = 3,
        name = "Peso colombiano",
        ISO = "COP",
        symbol = "$",
        exchangeRate = 0.00021,
        currencyPriority = 3,
        createAt = Date()
    ),
    CurrencyEntity(
        id = 4,
        name = "Euro",
        ISO = "EUR",
        symbol = "€",
        exchangeRate = 1.08,
        currencyPriority = 4,
        createAt = Date()
    )
)