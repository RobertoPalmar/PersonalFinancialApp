package com.rpalmar.financialapp.views.currency.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.usecases.currency.GetCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.rpalmar.financialapp.views.ui.UIEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
//    private val updateCurrencyUseCase: UpdateCurrencyUseCase
) : ViewModel() {

    private val _currencyUiState = MutableStateFlow(CurrencyUiState())
    val currencyUiState = _currencyUiState.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun loadCurrencies() {
        viewModelScope.launch {
            _currencyUiState.value = _currencyUiState.value.copy(isLoading = true)
            try {
                getCurrenciesUseCase()?.collectLatest { currencies ->
                    _currencyUiState.value = _currencyUiState.value.copy(
                        currencyList = currencies,
                        isLoading = false
                    )
                }
            } finally {
                if (_currencyUiState.value.isLoading) {
                    _currencyUiState.value = _currencyUiState.value.copy(isLoading = false)
                }
            }
        }
    }    fun onCurrencyFormEvent(event: CurrencyFormEvent) {
        when (event) {
            is CurrencyFormEvent.OnExchangeRateChange -> {
                val regex = Regex("^\\d*\\.?\\d*$")
                if (event.value.isEmpty() || event.value.matches(regex)) {
                    _currencyUiState.value = _currencyUiState.value.copy(
                        exchangeRate = event.value,
                        errors = _currencyUiState.value.errors - "exchangeRate"
                    )
                }
            }
            is CurrencyFormEvent.Submit -> {
                updateCurrency()
            }
        }
    }

    fun setSelectedCurrency(currency: CurrencyDomain) {
        _currencyUiState.value = _currencyUiState.value.copy(
            selectedCurrency = currency,
            exchangeRate = currency.exchangeRate.toString()
        )
    }

    private fun validateForm(): Boolean {
        val errors = mutableMapOf<String, String>()
        if (_currencyUiState.value.exchangeRate.isBlank() || _currencyUiState.value.exchangeRate.toDoubleOrNull() ?: 0.0 <= 0) {
            errors["exchangeRate"] = "La tasa de cambio debe ser mayor a cero."
        }
        _currencyUiState.value = _currencyUiState.value.copy(errors = errors)
        return errors.isEmpty()
    }

    private fun updateCurrency() {
        viewModelScope.launch {
            if (validateForm()) {
                _currencyUiState.value = _currencyUiState.value.copy(isSaving = true)
                try {
                    val updatedCurrency = _currencyUiState.value.selectedCurrency!!.copy(
                        exchangeRate = _currencyUiState.value.exchangeRate.toDouble()
                    )
//                    updateCurrencyUseCase(updatedCurrency)
                    _uiEvent.send(UIEvent.Success)
                } catch (e: Exception) {
                    _uiEvent.send(UIEvent.ShowError("Error al actualizar la moneda."))
                } finally {
                    _currencyUiState.value = _currencyUiState.value.copy(isSaving = false)
                }
            }
        }
    }
}
