package com.rpalmar.financialapp.views

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.usecases.currency.GetMainCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val getMainCurrencyUseCase: GetMainCurrencyUseCase
) : ViewModel() {

    //MAIN CURRENCY
    private val _mainCurrency = MutableStateFlow<CurrencyDomain?>(null)
    val mainCurrency = _mainCurrency.asStateFlow()

    //INITIALIZATION STATE
    private val _isAppInitialized = MutableStateFlow(false)
    val isAppInitialized = _isAppInitialized.asStateFlow()

    private val LOG_TAG = "AppViewModel"

    init {
        viewModelScope.launch {
            val currency = getMainCurrencyUseCase()
            if (currency == null) {
                Log.i(LOG_TAG, "Error al obtener la moneda principal")
            } else {
                _mainCurrency.value = currency
                _isAppInitialized.value = true;
            }
        }
    }
}
