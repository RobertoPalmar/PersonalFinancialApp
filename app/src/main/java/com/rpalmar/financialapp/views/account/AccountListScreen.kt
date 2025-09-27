package com.rpalmar.financialapp.views.account

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.views.ui.componentes.SimpleButton
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.views.ui.theme.Grey
import com.rpalmar.financialapp.views.ui.theme.White
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.ui.componentes.SummarySection
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Red

@Composable
fun AccountListScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current

    //ACCOUNT STATE DATA
    val accountFormState = viewModel.accountFormState.collectAsState()

    //LOAD ACCOUNT LIST
    LaunchedEffect(true) {
        viewModel.loadAccountLisData()
    }

    MainLayout {
        SummarySection(
            sectionName = "Accounts",
            totalEntities = accountFormState.value.accountList.size,
            mainSummaryData = "Balance: 1850.15 $",
            mainColor = Blue,
            icon = ImageVector.vectorResource(id = R.drawable.ic_wallet)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBarSection()
        Spacer(modifier = Modifier.height(3.dp))
        ButtonsSection( onNavigateToForm = onNavigateToForm )

        if (accountFormState.value.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(.7f))
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            AccountListSection(
                accountList = accountFormState.value.accountList,
                mainCurrency = accountFormState.value.mainCurrency!!,
            )
        }
    }
}


@Composable
fun SearchBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(48.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search Account") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            shape = RoundedCornerShape(10.dp),
        )
    }
}

@Composable
fun ButtonsSection(
    onNavigateToForm: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        SimpleButton(
            onClick = { onNavigateToForm() },
            icon = ImageVector.vectorResource(R.drawable.ic_plus),
            text = "New",
            color = Blue
        )
    }
}

@Composable
fun AccountListSection(
    accountList: List<AccountDomain>,
    mainCurrency: CurrencyDomain
) {


    val overscrollEffect = rememberOverscrollEffect()

    Text(
        text = "Account List",
        style = MaterialTheme.typography.bodyLarge,
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(1f)
            .overscroll(overscrollEffect),
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 10.dp
        ),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(accountList) { account ->
            AccountItemCard(
                account = account,
                baseCurrency = mainCurrency
            )
        }
    }
}

@SuppressLint("DefaultLocale", "UseKtx")
@Composable
fun AccountItemCard(
    account: AccountDomain,
    baseCurrency: CurrencyDomain
) {
    //FORMAT BALANCE AMOUNT
    val balanceFormated = String.format("%.2f", account.initBalance)
    val balanceInBaseCurrency = String.format("%.2f", account.initBalanceInBaseCurrency)

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedElevation by animateDpAsState(if (isPressed) 8.dp else 4.dp)

    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .height(82.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(15.dp)
        ) {
            //ICON SECTION
            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .weight(0.22f),
                horizontalAlignment = Alignment.Start
            ) {
                CircleIcon(
                    icon = ImageVector.vectorResource(R.drawable.ic_financial),
                    iconColor = Blue,
                    contentDescription = account.name,
                    size = 100.dp
                )
            }
            //ACCOUNT DESCRIPTION
            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .weight(0.6f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = account.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGrey
                )
            }
            //ACCOUNT BALANCE
            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .weight(0.5f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${balanceFormated} ${account.currency.symbol}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (account.initBalance > 0) Green else Red
                )
                Text(
                    text = "${balanceInBaseCurrency} ${baseCurrency.symbol}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrey
                )
            }
        }
    }
}

@Composable
fun CircleIcon(
    icon: ImageVector,
    size: Dp = 56.dp,
    iconColor: Color = Grey,
    contentDescription: String? = "",
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier
                .size(size * 0.5f)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ExampleAccountListPreview() {
    FinancialTheme(
        darkTheme = false
    ) {
        AccountListScreen(
            onNavigateToForm = {},
            onBackPressed = {},
        )
    }
}
