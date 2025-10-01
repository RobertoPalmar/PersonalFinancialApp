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
import com.rpalmar.financialapp.usecases.account.GetTransactionListPerAccount
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
import javax.inject.Singleton

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountDashboardDataUseCase: GetAccountDashboardDataUseCase,
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
    private val getAccountByIDUseCase: GetAccountByIDUseCase,
    private val getTransactionListPerAccount: GetTransactionListPerAccount,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    //CREATION FORM STATE
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

            is AccountFormEvent.OnInitBalanceChange -> {
                val regex = Regex("^\\d*\\.?\\d{0,2}$")
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _accountUIState.value = _accountUIState.value.copy(
                        initBalance = event.value,
                        errors = _accountUIState.value.errors - "initBalance"
                    )
                }
            }
            is AccountFormEvent.Submit ->{ saveAccount() }
            is AccountFormEvent.Reset -> { cleanForm() }

        }
    }

    /**
     * Save the account to the database
     */
    fun saveAccount(){
        viewModelScope.launch {
            try{
                //START LOADING
                _accountUIState.value = _accountUIState.value.copy(
                    isSaving = true
                )

                //VALIDATE FIEDLS
                var validForm = validateFormFields()
                if(!validForm){
                    Log.e("AccountViewModel", "Formulario Invalido")
                    return@launch
                }

                //MAP TO DOMAIN ENTITY
                val account = AccountDomain(
                    name = _accountUIState.value.accountName,
                    description = _accountUIState.value.description,
                    currency = _accountUIState.value.currency!!,
                    balance = _accountUIState.value.initBalance.toDouble(),
                    balanceInBaseCurrency = 0.0,
                    style = com.rpalmar.financialapp.models.database.StyleEntity(
                        color = _accountUIState.value.color,
                        icon = _accountUIState.value.icon
                    )
                )

                //CREATE ACCOUNT
                createAccountUseCase(account)

                //NOTIFY SUCCESS CREATION
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception){
                Log.e("AccountViewModel", e.message.toString())

                //NOTIFY SUCCESS CREATION
                _uiEvent.send(UIEvent.ShowError("Error al Crear Cuenta"))

            } finally {
                //FINISH LOADING
                _accountUIState.value = _accountUIState.value.copy(isSaving = false)
            }
        }
    }

    /**
     * Return a boolean indicating if the form fields are valid
     */
    fun validateFormFields(): Boolean{
        var errorCount = 0;
        if(_accountUIState.value.accountName.isEmpty()){
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("accountName" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountUIState.value.description.isEmpty()){
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("description" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountUIState.value.currency == null) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("currency" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountUIState.value.initBalance.isEmpty()) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("initBalance" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountUIState.value.color.isEmpty()) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("color" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountUIState.value.icon.isEmpty()) {
            _accountUIState.value = _accountUIState.value.copy(
                errors = _accountUIState.value.errors + ("icon" to "Campo Obligatorio")
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
    fun cleanForm(){
        _accountUIState.value = _accountUIState.value.copy(
            accountName = "",
            description = "",
            currency = null,
            initBalance = "",
            color = "",
            icon = "",
            errors = emptyMap()
        )
    }

    /**
     * Load account list data for account table
     */
    fun loadAccountLisData(){
        viewModelScope.launch {
            try{
                _accountUIState.value = _accountUIState.value.copy(isLoading = true)

                //GET RELATED DATA
                var accountDashboardData = getAccountDashboardDataUseCase();

                //VALIDATE RESULT
                if(accountDashboardData == null)
                    Log.e("AccountViewModel", "Error al obtener los datos de la cuenta")
                //SET DATA IN STATE
                else{
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
     * Return a flow with transactions list per account
     */
    fun getTransactionsPerAccount(accountID:Long): Flow<PagingData<TransactionDomain>>{
        return getTransactionListPerAccount(accountID).cachedIn(viewModelScope)
    }

    /**
     * Set the current account to view
     */
    fun setCurrentAccount(accountID:Long){
        viewModelScope.launch {
            try {
                _accountUIState.value = _accountUIState.value.copy(
                    isLoading = true,
                    currentSelectedAccount = null,
                )
                val currentAccount = getAccountByIDUseCase(accountID)
                if(currentAccount == null) Log.e("AccountViewModel", "Error al obtener la cuenta")
                else _accountUIState.value = _accountUIState.value.copy(
                    currentSelectedAccount = currentAccount,
                )
            } finally {
                _accountUIState.value = _accountUIState.value.copy(isLoading = false)
            }
        }
    }

}