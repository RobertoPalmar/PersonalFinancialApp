package com.rpalmar.financialapp.views.transaction.data

import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux

data class TransactionUiState(
    //FORM INPUT FIELDS
    val id: Long? = null,
    val originSource: SimpleTransactionSourceAux? = null,
    val destinationSource: SimpleTransactionSourceAux? = null,
    val amount: String = "",
    val destinationAmount: String = "",
    val exchangeRate: String = "1.0",
    val description: String = "",
    val transactionType: TransactionType? = null,
    val transactionDate: String = "",
    val currency: CurrencyDomain? = null,
    val transactionExchangeRate: Double = 0.0,
    var errors:Map<String,String?> = emptyMap(),
    var isEditing: Boolean = false,

    //AUX DATA
    val transactionSources: List<SimpleTransactionSourceAux> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isCrossCurrencyTransaction: Boolean = false
)

sealed class TransactionFormEvent{
    data class OnOriginSourceChange(val value:SimpleTransactionSourceAux): TransactionFormEvent()
    data class OnDestinationSourceChange(val value:SimpleTransactionSourceAux): TransactionFormEvent()
    data class OnAmountChange(val value: String): TransactionFormEvent()
    data class OnExchangeRateChange(val value: String): TransactionFormEvent()
    data class OnDestinationAmountChange(val value: String): TransactionFormEvent()
    data class OnDescriptionChange(val value:String): TransactionFormEvent()
    data class Submit(val transactionType:TransactionType): TransactionFormEvent()
    object Reset: TransactionFormEvent()
}
