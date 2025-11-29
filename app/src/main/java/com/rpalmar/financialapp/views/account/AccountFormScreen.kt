package com.rpalmar.financialapp.views.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.views.account.data.AccountFormEvent
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.theme.Black
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.BellSolid
import compose.icons.lineawesomeicons.CashRegisterSolid
import compose.icons.lineawesomeicons.ChartBarSolid
import compose.icons.lineawesomeicons.CheckCircleSolid
import compose.icons.lineawesomeicons.CreditCardSolid
import compose.icons.lineawesomeicons.EnvelopeSolid
import compose.icons.lineawesomeicons.HeartSolid
import compose.icons.lineawesomeicons.HomeSolid
import compose.icons.lineawesomeicons.InfoCircleSolid
import compose.icons.lineawesomeicons.ShoppingBagSolid
import compose.icons.lineawesomeicons.StarSolid
import compose.icons.lineawesomeicons.ToolsSolid

@Composable
fun AccountFormScreen(
    navController: NavHostController,
    accountViewModel: AccountViewModel
) {
    val context = LocalContext.current;
    val uiState by accountViewModel.accountUIState.collectAsState()
    val previewAccount = uiState.toTemporalAccount()

    //HANDLE ACCOUNT CREATION EVENTS
    LaunchedEffect(true) {
        accountViewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Success -> navController.popBackStack();
                is UIEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //HANDLE CLEAN TRANSACTION FORM
    LaunchedEffect(Unit) {
        if(!uiState.isEditing)
            accountViewModel.onAccountFormEvent(AccountFormEvent.Reset)
    }

    //LOAD AUXILIAR DATA
    LaunchedEffect(true) {
        accountViewModel.loadAuxData()
    }

    //COLOR LIST
    val colorList = listOf(
        Color(0xFFE57373), Color(0xFFF06292), Color(0xFFBA68C8),
        Color(0xFF9575CD), Color(0xFF7986CB), Color(0xFF64B5F6),
        Color(0xFF4FC3F7), Color(0xFF4DD0E1), Color(0xFF4DB6AC),
        Color(0xFF81C784), Color(0xFFAED581), Color(0xFFFFD54F)
    )

    //ICON LIST
    val iconList = listOf(
        LineAwesomeIcons.HomeSolid, LineAwesomeIcons.StarSolid,
        LineAwesomeIcons.ChartBarSolid, LineAwesomeIcons.CashRegisterSolid,
        LineAwesomeIcons.CreditCardSolid, LineAwesomeIcons.ShoppingBagSolid,
        LineAwesomeIcons.HeartSolid, LineAwesomeIcons.BellSolid,
        LineAwesomeIcons.EnvelopeSolid, LineAwesomeIcons.CheckCircleSolid,
        LineAwesomeIcons.InfoCircleSolid, LineAwesomeIcons.ToolsSolid
    )

    if(uiState.isLoading){
        LoadingScreen()
    }
    else{

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
                FormSectionTitle("New Account")

                AccountDataCard(
                    account = previewAccount,
                    mainCurrency = uiState.mainCurrency!!
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FormTextField(
                    value = uiState.accountName,
                    onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnAccountNameChange(it)) },
                    label = "Account Name"
                )

                FormTextField(
                    value = uiState.description,
                    onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnDescriptionChange(it)) },
                    label = "Description"
                )

                FormDropdown(
                    label = "Currency",
                    items = uiState.currencyList,
                    selectedItem = uiState.currency,
                    onItemSelected = { currency -> accountViewModel.onAccountFormEvent(AccountFormEvent.OnCurrencyChange(currency)) },
                    itemLabel = { "${it.symbol} - ${it.name}" },
                    itemDetail = { "ER: ${String.format("%.5f",it.exchangeRate)}"},
                )

                FormDoubleField(
                    value = uiState.balance,
                    onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnBalanceChange(it))},
                    label = "Balance"
                )

                FormColorPicker(
                    label = "Color de Cuenta",
                    colors = colorList,
                    selectedColor = uiState.color,
                    onColorSelected = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnColorChange(it))},
                )

                FormIconPicker(
                    label = "Icono de Cuenta",
                    icons = iconList,
                    selectedIcon = uiState.icon,
                    onIconSelected = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnIconChange(it)) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FormButton(
                    text = if(uiState.isEditing) "Update" else "Create",
                    onClick = {accountViewModel.onAccountFormEvent(AccountFormEvent.Submit)},
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