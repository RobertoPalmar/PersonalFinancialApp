package com.rpalmar.financialapp.views.account.data

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.StyleDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.usecases.account.CreateAccountUseCase
import com.rpalmar.financialapp.usecases.account.DeleteAccountUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountByIDUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountsListUseCase
import com.rpalmar.financialapp.usecases.account.GetTransactionListUseCase
import com.rpalmar.financialapp.usecases.account.UpdateAccountUseCase
import com.rpalmar.financialapp.usecases.currency.GetCurrencyListUseCase
import com.rpalmar.financialapp.views.ui.UIEvent
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CashRegisterSolid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getCurrencyListUseCase: GetCurrencyListUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
    private val getAccountListUseCase: GetAccountsListUseCase,
    private val getTransactionListUseCase: GetTransactionListUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    val LOG_TAG = "AccountViewModel"

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
                _accountUIState.value = _accountUIState.value.copy(
                    balance = event.value,
                    errors = _accountUIState.value.errors - "initBalance"
                )
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
                    style = StyleDomain(
                        uiColor = _accountUIState.value.color,
                        uiIcon = _accountUIState.value.icon
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
                    style = StyleDomain(
                        uiColor = _accountUIState.value.color,
                        uiIcon = _accountUIState.value.icon
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
                _accountUIState.value = _accountUIState.value.copy(
                    isSaving = false,
                )
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
                val currencyList = getCurrencyListUseCase()!!.first()
                val mainCurrency = currencyList.first {c -> c.mainCurrency}
                _accountUIState.value = _accountUIState.value.copy(
                    currencyList = currencyList,
                    mainCurrency = mainCurrency
                )
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error loading currencies: ${e.message}")
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
            balance = 0.0,
            color = Color(0xFF81C784),
            icon = LineAwesomeIcons.CashRegisterSolid,
            errors = emptyMap(),
            isEditing = false
        )
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
        return getTransactionListUseCase.getPagingDataByAccount(accountID).cachedIn(viewModelScope)
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
     * Delete account and set null his UI state
     */
    fun handleDeleteAccount(accountID: Long) {
        viewModelScope.launch {
            deleteAccountUseCase(accountID)
            _accountUIState.value = _accountUIState.value.copy(
                currentSelectedAccount = null
            )
        }
    }

    /**
     * Set the current account fields to update
     */
    fun handleUpdateAccountForm(account: AccountDomain) {
        viewModelScope.launch {
            _accountUIState.value = _accountUIState.value.copy(
                id = account.id,
                accountName = account.name,
                description = account.description,
                currency = account.currency,
                balance = account.balance,
                color = account.style.uiColor,
                icon = account.style.uiIcon,
                errors = emptyMap(),
                isEditing = true
            )
        }
    }

}