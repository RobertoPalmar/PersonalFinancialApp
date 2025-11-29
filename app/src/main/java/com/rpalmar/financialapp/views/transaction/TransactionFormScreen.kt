package com.rpalmar.financialapp.views.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.views.account.data.AccountFormEvent
import com.rpalmar.financialapp.views.transaction.data.TransactionFormEvent
import com.rpalmar.financialapp.views.transaction.data.TransactionViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.theme.Black

@Composable
fun TransactionFormScreen(
    transactionType: TransactionType,
    navController: NavHostController,
    transactionViewModel: TransactionViewModel
) {
    val context = LocalContext.current;
    val uiState by transactionViewModel.transactionUIState.collectAsState()

    //HANDLE TRANSACTION CREATION EVENTS
    LaunchedEffect(true) {
        transactionViewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Success -> navController.popBackStack();
                is UIEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //HANDLE CLEAN TRANSACTION FORM
    LaunchedEffect(Unit) {
        if (!uiState.isEditing)
            transactionViewModel.onTransactionFormEvent(TransactionFormEvent.Reset)
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
                FormSectionTitle("New Transaction")
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                when (transactionType) {
                    TransactionType.INCOME, TransactionType.EXPENSE -> {
                        FormDropdown(
                            label = "Source Account",
                            items = uiState.transactionSources,
                            selectedItem = uiState.originSource,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                            itemLabel = { it.name },
                            itemDetail = { "${it.balance} ${it.currency.symbol}" },
                        )
                        FormDoubleField(
                            label = "Amount",
                            value = uiState.amount,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                        )
                        FormDropdown(
                            label = "Category",
                            items = uiState.categories,
                            selectedItem = uiState.category,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnCategoryChange(it)) },
                            itemLabel = { it.name }
                        )
                    }

                    TransactionType.TRANSFER -> {
                        FormDropdown(
                            label = "Source Account",
                            items = uiState.transactionSources,
                            selectedItem = uiState.originSource,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                            itemLabel = { it.name },
                            itemDetail = { "${it.balance} ${it.currency.symbol}" },
                        )
                        FormDropdown(
                            label = "Destination Account",
                            items = uiState.transactionSources.filter { it.id != uiState.originSource?.id },
                            selectedItem = uiState.destinationSource,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                            itemLabel = { it.name },
                            itemDetail = { "${it.balance} ${it.currency.symbol}" },
                        )
                        FormDoubleField(
                            label = "Amount",
                            value = uiState.amount,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                        )
                        FormDoubleField(
                            label = "Exchange Rate",
                            value = uiState.exchangeRate,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnExchangeRateChange(it)) },
                        )
                    }

                    TransactionType.ADJUSTMENT -> {
                        val adjustmentFormatted = uiState.adjustmentAmount ?: 0.0
                        FormDropdown(
                            label = "Account to Adjust",
                            items = uiState.transactionSources,
                            selectedItem = uiState.originSource,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                            itemLabel = { it.name },
                            itemDetail = { "${it.balance} ${it.currency.symbol}" },
                            enabled = uiState.originSource == null
                        )
                        FormDoubleField(
                            label = "Real Amount",
                            value = uiState.amount,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                        )
                        FormDoubleField(
                            label = "Calculated Adjustment",
                            value = adjustmentFormatted,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                            enabled = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormButton(
                    text = if (uiState.isEditing) "Update" else "Create",
                    onClick = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.Submit(transactionType)) },
                    primary = true,
                    enable = !uiState.isSaving
                )
                FormButton(
                    text = "Cancel",
                    onClick = { navController.popBackStack() },
                    primary = false
                )
            }
        }
    }
}