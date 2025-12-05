package com.rpalmar.financialapp.views.currency.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CurrencyUIState(
    //FORM INPUT FILES
    val id: Long? = null,
    val name: String = "",
    val ISO:String = "",
    val symbol: String = "",
    val exchangeRate: Double? = null,
    val isMainCurrency: Boolean = false,

    //AUX DATA
    var errors: Map<String, String?> = emptyMap(),
    var isEditing: Boolean = false,
    val isLoading: Boolean = false,
    var isSaving: Boolean = false
)


sealed class CurrencyFormEvent {
    data class OnNameChange(val value: String) : CurrencyFormEvent()
    data class OnISOChange(val value: String) : CurrencyFormEvent()
    data class OnSymbolChange(val value: String) : CurrencyFormEvent()
    data class OnMainCurrencyChange(val value: Boolean) : CurrencyFormEvent()
    data class OnExchangeRateChange(val value: Double) : CurrencyFormEvent()
    object Submit : CurrencyFormEvent()
    object Reset : CurrencyFormEvent()
}

