package com.rpalmar.financialapp.providers.database.seeds

import com.rpalmar.financialapp.models.CategoryType
import com.rpalmar.financialapp.models.Constants.ADJUSTMENT_CATEGORY
import com.rpalmar.financialapp.models.Constants.TRANSFER_CATEGORY
import com.rpalmar.financialapp.models.database.CategoryEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.ExchangeRateHistoryEntity
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.views.ui.components.toHex
import com.rpalmar.financialapp.views.ui.theme.AccentYellow
import com.rpalmar.financialapp.views.ui.theme.Cyan
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ExchangeAltSolid
import compose.icons.lineawesomeicons.WrenchSolid
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

val categorySeeds = listOf(
    ADJUSTMENT_CATEGORY,
    TRANSFER_CATEGORY
)