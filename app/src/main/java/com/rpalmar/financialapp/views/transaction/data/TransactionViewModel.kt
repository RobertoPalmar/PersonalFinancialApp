package com.rpalmar.financialapp.views.transaction.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.usecases.account.GetAccountByIDUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountsListUseCase
import com.rpalmar.financialapp.views.account.data.AccountFormEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isEmpty
import kotlin.text.matches

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
    private val getAccountListUseCase: GetAccountsListUseCase,
) : ViewModel() {

    private val _transactionUIState = MutableStateFlow(TransactionUiState())
    val transactionUIState: StateFlow<TransactionUiState> = _transactionUIState.asStateFlow()

    fun loadInitialData(accountId: Long) {
        viewModelScope.launch {
            val sourceAccount = getAccountByIDUseCase(accountId)
            val allAccounts = getAccountListUseCase.invoke()?.first() ?: emptyList()
            _transactionUIState.update {
                it.copy(
                    sourceAccount = sourceAccount,
                    accounts = allAccounts,
                    isLoading = false
                )
            }
        }
    }

    /**
     * Handle events from the account form
     */
    fun onTransactionFormEvent(event: TransactionFormEvent) {
        when (event) {
            is TransactionFormEvent.OnAmountChange -> {
                val regex = Regex("^\\d*\\.?\\d{0,2}$")
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _transactionUIState.value = _transactionUIState.value.copy(
                        amount = event.value,
                        errors = _transactionUIState.value.errors - "amount"
                    )
                }
            }
            is TransactionFormEvent.OnDescriptionChange -> {
                _transactionUIState.value = _transactionUIState.value.copy(
                    description = event.value,
                    errors = _transactionUIState.value.errors - "description"
                )
            }
            is TransactionFormEvent.OnDestinationAccountChange -> {
                _transactionUIState.value = _transactionUIState.value.copy(
                    destinationAccount = event.value,
                    errors = _transactionUIState.value.errors - "destinationAccount"
                )
            }
            is TransactionFormEvent.OnSourceAccountChange -> {
                _transactionUIState.value = _transactionUIState.value.copy(
                    sourceAccount = event.value,
                    errors = _transactionUIState.value.errors - "sourceAccount"
                )
            }
            TransactionFormEvent.Reset -> {
                cleanForm()
            }
            TransactionFormEvent.Submit -> {
//                saveTransaction()
            }
        }
    }

    /**
     * Clean form data to new entries
     */
    fun cleanForm() {
        _transactionUIState.value = _transactionUIState.value.copy(
            id = null,
            sourceAccount = null,
            destinationAccount = null,
            amount = "",
            description = "",
            errors = emptyMap()
        )
    }
}
