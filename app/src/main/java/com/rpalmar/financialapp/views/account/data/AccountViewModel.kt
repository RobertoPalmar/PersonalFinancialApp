package com.rpalmar.financialapp.views.account.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.usecases.account.CreateAccountUseCase
import com.rpalmar.financialapp.usecases.account.DeleteAccountUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountByIDUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountDashboardDataUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountsListUseCase
import com.rpalmar.financialapp.usecases.account.GetTransactionListPerAccount
import com.rpalmar.financialapp.usecases.account.UpdateAccountUseCase
import com.rpalmar.financialapp.usecases.currency.GetCurrenciesUseCase
import com.rpalmar.financialapp.usecases.currency.GetMainCurrencyUseCase
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
    private val getTransactionListPerAccount: GetTransactionListPerAccount,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : ViewModel() {

    //CREATION FORM STATE
    private val _accountFormState = MutableStateFlow(AccountFormState())
    val accountFormState = _accountFormState.asStateFlow();

    //UI EVENT LISTENER
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Handle events from the account form
     */
    fun onAccountFormEvent(event: AccountFormEvent) {
        when (event) {
            is AccountFormEvent.OnAccountNameChange -> {
                _accountFormState.value = _accountFormState.value.copy(
                    accountName = event.value,
                    errors = _accountFormState.value.errors - "accountName"
                )
            }

            is AccountFormEvent.OnDescriptionChange -> {
                _accountFormState.value = _accountFormState.value.copy(
                    description = event.value,
                    errors = _accountFormState.value.errors - "description"
                )
            }

            is AccountFormEvent.OnCurrencyChange -> {
                _accountFormState.value = _accountFormState.value.copy(
                    currency = event.value,
                    errors = _accountFormState.value.errors - "currency"
                )
            }

            is AccountFormEvent.OnColorChange -> {
                _accountFormState.value = _accountFormState.value.copy(
                    color = event.value,
                    errors = _accountFormState.value.errors - "color"
                )
            }

            is AccountFormEvent.OnIconChange -> {
                _accountFormState.value = _accountFormState.value.copy(
                    icon = event.value,
                    errors = _accountFormState.value.errors - "icon"
                )
            }

            is AccountFormEvent.OnInitBalanceChange -> {
                val regex = Regex("^\\d*\\.?\\d{0,2}$")
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _accountFormState.value = _accountFormState.value.copy(
                        initBalance = event.value,
                        errors = _accountFormState.value.errors - "initBalance"
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
                _accountFormState.value = _accountFormState.value.copy(
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
                    name = _accountFormState.value.accountName,
                    description = _accountFormState.value.description,
                    currency = _accountFormState.value.currency!!,
                    initBalance = _accountFormState.value.initBalance.toDouble(),
                    initBalanceInBaseCurrency = 0.0,
                    style = com.rpalmar.financialapp.models.database.StyleEntity(
                        color = _accountFormState.value.color,
                        icon = _accountFormState.value.icon
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
                _accountFormState.value = _accountFormState.value.copy(isSaving = false)
            }
        }
    }

    /**
     * Return a boolean indicating if the form fields are valid
     */
    fun validateFormFields(): Boolean{
        var errorCount = 0;
        if(_accountFormState.value.accountName.isEmpty()){
            _accountFormState.value = _accountFormState.value.copy(
                errors = _accountFormState.value.errors + ("accountName" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountFormState.value.description.isEmpty()){
            _accountFormState.value = _accountFormState.value.copy(
                errors = _accountFormState.value.errors + ("description" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountFormState.value.currency == null) {
            _accountFormState.value = _accountFormState.value.copy(
                errors = _accountFormState.value.errors + ("currency" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountFormState.value.initBalance.isEmpty()) {
            _accountFormState.value = _accountFormState.value.copy(
                errors = _accountFormState.value.errors + ("initBalance" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountFormState.value.color.isEmpty()) {
            _accountFormState.value = _accountFormState.value.copy(
                errors = _accountFormState.value.errors + ("color" to "Campo Obligatorio")
            )
            errorCount++;
        }
        if(_accountFormState.value.icon.isEmpty()) {
            _accountFormState.value = _accountFormState.value.copy(
                errors = _accountFormState.value.errors + ("icon" to "Campo Obligatorio")
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
            _accountFormState.value = _accountFormState.value.copy(isLoading = true)

            try {
                val currencyListFlow = getCurrenciesUseCase()
                currencyListFlow?.collectLatest { currencyList ->
                    _accountFormState.value = _accountFormState.value.copy(
                        currencyList = currencyList
                    )
                }
            } finally {
                _accountFormState.value = _accountFormState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Clean form data to new entries
     */
    fun cleanForm(){
        _accountFormState.value = AccountFormState()
    }

    /**
     * Load account list data for account table
     */
    fun loadAccountLisData(){
        viewModelScope.launch {
            try{
                _accountFormState.value = _accountFormState.value.copy(isLoading = true)

                //GET RELATED DATA
                var accountDashboardData = getAccountDashboardDataUseCase();

                //VALIDATE RESULT
                if(accountDashboardData == null)
                    Log.e("AccountViewModel", "Error al obtener los datos de la cuenta")
                //SET DATA IN STATE
                else{
                    _accountFormState.value = _accountFormState.value.copy(
                        accountList = accountDashboardData.accountList,
                        mainCurrency = accountDashboardData.mainCurrency,
                        totalAccountBalance = accountDashboardData.totalAccountBalance,
                    )
                }
            } finally {
                _accountFormState.value = _accountFormState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Return a flow with transactions list per account
     */
    fun getTransactionsPerAccount(accountID:Long): Flow<PagingData<TransactionWithCurrencyRelation>>?{
        return getTransactionListPerAccount(accountID).cachedIn(viewModelScope)
    }

}