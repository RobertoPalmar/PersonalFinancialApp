package com.rpalmar.financialapp.views.account

import com.rpalmar.financialapp.views.ui.componentes.BaseTextField
import com.rpalmar.financialapp.views.ui.componentes.ColorPicker
import com.rpalmar.financialapp.views.ui.componentes.IconPicker
import androidx.compose.ui.graphics.toArgb
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rpalmar.financialapp.models.ButtonType
import com.rpalmar.financialapp.views.account.data.AccountFormEvent
import com.rpalmar.financialapp.views.account.data.AccountFormState
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.SimpleButton
import com.rpalmar.financialapp.views.ui.componentes.SimpleSelector
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.White

@Composable
fun AccountFormScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current

    //ACCOUNT STATE DATA
    val accountFormState = viewModel.accountFormState.collectAsState()

    //HANDLE CLEAN ACCOUNT FORM
    LaunchedEffect(Unit) {
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
            TitleSection(
                title = "Create Account",
                color = Blue
            )
            AccountFormSection(
                accountViewModel = viewModel,
                accountFormState = accountFormState.value
            )
            Spacer(modifier = Modifier.weight(1f))
            NavigatorButtonSection(
                accountViewModel = viewModel,
                onBackPressed = onBackPressed,
                accountFormState = accountFormState.value
            )
        }
    }
}

@Composable
fun TitleSection(
    title: String,
    color: Color,
    icon: ImageVector? = null
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(color),
        modifier = Modifier
            .height(55.dp)
            .fillMaxWidth(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        )
    ) {
        Row(
            modifier = Modifier.padding(15.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = title
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountFormSection(
    accountViewModel: AccountViewModel,
    accountFormState: AccountFormState
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(0.dp, 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        BaseTextField(
            value = accountFormState.accountName,
            onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnAccountNameChange(it)) },
            label = "Account Name"
        )
        BaseTextField(
            value = accountFormState.description,
            onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnDescriptionChange(it)) },
            label = "Account Description"
        )
        SimpleSelector(
            placeholder = "Select a Currency",
            itemList = accountFormState.currencyList,
            itemLabel = { "${it.name} (${it.symbol})" },
            onItemSelected = { currency -> accountViewModel.onAccountFormEvent(AccountFormEvent.OnCurrencyChange(currency)) }
        )
        BaseTextField(
            value = accountFormState.initBalance,
            onValueChange = { accountViewModel.onAccountFormEvent(AccountFormEvent.OnInitBalanceChange(it)) },
            label = "Initial Balance",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        ColorPicker(
            onColorSelected = { color ->
                accountViewModel.onAccountFormEvent(
                    AccountFormEvent.OnColorChange("#${Integer.toHexString(color.toArgb())}")
                )
            }
        )
        IconPicker(
            onIconSelected = { icon ->
                accountViewModel.onAccountFormEvent(AccountFormEvent.OnIconChange(icon))
            }
        )
    }

}

@Composable
fun NavigatorButtonSection(
    accountViewModel: AccountViewModel,
    accountFormState: AccountFormState,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
    ) {
        SimpleButton(
            text = "Go Back",
            color = DarkGrey,
            onClick = { onBackPressed() },
            type = ButtonType.PRIMARY,
            modifier = Modifier.weight(1f),
            enable = !accountFormState.isSaving
        )
        Spacer(modifier = Modifier.width(10.dp))
        SimpleButton(
            modifier = Modifier.weight(1f),
            text = "Create",
            color = Blue,
            onClick = { accountViewModel.onAccountFormEvent(AccountFormEvent.Submit) },
            type = ButtonType.PRIMARY,
            enable = !accountFormState.isSaving,
            content = if (accountFormState.isSaving) {
                {
                    CircularProgressIndicator(
                        color = White,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else null
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ExampleAccountFormPreview() {
    FinancialTheme(
        darkTheme = false
    ) {
        AccountFormScreen(
            onBackPressed = {}
        )
    }
}