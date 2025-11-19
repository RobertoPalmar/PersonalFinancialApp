package com.rpalmar.financialapp.views.transaction

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.views.transaction.data.TransactionFormEvent
import com.rpalmar.financialapp.views.transaction.data.TransactionUiState
import com.rpalmar.financialapp.views.transaction.data.TransactionViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.componentes.BaseTextField
import com.rpalmar.financialapp.views.ui.componentes.DatePickerDialogComponent
import com.rpalmar.financialapp.views.ui.componentes.FormNavigatorButtonSection
import com.rpalmar.financialapp.views.ui.componentes.refactor.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.SimpleSelector
import com.rpalmar.financialapp.views.ui.componentes.TitleSectionCard
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionFormScreen(
    transactionType: TransactionType,
    onBackPressed: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    //TRANSACTION STATE DATA
    val transactionUiState by viewModel.transactionUIState.collectAsState()

    //HANDLE CLEAN TRANSACTION FORM
    LaunchedEffect(Unit) {
        if(!transactionUiState.isEditing)
            viewModel.onTransactionFormEvent(TransactionFormEvent.Reset)
    }

    //HANDLE ACCOUNT CREATION EVENTS
    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Success ->
                    onBackPressed();
                is UIEvent.ShowError ->
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    MainLayout {
        if (transactionUiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxSize(1f)
            ) {
                TitleSectionCard(
                    title = if (false) "Edit Transaction" else "Create Transaction",
                    subtitle = transactionType.name,
                    backgroundColor = Blue,
                )
                TransactionFormSection(
                    transactionType = transactionType,
                    transactionUiState = transactionUiState,
                    transactionViewModel = viewModel
                )
                Spacer(modifier = Modifier.weight(1f))
                FormNavigatorButtonSection(
                    onConfirm = { viewModel.onTransactionFormEvent(TransactionFormEvent.Submit(transactionType)) },
                    onBackPressed = onBackPressed,
                    isEditing = false,
                    isSaving = false
                )
            }
        }
    }
}

@Composable
fun TransactionFormSection(
    transactionUiState: TransactionUiState,
    transactionViewModel: TransactionViewModel,
    transactionType: TransactionType,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(0.dp, 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        when (transactionType) {
            TransactionType.INCOME, TransactionType.EXPENSE -> {
                SimpleSelector(
                    placeholder = "Source Account",
                    itemList = transactionUiState.transactionSources,
                    selectedItem = transactionUiState.originSource,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                    errorMessage =  if (transactionUiState.errors.containsKey("sourceAccount")) transactionUiState.errors["sourceAccount"] else null,
                    enabled = transactionUiState.originSource == null
                )
                BaseTextField(
                    value = transactionUiState.amount,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                    label = "Amount",
                    leadingText = transactionUiState.originSource?.currency?.symbol,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage =  if (transactionUiState.errors.containsKey("amount")) transactionUiState.errors["amount"] else null
                )
                SimpleSelector(
                    placeholder = "Category",
                    itemList = transactionUiState.categories,
                    selectedItem = transactionUiState.category,
                    itemLabel = { it.name },
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnCategoryChange(it)) },
                    errorMessage =  if (transactionUiState.errors.containsKey("category")) transactionUiState.errors["category"] else null
                )
            }
            TransactionType.TRANSFER -> {
                SimpleSelector(
                    placeholder = "Source Account",
                    itemList = transactionUiState.transactionSources,
                    selectedItem = transactionUiState.originSource,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                    errorMessage =  if (transactionUiState.errors.containsKey("sourceAccount")) transactionUiState.errors["sourceAccount"] else null,
                    enabled = transactionUiState.originSource == null
                )
                SimpleSelector(
                    placeholder = "Destination Account",
                    itemList = transactionUiState.transactionSources.filter { it.id != transactionUiState.originSource?.id },
                    selectedItem = transactionUiState.destinationSource,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDestinationSourceChange(it)) },
                    errorMessage =  if (transactionUiState.errors.containsKey("destinationAccount")) transactionUiState.errors["destinationAccount"] else null
                )
                BaseTextField(
                    value = transactionUiState.amount,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                    label = "Origin Amount",
                    leadingText = transactionUiState.originSource?.currency?.symbol,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage = if (transactionUiState.errors.containsKey("amount")) transactionUiState.errors["amount"] else null,
                )
                BaseTextField(
                    value = transactionUiState.destinationAmount,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDestinationAmountChange(it)) },
                    label = "Destination Amount",
                    leadingText = transactionUiState.destinationSource?.currency?.symbol,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
//                BaseTextField(
//                    value = transactionUiState.exchangeRate,
//                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnExchangeRateChange(it)) },
//                    label = "Exchange Rate",
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Number,
//                        imeAction = ImeAction.Next
//                    )
//                )
            }
            TransactionType.ADJUSTMENT -> {
                val adjustmentFormatted = numberFormat.format(transactionUiState.adjustmentAmount ?: 0.0)

                SimpleSelector(
                    placeholder = "Account to Adjust",
                    itemList = transactionUiState.transactionSources,
                    selectedItem = transactionUiState.originSource,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                    errorMessage = if (transactionUiState.errors.containsKey("sourceAccount")) transactionUiState.errors["sourceAccount"] else null,
                    enabled = transactionUiState.originSource == null
                )
                BaseTextField(
                    value = transactionUiState.amount,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                    label = "Real Amount",
                    leadingText = transactionUiState.originSource?.currency?.symbol,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    errorMessage =  if (transactionUiState.errors.containsKey("amount")) transactionUiState.errors["amount"] else null
                )
                BaseTextField(
                    value = "Adjustment: $adjustmentFormatted",
                    onValueChange = { },
                    label = "Calculated Adjustment",
                    enabled = false
                )
            }
        }
        val showDatePicker = remember { mutableStateOf(false) }

        BaseTextField(
            value = transactionUiState.transactionDate,
            onValueChange = { },
            label = "Transaction Date",
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker.value = true }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.DateRange,
                        contentDescription = "Date Picker"
                    )
                }
            }
        )

        if (showDatePicker.value) {
            DatePickerDialogComponent(
                onDateSelected = {
                    transactionViewModel.onTransactionFormEvent(
                        TransactionFormEvent.OnDateChange(it)
                    )
                    showDatePicker.value = false
                },
                onDismiss = { showDatePicker.value = false }
            )
        }

        BaseTextField(
            value = transactionUiState.description,
            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDescriptionChange(it)) },
            label = "Description",
            errorMessage =  if (transactionUiState.errors.containsKey("description")) transactionUiState.errors["description"] else null
        )
    }
}




