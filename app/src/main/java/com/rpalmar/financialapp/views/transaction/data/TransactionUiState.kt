package com.rpalmar.financialapp.views.transaction.data

import com.rpalmar.financialapp.models.CategoryType
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.StyleDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.QuestionSolid
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class TransactionUiState(
    //FORM INPUT FIELDS
    val id: Long? = null,
    val originSource: SimpleTransactionSourceAux? = null,
    val destinationSource: SimpleTransactionSourceAux? = null,
    val amount: Double = 0.0,
    val destinationAmount: Double = 0.0,
    val exchangeRate: Double = 1.0,
    val description: String = "",
    val transactionType: TransactionType? = null,
    val transactionDate: String = "",
    val currency: CurrencyDomain? = null,
    val transactionExchangeRate: Double = 0.0,
    val adjustmentAmount: Double = 0.0,
    val category: CategoryDomain? = null,
    var errors: Map<String, String?> = emptyMap(),
    var isEditing: Boolean = false,

    //AUX DATA
    val transactionSources: List<SimpleTransactionSourceAux> = emptyList(),
    val categories: List<CategoryDomain> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isCrossCurrencyTransaction: Boolean = false
)

sealed class TransactionFormEvent {
    data class OnOriginSourceChange(val value: SimpleTransactionSourceAux) : TransactionFormEvent()
    data class OnDestinationSourceChange(val value: SimpleTransactionSourceAux) : TransactionFormEvent()
    data class OnAmountChange(val value: Double) : TransactionFormEvent()
    data class OnExchangeRateChange(val value: Double) : TransactionFormEvent()
    data class OnDestinationAmountChange(val value: Double) : TransactionFormEvent()
    data class OnDescriptionChange(val value: String) : TransactionFormEvent()
    data class OnDateChange(val value: String) : TransactionFormEvent()
    data class OnCategoryChange(val value: CategoryDomain) : TransactionFormEvent()
    data class Submit(val transactionType: TransactionType) : TransactionFormEvent()
    object Reset : TransactionFormEvent()
}
