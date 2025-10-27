package com.rpalmar.financialapp.views.currency

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.views.currency.data.CurrencyFormEvent
import com.rpalmar.financialapp.views.currency.data.CurrencyViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.componentes.BaseTextField
import com.rpalmar.financialapp.views.ui.componentes.FormNavigatorButtonSection
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.TitleSectionCard
import com.rpalmar.financialapp.views.ui.theme.Blue

@Composable
fun CurrencyFormScreen(
    navController: NavHostController,
    currencyId: Long,
) {
    val viewModel: CurrencyViewModel = hiltViewModel()
    val currencyState by viewModel.currencyUiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        val currency = viewModel.currencyUiState.value.currencyList.find { it.id == currencyId }
        if (currency != null) {
            viewModel.setSelectedCurrency(currency)
        }
    }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Success -> navController.popBackStack()
                is UIEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    MainLayout {
        Column(modifier = Modifier.fillMaxSize()) {
            TitleSectionCard(
                title = "Edit Currency",
                subtitle = currencyState.selectedCurrency?.name ?: "",
                backgroundColor = Blue,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            BaseTextField(
                value = currencyState.exchangeRate,
                onValueChange = { viewModel.onCurrencyFormEvent(CurrencyFormEvent.OnExchangeRateChange(it)) },
                label = "Exchange Rate",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                errorMessage = currencyState.errors["exchangeRate"]
            )
            Spacer(modifier = Modifier.weight(1f))
            FormNavigatorButtonSection(
                onConfirm = { viewModel.onCurrencyFormEvent(CurrencyFormEvent.Submit) },
                onBackPressed = { navController.popBackStack() },
                isEditing = true,
                isSaving = currencyState.isSaving
            )
        }
    }
}
