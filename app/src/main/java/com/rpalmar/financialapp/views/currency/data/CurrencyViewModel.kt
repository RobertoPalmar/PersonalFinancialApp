package com.rpalmar.financialapp.views.currency.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.usecases.currency.CreateCurrencyUseCase
import com.rpalmar.financialapp.usecases.currency.GetCurrencyByIDUseCase
import com.rpalmar.financialapp.usecases.currency.UpdateCurrencyUseCase
import com.rpalmar.financialapp.views.category.data.CategoryFormEvent
import com.rpalmar.financialapp.views.ui.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val createCurrencyUseCase: CreateCurrencyUseCase,
    private val updateCurrencyUseCase: UpdateCurrencyUseCase,
    private val getCurrencyByIDUseCase: GetCurrencyByIDUseCase
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
}
