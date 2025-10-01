package com.rpalmar.financialapp.views.account

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.utils.Utils
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Grey
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import java.util.Date
import java.util.UUID

@Composable
fun AccountDetailScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit,
    accountId: Long
) {
    //SET UP VIEW MODEL
    val backStackEntry = remember(navController.currentBackStackEntry) {navController.getBackStackEntry("account_flow")}
    val viewModel: AccountViewModel = hiltViewModel(backStackEntry)

    //ACCOUNT STATE DATA
    val accountFormState = viewModel.accountUIState.collectAsState()

    LaunchedEffect(key1 = accountId) {
        viewModel.setCurrentAccount(accountId)
    }

    MainLayout {
        if (accountFormState.value.currentSelectedAccount == null || accountFormState.value.mainCurrency == null) {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            MainAccountCard(
                account = accountFormState.value.currentSelectedAccount!!,
                mainCurrency = accountFormState.value.mainCurrency!!
            )
            Spacer(modifier = Modifier.height(8.dp))
            TransactionsListSection(
                viewModel = viewModel,
                accountID = accountFormState.value.currentSelectedAccount!!.id,
                mainCurrency = accountFormState.value.mainCurrency!!
            )
        }
    }
}


@Composable
fun MainAccountCard(
    account: AccountDomain,
    mainCurrency: CurrencyDomain
) {
    // FORMAT BALANCE AMOUNT
    val balanceFormatted = remember(account.balance) {
        String.format("%.2f", account.balance)
    }
    val balanceInBaseCurrency = remember(account.balanceInBaseCurrency) {
        String.format("%.2f", account.balanceInBaseCurrency)
    }

    ElevatedCard(
        colors = CardDefaults.cardColors(Blue),
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        )
    ) {
        Box() {

            Column(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(15.dp)
            ) {
                Row() {
                    Column(
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(
                            text = account.name,
                            style = MaterialTheme.typography.titleLarge,

                            )
                        Text(
                            text = account.description,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "$balanceFormatted ${account.currency.symbol}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$balanceInBaseCurrency ${mainCurrency.symbol}",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
fun TransactionsListSection(
    accountID: Long,
    viewModel: AccountViewModel,
    mainCurrency: CurrencyDomain
) {

    val transactions = viewModel.getTransactionsPerAccount(accountID).collectAsLazyPagingItems()
    val overscrollEffect = rememberOverscrollEffect()

    Text(
        text = "Transactions List",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(0.dp, 10.dp)
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
        when {
            //HANDLE LOADING STATE
            transactions.loadState.refresh is LoadState.Loading -> {
                item {
                    Column {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                }
            }
            //HANDLE NO ITEMS
            transactions.itemCount == 0 -> {
                item {
                    Text(
                        text = "Aún no hay transacciones",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = Grey
                    )
                }
            }
            //HANDLE TRANSACTIONS
            else -> {
                items(transactions.itemCount) { index ->
                    val transaction = transactions[index]
                    transaction?.let {
                        TransactionCard(transaction = it, mainCurrency = mainCurrency)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionCard(
    transaction: TransactionDomain,
    mainCurrency: CurrencyDomain
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedElevation by animateDpAsState(if (isPressed) 8.dp else 4.dp, label = "cardElevation")

    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .height(110.dp)
            .fillMaxWidth(),
//            .clickable(
//                interactionSource = interactionSource,
//                indication = null,
//                onClick = { /* TODO: acción al hacer clic */ }
//            ),
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            // TRANSACTION SECTION
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                if (transaction.originSource != null) {
                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = "arrow",
                            tint = DarkGrey,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 5.dp, 0.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = "${transaction.originSource?.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrey
                        )
                    }
                }

                if (transaction.destinationSource != null) {
                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_plus),
                            contentDescription = "arrow",
                            tint = DarkGrey,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 5.dp, 0.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = "${transaction.destinationSource?.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrey
                        )
                    }
                }
            }

            // AMOUNT DATA
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${transaction.amount} ${transaction.currency.symbol}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.amount > 0) Green else Red
                )
                Text(
                    text = "${transaction.amountInBaseCurrency} ${mainCurrency.symbol}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrey
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = Utils.getFormatDate(transaction.transactionDate),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
                Text(
                    text = Utils.getFormatHours(transaction.transactionDate),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ExampleAccountDetailPreview() {
    FinancialTheme(
        darkTheme = false
    ) {
        TransactionCard(
            mainCurrency = CurrencyDomain(
                id = 1,
                name = "Dolar",
                symbol = "$",
                exchangeRate = 1.0,
                currencyPriority = 1,
                ISO = "USD"
            ),
            transaction = TransactionDomain(
                transactionCode = UUID.randomUUID(),
                originSource = SimpleTransactionSourceAux(
                    name = "Cuenta Origen",
                    description = "",
                    transactionEntityType = TransactionSourceType.ACCOUNT,
                    id = 1
                ),
                destinationSource = SimpleTransactionSourceAux(
                    name = "Cuenta Destino",
                    description = "",
                    transactionEntityType = TransactionSourceType.ACCOUNT,
                    id = 2
                ),
                amount = 514.12,
                description = "Transaccion de Carga Inicial",
                transactionDate = Date(),
                currency = CurrencyDomain(
                    id = 1,
                    name = "Dolar",
                    symbol = "$",
                    exchangeRate = 1.0,
                    currencyPriority = 1,
                    ISO = "USD"
                ),
                amountInBaseCurrency = 100.0
            )
        )
    }
}