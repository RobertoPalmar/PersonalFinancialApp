package com.rpalmar.financialapp.views.currency.data

import com.rpalmar.financialapp.models.domain.CurrencyDomain

data class CurrencyUiState(
    var currencyList: List<CurrencyDomain> = emptyList(),
    var isLoading: Boolean = true,
    var isSaving: Boolean = false,
    var selectedCurrency: CurrencyDomain? = null,
    var exchangeRate: String = "",
    var errors: Map<String, String> = emptyMap()
)

sealed class CurrencyFormEvent {
    data class OnExchangeRateChange(val value: String) : CurrencyFormEvent()
    object Submit : CurrencyFormEvent()
}