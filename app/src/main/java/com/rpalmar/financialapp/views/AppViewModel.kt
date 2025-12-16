package com.rpalmar.financialapp.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.usecases.currency.GetMainCurrencyUseCase
import com.rpalmar.financialapp.usecases.currency.ObserveMainCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val observeMainCurrencyUseCase: ObserveMainCurrencyUseCase
) : ViewModel() {
    private val LOG_TAG = "AppViewModel"

    //MAIN CURRENCY
    val mainCurrency = observeMainCurrencyUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null
        )


    //INITIALIZATION APP
    val isAppInitialized = mainCurrency
        .map { it != null }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            false
        )
}
