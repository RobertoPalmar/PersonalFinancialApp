package com.rpalmar.financialapp.views.account.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.usecases.account.CreateAccountUseCase
import com.rpalmar.financialapp.usecases.account.DeleteAccountUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountByIDUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountDashboardDataUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountsListUseCase
import com.rpalmar.financialapp.usecases.account.GetTransactionListPerAccountUseCase
import com.rpalmar.financialapp.usecases.account.UpdateAccountUseCase
import com.rpalmar.financialapp.usecases.currency.GetCurrenciesUseCase
import com.rpalmar.financialapp.views.ui.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountDashboardDataUseCase: GetAccountDashboardDataUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
    private val getAccountListUseCase: GetAccountsListUseCase,
    private val getTransactionListPerAccountUseCase: GetTransactionListPerAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    //UI STATE
    private val _accountUIState = MutableStateFlow(AccountUIState())
    val accountUIState = _accountUIState.asStateFlow();

    //UI EVENT LISTENER
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Handle events from the account form
     */
    fun onAccountFormEvent(event: AccountFormEvent) {
        when (event) {
            is AccountFormEvent.OnAccountNameChange -> {
                _accountUIState.value = _accountUIState.value.copy(
                    accountName = event.value,
                    errors = _accountUIState.value.errors - "accountName"
                )
            }

            is AccountFormEvent.OnDescriptionChange -> {
                _accountUIState.value = _accountUIState.value.copy(
                    description = event.value,
                    errors = _accountUIState.value.errors - "description"
                )
            }

            is AccountFormEvent.OnCurrencyChange -> {
                _accountUIState.value = _accountUIState.value.copy(
                    currency = event.value,
                    errors = _accountUIState.value.errors - "currency"
                )
            }

            is AccountFormEvent.OnColorChange -> {
                _accountUIState.value = _accountUIState.value.copy(
                    color = event.value,
                    errors = _accountUIState.value.errors - "color"
                )
            }

            is AccountFormEvent.OnIconChange -> {
                _accountUIState.value = _accountUIState.value.copy(
                    icon = event.value,
                    errors = _accountUIState.value.errors - "icon"
                )
            }

            is AccountFormEvent.OnBalanceChange -> {
                val regex = Regex("^\\d*\\.?\\d{0,2}$")
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _accountUIState.value = _accountUIState.value.copy(
                        balance = event.value,
                        errors = _accountUIState.value.errors - "initBalance"
                    )
                }
            }

            is AccountFormEvent.Submit -> {
                if(_accountUIState.value.isEditing) updateAccount()
                else saveAccount()
            }

            is AccountFormEvent.Reset -> {
                cleanForm()
            }

        }
    }

    /**
     * Save the account to the database
     */
    fun saveAccount() {
        viewModelScope.launch {
            try {
                //START LOADING
                _accountUIState.value = _accountUIState.value.copy(
                    isSaving = true
                )

                //VALIDATE FIEDLS
                var validForm = validateFormFields()
                if (!validForm) {
                    Log.e("AccountViewModel", "Formulario Invalido")
                    return@launch
                }

                //MAP TO DOMAIN ENTITY
                val account = AccountDomain(
                    name = _accountUIState.value.accountName,
                    description = _accountUIState.value.description,
                    currency = _accountUIState.value.currency!!,
                    balance = _accountUIState.value.balance.toDouble(),
                    balanceInMainCurrency = 0.0,
                    style = com.rpalmar.financialapp.models.database.StyleEntity(
                        color = _accountUIState.value.color,
                        icon = _accountUIState.value.icon
                    )
                )

                //CREATE ACCOUNT
                createAccountUseCase(account)

                //NOTIFY SUCCESS CREATION
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception) {
                Log.e("AccountViewModel", e.message.toString())

                //NOTIFY ERROR CREATION
                _uiEvent.send(UIEvent.ShowError("Error al Crear Cuenta"))

            } finally {
                //FINISH LOADING
                _accountUIState.value = _accountUIState.value.copy(isSaving = false)
            }
        }
    }

    fun updateAccount(){
        viewModelScope.launch {
            try {
                //START LOADING
                _accountUIState.value = _accountUIState.value.copy(
                    isSaving = true
                )

                //VALIDATE FIEDLS
                var validForm = validateFormFields()
                if (!validForm) {
                    Log.e("AccountViewModel", "Formulario Invalido")
                    return@launch
                }

                //MAP TO DOMAIN ENTITY
                val account = AccountDomain(
                    id = _accountUIState.value.id!!,
                    name = _accountUIState.value.accountName,
                    description = _accountUIState.value.description,
                    currency = _accountUIState.value.currency!!,
                    balance = _accountUIState.value.balance.toDouble(),
                    balanceInMainCurrency = 0.0,
                    style = com.rpalmar.financialapp.models.database.StyleEntity(
                        color = _accountUIState.value.color,
                        icon = _accountUIState.value.icon
                    )
                )

                //UPDATE ACCOUNT
                updateAccountUseCase(account)

                //NOTIFY SUCCESS UPDATE
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception) {
                Log.e("AccountViewModel", e.message.toString())

                //NOTIFY ERROR UPDATE
                _uiEvent.send(UIEvent.ShowError("Error al Actualizar Cuenta"))

            } finally {
                //FINISH LOADING
                _accountUIState.value = _accountUIState.value.copy(isSaving = false)
            }
        }
    }


    /**
     * Return a boolean indicating if the form fields are valid
     */
    fun validateFormFields(): Boolean {
        var errorCount = 0;
        if (_accountUIState.value.accountName.isEmpty()) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("accountName" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if (_accountUIState.value.description.isEmpty()) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("description" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if (_accountUIState.value.currency == null) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("currency" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if (_accountUIState.value.balance.isEmpty()) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("initBalance" to "Campo Obligatorio")
            )
            errorCount++;
        }

        return errorCount == 0;
    }

    /**
     * Load aux data related with the account form
     */
    fun loadAuxData() {
        viewModelScope.launch {
            //LOADING CURRENCIES
            _accountUIState.value = _accountUIState.value.copy(isLoading = true)

            try {
                val currencyListFlow = getCurrenciesUseCase()
                currencyListFlow?.collectLatest { currencyList ->
                    _accountUIState.value = _accountUIState.value.copy(
                        currencyList = currencyList
                    )
                }
            } finally {
                _accountUIState.value = _accountUIState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Clean form data to new entries
     */
    fun cleanForm() {
        _accountUIState.value = _accountUIState.value.copy(
            id = null,
            accountName = "",
            description = "",
            currency = null,
            balance = "",
            color = "#FF6200EE",
            icon = "ic_wallet",
            errors = emptyMap()
        )
    }

    /**
     * Load account list data for account table
     */
    fun loadAccountLisData() {
        viewModelScope.launch {
            try {
                _accountUIState.value = _accountUIState.value.copy(isLoading = true)

                //GET RELATED DATA
                var accountDashboardData = getAccountDashboardDataUseCase();

                //VALIDATE RESULT
                if (accountDashboardData == null)
                    Log.e("AccountViewModel", "Error al obtener los datos de la cuenta")
                //SET DATA IN STATE
                else {
                    _accountUIState.value = _accountUIState.value.copy(
                        accountList = accountDashboardData.accountList,
                        mainCurrency = accountDashboardData.mainCurrency,
                        totalAccountBalance = accountDashboardData.totalAccountBalance,
                    )
                }
            } finally {
                _accountUIState.value = _accountUIState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Return a flow with account list
     */
    fun getAccounts(): Flow<PagingData<AccountDomain>> {
        return getAccountListUseCase.getPaginated().cachedIn(viewModelScope)
    }

    /**
     * Return a flow with transactions list per account
     */
    fun getTransactionsPerAccount(accountID: Long): Flow<PagingData<TransactionDomain>> {
        return getTransactionListPerAccountUseCase.getPagingData(accountID).cachedIn(viewModelScope)
    }

    /**
     * Set the current account to view
     */
    fun setCurrentAccount(accountID: Long) {
        viewModelScope.launch {
            try {
                _accountUIState.value = _accountUIState.value.copy(
                    isLoading = true,
                    currentSelectedAccount = null,
                )
                val currentAccount = getAccountByIDUseCase(accountID)
                if (currentAccount == null) Log.e("AccountViewModel", "Error al obtener la cuenta")
                else _accountUIState.value = _accountUIState.value.copy(
                    currentSelectedAccount = currentAccount,
                )
            } finally {
                _accountUIState.value = _accountUIState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Set the current account fields to update
     */
    fun handleDeleteAccount() {
        viewModelScope.launch {
            deleteAccountUseCase(_accountUIState.value.currentSelectedAccount!!.id)
            _accountUIState.value = _accountUIState.value.copy(
                currentSelectedAccount = null
            )
        }
    }

    /**
     * Set the current account fields to update
     */
    fun handleUpdateAccountForm() {
        viewModelScope.launch {
            var currentAccount = _accountUIState.value.currentSelectedAccount;
            _accountUIState.value = _accountUIState.value.copy(
                id = currentAccount!!.id,
                accountName = currentAccount.name,
                description = currentAccount.description,
                currency = currentAccount.currency,
                balance = currentAccount.balance.toString(),
                color = currentAccount.style?.color ?: "",
                icon = currentAccount.style?.icon ?: "",
                errors = emptyMap(),
                isEditing = true
            )
        }
    }

}