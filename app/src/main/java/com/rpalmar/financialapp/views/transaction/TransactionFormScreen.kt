package com.rpalmar.financialapp.views.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.views.transaction.data.TransactionFormEvent
import com.rpalmar.financialapp.views.transaction.data.TransactionUiState
import com.rpalmar.financialapp.views.transaction.data.TransactionViewModel
import com.rpalmar.financialapp.views.ui.componentes.BaseTextField
import com.rpalmar.financialapp.views.ui.componentes.FormNavigatorButtonSection
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.SimpleSelector
import com.rpalmar.financialapp.views.ui.componentes.TitleSectionCard
import com.rpalmar.financialapp.views.ui.theme.Blue
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionFormScreen(
    transactionType: TransactionType,
    accountId: Long,
    onBackPressed: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactionUiState by viewModel.transactionUIState.collectAsState()

    LaunchedEffect(key1 = accountId) {
        viewModel.loadInitialData(accountId)
    }

    fun handleSaveTransaction() {
        // TODO: Implement save logic
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
                    onConfirm = { handleSaveTransaction() },
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
                    itemList = transactionUiState.accounts,
                    selectedItem = transactionUiState.sourceAccount,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnSourceAccountChange(it)) }
                )
                BaseTextField(
                    value = transactionUiState.amount,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                    label = "Amount",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }
            TransactionType.TRANSFER -> {
                SimpleSelector(
                    placeholder = "Source Account",
                    itemList = transactionUiState.accounts,
                    selectedItem = transactionUiState.sourceAccount,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnSourceAccountChange(it)) }
                )
                SimpleSelector(
                    placeholder = "Destination Account",
                    itemList = transactionUiState.accounts.filter { it.id != transactionUiState.sourceAccount?.id },
                    selectedItem = transactionUiState.destinationAccount,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDestinationAccountChange(it)) }
                )
                BaseTextField(
                    value = transactionUiState.amount,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                    label = "Amount",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }
            TransactionType.ADJUSTMENT -> {
                val realAmountValue = transactionUiState.amount.toDoubleOrNull() ?: 0.0
                val currentBalance = transactionUiState.sourceAccount?.balance ?: 0.0
                val adjustment = realAmountValue - currentBalance
                val adjustmentFormatted = numberFormat.format(adjustment)

                SimpleSelector(
                    placeholder = "Account to Adjust",
                    itemList = transactionUiState.accounts,
                    selectedItem = transactionUiState.sourceAccount,
                    itemLabel = { it.name },
                    itemDetail = {"${it.balance} ${it.currency.symbol}"},
                    onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnSourceAccountChange(it)) }
                )
                BaseTextField(
                    value = transactionUiState.amount,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                    label = "Real Amount",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
                BaseTextField(
                    value = "Adjustment: $adjustmentFormatted",
                    onValueChange = { },
                    label = "Calculated Adjustment",
                    enabled = false
                )
            }
        }
        BaseTextField(
            value = transactionUiState.description,
            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDescriptionChange(it)) },
            label = "Description"
        )
    }
}




