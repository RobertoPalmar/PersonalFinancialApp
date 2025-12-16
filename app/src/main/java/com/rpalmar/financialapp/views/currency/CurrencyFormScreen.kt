package com.rpalmar.financialapp.views.currency

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rpalmar.financialapp.providers.database.repositories.SharedPreferencesRepository
import com.rpalmar.financialapp.views.currency.data.CurrencyFormEvent
import com.rpalmar.financialapp.views.currency.data.CurrencyViewModel
import com.rpalmar.financialapp.views.navigation.LocalMainCurrency
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.components.*
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.White

@Composable
fun CurrencyFormScreen(
    navController: NavController,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by currencyViewModel.currencyUIState.collectAsState()
    val mainCurrency = LocalMainCurrency.current ?: return

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

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // CURRENCY SEARCH BUTTON
                var showCurrencyDialog by remember { mutableStateOf(false) }
                
                FormButton(
                    text = "Search Currency Template",
                    onClick = { showCurrencyDialog = true },
                    primary = false
                )
                
                if (showCurrencyDialog) {
                    CurrencySearchDialog(
                        onDismiss = { showCurrencyDialog = false },
                        onCurrencySelected = { currencyInfo ->
                            currencyViewModel.onCurrencyFormEvent(
                                CurrencyFormEvent.OnNameChange(currencyInfo.name)
                            )
                            currencyViewModel.onCurrencyFormEvent(
                                CurrencyFormEvent.OnISOChange(currencyInfo.isoCode)
                            )
                            currencyViewModel.onCurrencyFormEvent(
                                CurrencyFormEvent.OnSymbolChange(currencyInfo.symbol)
                            )
                        }
                    )
                }

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

                // EXCHANGE RATE CALCULATOR
                Text(
                    text = "Exchange Rate Calculator",
                    style = MaterialTheme.typography.titleSmall,
                    color = if (uiState.isMainCurrency) White.copy(alpha = 0.5f) else White,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                
                var calculatorCurrent by remember { mutableStateOf("1") }
                var calculatorBase by remember { mutableStateOf("") }
                
                // Pre-populate calculatorBase when editing an existing currency
                LaunchedEffect(uiState.exchangeRate, uiState.isEditing) {
                    if (uiState.isEditing && uiState.exchangeRate != null && uiState.exchangeRate!! > 0) {
                        val currentVal = calculatorCurrent.toDoubleOrNull() ?: 1.0
                        calculatorBase = (uiState.exchangeRate!! * currentVal).toString()
                    }
                }
                
                // Set exchange rate to 1.0 when isMainCurrency is toggled on
                LaunchedEffect(uiState.isMainCurrency) {
                    if (uiState.isMainCurrency) {
                        currencyViewModel.onCurrencyFormEvent(
                            CurrencyFormEvent.OnExchangeRateChange(1.0)
                        )
                        calculatorCurrent = "1"
                        calculatorBase = "1"
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormTextField(
                        value = calculatorCurrent,
                        onValueChange = { value ->
                            if (!uiState.isMainCurrency) {
                                calculatorCurrent = value
                                val currentValue = value.toDoubleOrNull()
                                val baseValue = calculatorBase.toDoubleOrNull()
                                if (currentValue != null && currentValue > 0 && baseValue != null) {
                                    val rate = baseValue / currentValue
                                    currencyViewModel.onCurrencyFormEvent(
                                        CurrencyFormEvent.OnExchangeRateChange(rate)
                                    )
                                }
                            }
                        },
                        label = uiState.ISO.ifEmpty { "Current Currency" },
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isMainCurrency,
                        prefix = uiState.symbol
                    )
                    
                    Text(
                        text = "=",
                        color = if (uiState.isMainCurrency) White.copy(alpha = 0.5f) else White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    FormTextField(
                        value = calculatorBase,
                        onValueChange = { value ->
                            if (!uiState.isMainCurrency) {
                                calculatorBase = value
                                val currentValue = calculatorCurrent.toDoubleOrNull()
                                val baseValue = value.toDoubleOrNull()
                                if (currentValue != null && currentValue > 0 && baseValue != null) {
                                    val rate = baseValue / currentValue
                                    currencyViewModel.onCurrencyFormEvent(
                                        CurrencyFormEvent.OnExchangeRateChange(rate)
                                    )
                                }
                            }
                        },
                        label = "Main Currency",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isMainCurrency,
                        prefix = mainCurrency.symbol
                    )
                }

                // EXCHANGE RATE (READ ONLY)
                FormTextField(
                    value = uiState.exchangeRate?.toString() ?: "",
                    onValueChange = { },
                    label = "Exchange Rate (Calculated)",
                    errorMessage = uiState.errors["exchangeRate"],
                    enabled = false
                )

                // IS MAIN CURRENCY
                var showDeselectMainWarning by remember { mutableStateOf(false) }
                var pdMainCurrencyChange by remember { mutableStateOf<Boolean?>(null) }
                
                FormSwitch(
                    label = "Is Main Currency?",
                    checked = uiState.isMainCurrency,
                    onCheckedChange = { isChecked ->
                        // If we are deselecting main currency (changing from true to false)
                        if (!isChecked && uiState.isMainCurrency) {
                            if (currencyViewModel.shouldShowWarning(SharedPreferencesRepository.WARNING_DESELECTING_MAIN_CURRENCY)) {
                                pdMainCurrencyChange = isChecked
                                showDeselectMainWarning = true
                            } else {
                                currencyViewModel.onCurrencyFormEvent(
                                    CurrencyFormEvent.OnMainCurrencyChange(isChecked)
                                )
                            }
                        } else {
                            currencyViewModel.onCurrencyFormEvent(
                                CurrencyFormEvent.OnMainCurrencyChange(isChecked)
                            )
                        }
                    }
                )
                
                if (showDeselectMainWarning) {
                    WarningDialog(
                        title = "Main Currency Change",
                        message = "Another currency will be automatically selected as main. Are you sure you want to proceed?",
                        onDismiss = { 
                            showDeselectMainWarning = false 
                            pdMainCurrencyChange = null
                        },
                        onConfirm = {
                             pdMainCurrencyChange?.let {
                                 currencyViewModel.onCurrencyFormEvent(
                                     CurrencyFormEvent.OnMainCurrencyChange(it)
                                 )
                             }
                        },
                        confirmText = "Proceed",
                        onDontShowAgainChanged = { dontShow ->
                             currencyViewModel.setWarningPreference(SharedPreferencesRepository.WARNING_DESELECTING_MAIN_CURRENCY, !dontShow)
                        }
                    )
                }
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
