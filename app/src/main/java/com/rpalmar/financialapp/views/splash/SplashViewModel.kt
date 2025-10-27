package com.rpalmar.financialapp.views.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.usecases.sync.SyncExchangeRateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val syncExchangeRateUseCase: SyncExchangeRateUseCase
) : ViewModel() {

    fun syncExchangeRates() {
        viewModelScope.launch {
            syncExchangeRateUseCase()
        }
    }
}
