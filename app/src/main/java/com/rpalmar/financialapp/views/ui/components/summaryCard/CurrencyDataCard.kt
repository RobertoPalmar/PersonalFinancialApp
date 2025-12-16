package com.rpalmar.financialapp.views.ui.components.summaryCard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.CurrencyDeletionValidation
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.SharedPreferencesRepository
import com.rpalmar.financialapp.views.currency.data.CurrencyViewModel
import com.rpalmar.financialapp.views.navigation.LocalMainCurrency
import com.rpalmar.financialapp.views.ui.components.DefaultIcon
import com.rpalmar.financialapp.views.ui.components.ModalDialog
import com.rpalmar.financialapp.views.ui.components.PreferenceStarIcon
import com.rpalmar.financialapp.views.ui.components.WarningDialog
import com.rpalmar.financialapp.views.ui.components.formatAmount
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.LightGrey
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.Octicons
import compose.icons.octicons.Pencil24
import compose.icons.octicons.Trash24
import kotlinx.coroutines.launch

@Composable
fun CurrencyDataCard(
    currency: CurrencyDomain,
    currencyViewModel: CurrencyViewModel,
    onBackNavigation: () -> Unit,
    onEditNavigation: () -> Unit,
) {
    val mainCurrency = LocalMainCurrency.current ?: return
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showWarningDialog by remember { mutableStateOf<CurrencyDeletionValidation?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    fun onDeleteCurrencyClick(){
        scope.launch {
            val validation = currencyViewModel.validateCurrencyDeletion(currency.id)
            
            when (validation) {
                is CurrencyDeletionValidation.CanDelete -> {
                    currencyViewModel.handleDeleteCurrency(currency.id)
                    onBackNavigation()
                }
                is CurrencyDeletionValidation.HasAccounts -> {
                    if (currencyViewModel.shouldShowWarning(SharedPreferencesRepository.WARNING_CURRENCY_HAS_ACCOUNTS)) {
                        showWarningDialog = validation
                    } else {
                        Toast.makeText(context, "Cannot delete: Currency has associated accounts", Toast.LENGTH_SHORT).show()
                    }
                }
                is CurrencyDeletionValidation.LastActiveCurrency -> {
                    if (currencyViewModel.shouldShowWarning(SharedPreferencesRepository.WARNING_LAST_ACTIVE_CURRENCY)) {
                        showWarningDialog = validation
                    } else {
                        Toast.makeText(context, "Cannot delete: Must have at least one active currency", Toast.LENGTH_SHORT).show()
                    }
                }
                is CurrencyDeletionValidation.IsMainCurrency -> {
                    if (currencyViewModel.shouldShowWarning(SharedPreferencesRepository.WARNING_DELETING_MAIN_CURRENCY)) {
                        showWarningDialog = validation
                    } else {
                        // Directly delete if warning is suppressed
                        currencyViewModel.handleDeleteCurrency(currency.id)
                        onBackNavigation() // Navigate back after direct delete
                    }
                }
            }
        }
    }

    fun onEditCurrencyClick(){
        currencyViewModel.handleUpdateCurrencyForm(currency)
        onEditNavigation();
    }

    // WARNING DIALOGS
    if (showWarningDialog != null) {
        when (val validation = showWarningDialog!!) {
            is CurrencyDeletionValidation.HasAccounts -> {
                WarningDialog(
                    title = "Cannot Delete Currency",
                    message = "It is not possible to delete this currency because there are ${validation.count} accounts associated with it.",
                    onDismiss = { showWarningDialog = null },
                    confirmText = "OK",
                    dismissText = "",
                    onDontShowAgainChanged = { dontShow ->
                        currencyViewModel.setWarningPreference(SharedPreferencesRepository.WARNING_CURRENCY_HAS_ACCOUNTS, !dontShow)
                    }
                )
            }
            is CurrencyDeletionValidation.LastActiveCurrency -> {
                WarningDialog(
                    title = "Cannot Delete Currency",
                    message = "Cannot delete the currency, as there must be at least one active currency.",
                    onDismiss = { showWarningDialog = null },
                    confirmText = "OK",
                    dismissText = "",
                    onDontShowAgainChanged = { dontShow ->
                        currencyViewModel.setWarningPreference(SharedPreferencesRepository.WARNING_LAST_ACTIVE_CURRENCY, !dontShow)
                    }
                )
            }
            is CurrencyDeletionValidation.IsMainCurrency -> {
                WarningDialog(
                    title = "Cannot Delete Currency",
                    message = "You cannot delete a currency that is the primary currency. Update another currency to primary and then return to delete this one.",
                    onDismiss = { showWarningDialog = null },
                    confirmText = "OK",
                    dismissText = "",
                    onDontShowAgainChanged = { dontShow ->
                        currencyViewModel.setWarningPreference(SharedPreferencesRepository.WARNING_DELETING_MAIN_CURRENCY, !dontShow)
                    }
                )
            }
            else -> {}
        }
    }

    // CONFIRM DELETE DIALOG (Standard)
    if (showDeleteDialog) {
        ModalDialog(
            title = "Delete Currency",
            message = "Are you sure you want to delete this currency?",
            onAccept = { onDeleteCurrencyClick()},
            onDismiss = { showDeleteDialog = false }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(vertical = 10.dp, horizontal = 15.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGrey),
            shape = RoundedCornerShape(14.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 0.dp)
                        .background(Red.copy(alpha = 0.10f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp, horizontal = 18.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        // HEADER
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //CURRENCY DATA
                            Column {
                                Text(
                                    text = "Currency",
                                    color = LightGrey,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                ) {
                                    Text(
                                        text = currency.name,
                                        color = White,
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    if (currency.mainCurrency) {
                                        Spacer(modifier = Modifier.width(5.dp))
                                        PreferenceStarIcon(
                                            size = 25.dp
                                        )
                                    }
                                }
                            }

                            //CURRENCY ICON
                            DefaultIcon(
                                title = currency.name,
                                textIcon = currency.symbol,
                                color = Red,
                                circleSize = 50.dp,
                                iconSize = 30.dp
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // FOOTER
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {

                                Text(
                                    text = "Exchange Rate",
                                    color = White,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Text(
                                        text = "${formatAmount(currency.exchangeRate, decimals = 5)} ${currency.symbol}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = White,
                                        maxLines = 1,
                                    )
                                    if (!currency.mainCurrency) {
                                        Text(
                                            "x",
                                            modifier = Modifier.padding(horizontal = 5.dp),
                                            style = MaterialTheme.typography.titleSmall,
                                            color = White,
                                            fontWeight = FontWeight.Normal
                                        )
                                        Text(
                                            text = "1${mainCurrency.symbol}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = White,
                                            fontWeight = FontWeight.Normal,
                                            maxLines = 1,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            //ACTION BUTTONS SECTION
                            Row(
                            ) {

                                IconButton(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier.size(35.dp)
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(image = Octicons.Trash24),
                                        contentDescription = "Delete",
                                        tint = White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {onEditCurrencyClick()},
                                    modifier = Modifier.size(35.dp)
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(image = Octicons.Pencil24),
                                        contentDescription = "Edit",
                                        tint = White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun CurrencyDataCardPreview() {

    MaterialTheme {
        CurrencyDataCard(
            currency = MockupProvider.getMockCurrencies()[0],
            currencyViewModel = hiltViewModel(),
            onBackNavigation = {},
            onEditNavigation = {}
        )
    }
}
