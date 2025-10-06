package com.rpalmar.financialapp.views.account

import android.annotation.SuppressLint
import com.rpalmar.financialapp.views.ui.componentes.BaseTextField
import com.rpalmar.financialapp.views.ui.componentes.ColorPicker
import com.rpalmar.financialapp.views.ui.componentes.IconPicker
import androidx.compose.ui.graphics.toArgb
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.views.account.data.AccountFormEvent
import com.rpalmar.financialapp.views.account.data.AccountUIState
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.componentes.FormNavigatorButtonSection
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.SimpleSelector
import com.rpalmar.financialapp.views.ui.componentes.TitleSectionCard
import com.rpalmar.financialapp.views.ui.theme.Blue

@Composable
fun AccountFormScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit
) {
    //SET UP VIEW MODEL
    val backStackEntry = remember(navController.currentBackStackEntry) { navController.getBackStackEntry("account_flow") }
    val viewModel: AccountViewModel = hiltViewModel(backStackEntry)

    val context = LocalContext.current

    //ACCOUNT STATE DATA
    val accountUIState = viewModel.accountUIState.collectAsState()

    //HANDLE CLEAN ACCOUNT FORM
    LaunchedEffect(Unit) {
        if (!accountUIState.value.isEditing)
            viewModel.onAccountFormEvent(AccountFormEvent.Reset)
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

    //LOAD CURRENCY DATA
    LaunchedEffect(true) {
        viewModel.loadAuxData()
    }

    MainLayout {
        Column(
            modifier = Modifier.fillMaxSize(1f)
        ) {
            TitleSectionCard(
                title = if (accountUIState.value.isEditing) "Edit Account" else "Create Account",
                backgroundColor = Blue
            )
            AccountFormSection(
                accountViewModel = viewModel,
                accountUIState = accountUIState.value
            )
            Spacer(modifier = Modifier.weight(1f))
            FormNavigatorButtonSection(
                onBackPressed = onBackPressed,
                isEditing = accountUIState.value.isEditing,
                isSaving = accountUIState.value.isSaving,
                onConfirm = { viewModel.onAccountFormEvent(AccountFormEvent.Submit) }
            )
        }
    }
}



@SuppressLint("DefaultLocale")
@Composable
fun AccountFormSection(
    accountViewModel: AccountViewModel,
    accountUIState: AccountUIState
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(0.dp, 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        BaseTextField(
            value = accountUIState.accountName,
            onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnAccountNameChange(it)) },
            label = "Account Name"
        )
        BaseTextField(
            value = accountUIState.description,
            onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnDescriptionChange(it)) },
            label = "Account Description"
        )
        SimpleSelector(
            placeholder = "Select a Currency",
            itemList = accountUIState.currencyList,
            selectedItem = accountUIState.currency,
            itemLabel = { "${it.symbol} - ${it.name}" },
            itemDetail = { "ER: ${String.format("%.5f",it.exchangeRate)}"},
            onItemSelected = { currency -> accountViewModel.onAccountFormEvent(AccountFormEvent.OnCurrencyChange(currency)) }
        )
            BaseTextField(
                value = accountUIState.balance,
                onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnBalanceChange(it))},
                label = "Initial Balance",
                leadingText = accountUIState.currency?.symbol,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        ColorPicker(
            initialColor = accountUIState.color,
            onColorSelected = { color ->
                accountViewModel.onAccountFormEvent(
                    AccountFormEvent.OnColorChange("#${Integer.toHexString(color.toArgb())}")
                )
            }
        )
        IconPicker(
            initialIcon = accountUIState.icon,
            onIconSelected = { icon ->
                accountViewModel.onAccountFormEvent(AccountFormEvent.OnIconChange(icon))
            }
        )
    }

}
