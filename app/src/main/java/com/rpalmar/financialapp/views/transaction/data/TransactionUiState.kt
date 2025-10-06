package com.rpalmar.financialapp.views.transaction.data

import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain

data class TransactionUiState(
    //FORM INPUT FIELDS
    val id: Long? = null,
    val sourceAccount: AccountDomain? = null,
    val destinationAccount: AccountDomain? = null,
    val amount: String = "",
    val description: String = "",
    var errors:Map<String,String?> = emptyMap(),

    //AUX DATA
    val accounts: List<AccountDomain> = emptyList(),
    val isLoading: Boolean = true
)

sealed class TransactionFormEvent{
    data class OnSourceAccountChange(val value: AccountDomain): TransactionFormEvent()
    data class OnDestinationAccountChange(val value:AccountDomain): TransactionFormEvent()
    data class OnAmountChange(val value: String): TransactionFormEvent()
    data class OnDescriptionChange(val value:String): TransactionFormEvent()
    object Submit: TransactionFormEvent()
    object Reset: TransactionFormEvent()
}
