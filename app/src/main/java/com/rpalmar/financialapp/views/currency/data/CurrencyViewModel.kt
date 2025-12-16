package com.rpalmar.financialapp.views.currency.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.models.CurrencyDeletionValidation
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.usecases.account.GetAccountsCountByCurrencyUseCase
import com.rpalmar.financialapp.usecases.currency.CreateCurrencyUseCase
import com.rpalmar.financialapp.usecases.currency.DeleteCurrencyUseCase
import com.rpalmar.financialapp.usecases.currency.GetCurrencyByIDUseCase
import com.rpalmar.financialapp.usecases.currency.GetCurrencyListUseCase
import com.rpalmar.financialapp.usecases.currency.UpdateCurrencyUseCase
import com.rpalmar.financialapp.usecases.preferences.GetWarningPreferenceUseCase
import com.rpalmar.financialapp.usecases.preferences.SetWarningPreferenceUseCase
import com.rpalmar.financialapp.views.ui.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val createCurrencyUseCase: CreateCurrencyUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase,
    private val deleteCurrencyUseCase: DeleteCurrencyUseCase,
    private val getAccountsCountByCurrencyUseCase: GetAccountsCountByCurrencyUseCase,
    private val getCurrencyListUseCase: GetCurrencyListUseCase,
    private val getWarningPreferenceUseCase: GetWarningPreferenceUseCase,
    private val setWarningPreferenceUseCase: SetWarningPreferenceUseCase
) : ViewModel() {

    val LOG_TAG = "CurrencyViewModel"

    //UI STATE
    private val _currencyUIState = MutableStateFlow(CurrencyUIState())
    val currencyUIState = _currencyUIState.asStateFlow()

    //UI EVENT LISTENER
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Handle events from the currency form
     */
    fun onCurrencyFormEvent(event: CurrencyFormEvent) {
        when (event) {

            is CurrencyFormEvent.OnNameChange -> {
                _currencyUIState.value = _currencyUIState.value.copy(
                    name = event.value,
                    errors = _currencyUIState.value.errors - "name"
                )
            }

            is CurrencyFormEvent.OnISOChange -> {
                _currencyUIState.value = _currencyUIState.value.copy(
                    ISO = event.value,
                    errors = _currencyUIState.value.errors - "iso"
                )
            }

            is CurrencyFormEvent.OnSymbolChange -> {
                _currencyUIState.value = _currencyUIState.value.copy(
                    symbol = event.value,
                    errors = _currencyUIState.value.errors - "symbol"
                )
            }

            is CurrencyFormEvent.OnExchangeRateChange -> {
                _currencyUIState.value = _currencyUIState.value.copy(
                    exchangeRate = event.value,
                    errors = _currencyUIState.value.errors - "exchangeRate"
                )
            }

            is CurrencyFormEvent.OnMainCurrencyChange -> {
                _currencyUIState.value = _currencyUIState.value.copy(
                    isMainCurrency = event.value,
                    errors = _currencyUIState.value.errors - "isMainCurrency"
                )
            }

            CurrencyFormEvent.Submit -> {
                if (_currencyUIState.value.isEditing) updateCurrency()
                else saveCurrency()
            }

            CurrencyFormEvent.Reset -> {
                cleanForm()
            }
        }
    }

    /**
     * Save the currency to the database
     */
    fun saveCurrency() {
        viewModelScope.launch {
            try {
                // START LOADING
                _currencyUIState.value = _currencyUIState.value.copy(
                    isLoading = true
                )

                // VALIDATE FIELDS
                val isValid = validateFormFields()
                if (!isValid) {
                    Log.e(LOG_TAG, "Formulario inválido")
                    return@launch
                }

                // MAP TO DOMAIN ENTITY
                val currency = CurrencyDomain(
                    name = _currencyUIState.value.name,
                    ISO = _currencyUIState.value.ISO,
                    symbol = _currencyUIState.value.symbol,
                    exchangeRate = _currencyUIState.value.exchangeRate!!,
                    mainCurrency = _currencyUIState.value.isMainCurrency,
                )

                // CREATE CURRENCY
                createCurrencyUseCase(currency)

                // NOTIFY SUCCESS
                _uiEvent.send(UIEvent.Success)

            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message.toString())

                // NOTIFY ERROR
                _uiEvent.send(UIEvent.ShowError("Error al crear Moneda"))
            } finally {
                // FINISH LOADING
                _currencyUIState.value = _currencyUIState.value.copy(isLoading = false)
            }
        }
    }

    fun updateCurrency() {
        viewModelScope.launch {
            try {
                // START LOADING
                _currencyUIState.value = _currencyUIState.value.copy(
                    isLoading = true
                )

                // VALIDATE FIELDS
                val isValid = validateFormFields()
                if (!isValid) {
                    Log.e(LOG_TAG, "Formulario inválido")
                    return@launch
                }

                // MAP TO DOMAIN ENTITY
                val currency = CurrencyDomain(
                    id = _currencyUIState.value.id!!,
                    name = _currencyUIState.value.name,
                    ISO = _currencyUIState.value.ISO,
                    symbol = _currencyUIState.value.symbol,
                    exchangeRate = _currencyUIState.value.exchangeRate!!,
                    mainCurrency = _currencyUIState.value.isMainCurrency
                )

                // UPDATE CURRENCY
                updateCurrencyUseCase(currency)

                // NOTIFY SUCCESS UPDATE
                _uiEvent.send(UIEvent.Success)

            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message.toString())

                // NOTIFY ERROR UPDATE
                _uiEvent.send(UIEvent.ShowError("Error al Actualizar Moneda"))
            } finally {
                // FINISH LOADING
                _currencyUIState.value = _currencyUIState.value.copy(
                    isLoading = false
                )
            }
        }
    }

    /**
     * Return a boolean indicating if the form fields are valid
     */
    fun validateFormFields(): Boolean {
        var errorCount = 0
        val state = _currencyUIState.value

        // VALIDATE NAME
        if (state.name.isEmpty()) {
            _currencyUIState.value = _currencyUIState.value.copy(
                errors = state.errors + ("name" to "Campo Obligatorio")
            )
            errorCount++
        }

        // VALIDATE ISO
        if (state.ISO.isEmpty()) {
            _currencyUIState.value = _currencyUIState.value.copy(
                errors = _currencyUIState.value.errors + ("ISO" to "Campo Obligatorio")
            )
            errorCount++
        }

        // VALIDATE SYMBOL
        if (state.symbol.isEmpty()) {
            _currencyUIState.value = _currencyUIState.value.copy(
                errors = _currencyUIState.value.errors + ("symbol" to "Campo Obligatorio")
            )
            errorCount++
        }

        // VALIDATE EXCHANGE RATE
        if (state.exchangeRate == null) {
            _currencyUIState.value = _currencyUIState.value.copy(
                errors = _currencyUIState.value.errors + ("exchangeRate" to "Campo Obligatorio")
            )
            errorCount++
        }

        return errorCount == 0
    }

    /**
     * Clean form data to new entries
     */
    fun cleanForm() {
        _currencyUIState.value = _currencyUIState.value.copy(
            id = null,
            name = "",
            ISO = "",
            symbol = "",
            exchangeRate = null,
            isMainCurrency = false,
            errors = emptyMap(),
            isEditing = false
        )
    }

    /**
     * Validate if a currency can be deleted
     */
    suspend fun validateCurrencyDeletion(currencyId: Long): CurrencyDeletionValidation {
        //VALIDATE ACCOUNT ASSOCIATED
        val accountsCount = getAccountsCountByCurrencyUseCase(currencyId) ?: 0
        if (accountsCount > 0)
            return CurrencyDeletionValidation.HasAccounts(accountsCount)

        //VALIDATE LAST ACTIVE CURRENCY
        val activeCurrencies = getCurrencyListUseCase.invoke()!!.first()
        if (activeCurrencies.size == 1 && activeCurrencies[0].id == currencyId) {
            return CurrencyDeletionValidation.LastActiveCurrency
        }
        
        //VALIDATE MAIN CURRENCY
        val currency = activeCurrencies.find { it.id == currencyId }
        if (currency?.mainCurrency == true && activeCurrencies.size > 1) {
            return CurrencyDeletionValidation.IsMainCurrency
        }
        
        return CurrencyDeletionValidation.CanDelete
    }

    /**
     * Delete currency
     */
    fun handleDeleteCurrency(currencyID: Long) {
        viewModelScope.launch {
            deleteCurrencyUseCase(currencyID)
        }
    }

    /**
     * Set the current currency fields to update
     */
    fun handleUpdateCurrencyForm(currency: CurrencyDomain) {
        viewModelScope.launch {
            _currencyUIState.value = _currencyUIState.value.copy(
                id = currency.id,
                name = currency.name,
                ISO = currency.ISO,
                symbol = currency.symbol,
                exchangeRate = currency.exchangeRate,
                isMainCurrency = currency.mainCurrency,
                errors = emptyMap(),
                isEditing = true
            )
        }
    }

    fun shouldShowWarning(key: String): Boolean {
        return getWarningPreferenceUseCase(key)
    }

    fun setWarningPreference(key: String, show: Boolean) {
        setWarningPreferenceUseCase(key, show)
    }
}
