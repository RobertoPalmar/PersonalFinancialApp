package com.rpalmar.financialapp.views.currency

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rpalmar.financialapp.views.currency.data.CurrencyFormEvent
import com.rpalmar.financialapp.views.currency.data.CurrencyViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.components.*
import com.rpalmar.financialapp.views.ui.theme.Black

@Composable
fun CurrencyFormScreen(
    navController: NavController,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by currencyViewModel.currencyUIState.collectAsState()

    // HANDLE CURRENCY CREATION EVENTS
    LaunchedEffect(true) {
        currencyViewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Success -> navController.popBackStack()
                is UIEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // HANDLE CLEAN CURRENCY FORM
    LaunchedEffect(Unit) {
        if (!uiState.isEditing)
            currencyViewModel.onCurrencyFormEvent(CurrencyFormEvent.Reset)
    }

    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FormSectionTitle(
                    title = if (uiState.isEditing) "Edit Currency" else "New Currency"
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // NAME
                FormTextField(
                    value = uiState.name,
                    onValueChange = {
                        currencyViewModel.onCurrencyFormEvent(
                            CurrencyFormEvent.OnNameChange(it)
                        )
                    },
                    label = "Currency Name",
                    errorMessage = uiState.errors["name"]
                )

                // ISO CODE
                FormTextField(
                    value = uiState.ISO,
                    onValueChange = {
                        currencyViewModel.onCurrencyFormEvent(
                            CurrencyFormEvent.OnISOChange(it)
                        )
                    },
                    label = "ISO Code",
                    errorMessage = uiState.errors["ISO"]
                )

                // SYMBOL
                FormTextField(
                    value = uiState.symbol,
                    onValueChange = {
                        currencyViewModel.onCurrencyFormEvent(
                            CurrencyFormEvent.OnSymbolChange(it)
                        )
                    },
                    label = "Symbol",
                    errorMessage = uiState.errors["symbol"]
                )

                // EXCHANGE RATE
                FormTextField(
                    value = uiState.exchangeRate?.toString() ?: "",
                    onValueChange = {
                        val parsed = it.toDoubleOrNull() ?: 0.0
                        currencyViewModel.onCurrencyFormEvent(
                            CurrencyFormEvent.OnExchangeRateChange(parsed)
                        )
                    },
                    label = "Exchange Rate",
                    errorMessage = uiState.errors["exchangeRate"]
                )

                // IS MAIN CURRENCY
                FormSwitch(
                    label = "Is Main Currency?",
                    checked = uiState.isMainCurrency,
                    onCheckedChange = {
                        currencyViewModel.onCurrencyFormEvent(
                            CurrencyFormEvent.OnMainCurrencyChange(it)
                        )
                    }
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormButton(
                    text = if (uiState.isEditing) "Update" else "Create",
                    onClick = { currencyViewModel.onCurrencyFormEvent(CurrencyFormEvent.Submit) },
                    primary = true,
                    enable = !uiState.isSaving
                )
                //NAVIGATION STATE
                var isNavigating by remember { mutableStateOf(false) }

                FormButton(
                    text = "Cancel",
                    onClick = {
                        if (!isNavigating) {
                            isNavigating = true
                            navController.popBackStack()
                        }
                    },
                    primary = false,
                    enable = !isNavigating
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun CurrencyFormScreenPreview() {
    MaterialTheme {
        CurrencyFormScreen(
            navController = rememberNavController()
        )
    }
}
