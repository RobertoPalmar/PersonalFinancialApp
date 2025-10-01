package com.rpalmar.financialapp.views.account.data

import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain

data class AccountUIState(
    //FORM INPUT FIELDS
    var accountName: String = "",
    var description: String = "",
    var currency: CurrencyDomain? = null,
    var initBalance: String = "",
    var color: String = "",
    var icon: String = "",
    var errors:Map<String,String?> = emptyMap(),

    //AUX DATA
    var currencyList: List<CurrencyDomain> = emptyList(),
    var mainCurrency: CurrencyDomain? = null,
    var accountList: List<AccountDomain> = emptyList(),
    var totalAccountBalance:Double = 0.0,
    var isLoading: Boolean = true,
    var isSaving: Boolean = false,

    var currentSelectedAccount: AccountDomain? = null
)

sealed class AccountFormEvent{
    data class OnAccountNameChange(val value:String): AccountFormEvent()
    data class OnDescriptionChange(val value:String): AccountFormEvent()
    data class OnCurrencyChange(val value: CurrencyDomain): AccountFormEvent()
    data class OnInitBalanceChange(val value:String): AccountFormEvent()
    data class OnColorChange(val value:String): AccountFormEvent()
    data class OnIconChange(val value:String): AccountFormEvent()
    object Submit: AccountFormEvent()
    object Reset: AccountFormEvent()
}