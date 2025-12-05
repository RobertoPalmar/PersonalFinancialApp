package com.rpalmar.financialapp.views.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.views.transaction.data.TransactionFormEvent
import com.rpalmar.financialapp.views.transaction.data.TransactionViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionFormScreen(
    navController: NavHostController,
    transactionViewModel: TransactionViewModel,
    transactionType: TransactionType,
    sourceAccountID:Long? = null
) {
    val context = LocalContext.current
    val uiState by transactionViewModel.transactionUIState.collectAsState()

    //HANDLE TRANSACTION CREATION EVENTS
    LaunchedEffect(true) {
        transactionViewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Success -> navController.popBackStack()
                is UIEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //HANDLE CLEAN TRANSACTION FORM
    LaunchedEffect(Unit) {
        if (!uiState.isEditing) {
            transactionViewModel.setTransactionType(transactionType)
            transactionViewModel.onTransactionFormEvent(TransactionFormEvent.Reset)
        }
    }

    LaunchedEffect(true) {
        transactionViewModel.loadInitialData(sourceAccountID);
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

                TransactionTypeChip(transactionType)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FormTextField(
                    label = "Description",
                    value = uiState.description,
                    onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDescriptionChange(it)) },
                )

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dateValue = try { dateFormat.parse(uiState.transactionDate)} catch (e: Exception) {Date()}

                FormDateField(
                    label = "Date",
                    value = dateValue,
                    onValueChange = {
                        val newDate = dateFormat.format(it)
                        transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDateChange(newDate))
                    }
                )
                when (transactionType) {
                    TransactionType.INCOME, TransactionType.EXPENSE -> {
                        FormDropdown(
                            label = "Source Account",
                            items = uiState.transactionSources,
                            selectedItem = uiState.originSource,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnOriginSourceChange(it)) },
                            itemLabel = { it.name },
                            itemDetail = { "${it.balance} ${it.currency.symbol}" },
                            enabled = sourceAccountID == null
                        )
                        FormDoubleField(
                            label = "Amount",
                            value = uiState.amount,
                            prefix = uiState.originSource?.currency?.symbol,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                        )
                        FormDropdown(
                            label = "Category",
                            items = uiState.categories,
                            selectedItem = uiState.category,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnCategoryChange(it)) },
                            itemLabel = { it.name },
                            itemIcon = { it.style.uiIcon }
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
                            enabled = sourceAccountID == null
                        )
                        FormDropdown(
                            label = "Destination Account",
                            items = uiState.transactionSources.filter { it.id != uiState.originSource?.id },
                            selectedItem = uiState.destinationSource,
                            onItemSelected = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDestinationSourceChange(it)) },
                            itemLabel = { it.name },
                            itemDetail = { "${it.balance} ${it.currency.symbol}" },
                        )
                        FormDoubleField(
                            label = "Origin Amount",
                            prefix = uiState.originSource?.currency?.symbol,
                            value = uiState.amount,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                        )
                        FormDoubleField(
                            label = "Destination Amount",
                            prefix = uiState.destinationSource?.currency?.symbol,
                            value = uiState.destinationAmount,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnDestinationAmountChange(it)) },
                        )
                        FormDoubleField(
                            label = "Exchange Rate",
                            value = uiState.exchangeRate,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnExchangeRateChange(it)) },
                        )
                    }

                    TransactionType.ADJUSTMENT -> {
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
                            prefix = uiState.originSource?.currency?.symbol,
                            value = uiState.amount,
                            onValueChange = { transactionViewModel.onTransactionFormEvent(TransactionFormEvent.OnAmountChange(it)) },
                        )
                        FormDoubleField(
                            label = "Calculated Adjustment",
                            prefix = uiState.originSource?.currency?.symbol,
                            value = uiState.adjustmentAmount,
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

@Composable
fun TransactionTypeChip(
    type: TransactionType,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = type.resolveColor().copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = type.resolveColor(),
                    shape = CircleShape
                )
        )

        Icon(
            imageVector = type.resolveIcon(),
            contentDescription = type.resolveLabel(),
            tint = type.resolveColor(),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(16.dp)
        )

        Text(
            text = type.resolveLabel(),
            color = type.resolveColor(),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun TransactionDataCardPreview() {

    MaterialTheme {
        TransactionFormScreen(
            transactionType = TransactionType.INCOME,
            navController = NavHostController(LocalContext.current),
            transactionViewModel = hiltViewModel()
        )
    }
}
