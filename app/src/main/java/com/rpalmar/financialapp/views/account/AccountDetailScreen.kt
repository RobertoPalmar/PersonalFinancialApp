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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Addchart
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.utils.Utils
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.ui.componentes.ConfirmDialog
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.TransactionTypeDialog
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Grey
import com.rpalmar.financialapp.views.ui.theme.Orange
import com.rpalmar.financialapp.views.ui.theme.PurpleBlue
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

    //DIALOG STATE
    var showTransactionTypeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = accountId) {
        viewModel.setCurrentAccount(accountId)
    }
    
    fun handleDeleteAccount(){
        viewModel.handleDeleteAccount()
        navController.navigate("accounts")
    }

    fun handleUpdateAccount(){
        viewModel.handleUpdateAccountForm()
        navController.navigate("accountForm")
    }

    //DELETE DIALOG STATE
    var showDeleteDialog by remember { mutableStateOf(false) }

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
                mainCurrency = accountFormState.value.mainCurrency!!,
                handleShowConfirmDeleteDialog = { showDeleteDialog = !showDeleteDialog },
                handleEditAccount = { handleUpdateAccount() },
                handleNewTransaction = { showTransactionTypeDialog = true }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TransactionsListSection(
                viewModel = viewModel,
                accountID = accountFormState.value.currentSelectedAccount!!.id,
                mainCurrency = accountFormState.value.mainCurrency!!
            )
        }

        //CONFIRM DELETE ACCOUNT DIALOG
        ConfirmDialog(
            show = showDeleteDialog,
            onConfirm = { handleDeleteAccount() },
            onDismiss = { showDeleteDialog = false}
        )

        TransactionTypeDialog(
            show = showTransactionTypeDialog,
            onDismiss = { showTransactionTypeDialog = false },
            onTypeSelected = { transactionType ->
                val route = "transactionForm/${transactionType.name}?sourceType=${TransactionSourceType.ACCOUNT}&sourceId=${accountId}"
                navController.navigate(route)
                showTransactionTypeDialog = false
            }
        )
    }
}


@Composable
fun MainAccountCard(
    account: AccountDomain,
    mainCurrency: CurrencyDomain,
    handleShowConfirmDeleteDialog: () -> Unit = {},
    handleEditAccount: () -> Unit = {},
    handleNewTransaction: () -> Unit = {}
) {

    // FORMAT BALANCE AMOUNT
    val balanceFormatted = remember(account.balance) {String.format("%.2f", account.balance)}
    val balanceInBaseCurrency = remember(account.balanceInBaseCurrency) {String.format("%.2f", account.balanceInBaseCurrency)}

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
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                ) {
                    Column(){
                        Text(
                            text = "$balanceFormatted ${account.currency.symbol}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$balanceInBaseCurrency ${mainCurrency.symbol}",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        IconButton(onClick = { handleShowConfirmDeleteDialog() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Account"
                            )
                        }
                        IconButton(onClick = { handleEditAccount() } ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Update Account"
                            )
                        }
                        IconButton(onClick = { handleNewTransaction() } ) {
                            Icon(
                                imageVector = Icons.Default.Addchart,
                                contentDescription = "Add Transaction"
                            )
                        }
                    }
                }
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
            .height(120.dp)
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
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TransactionTypeLabel(type = transaction.transactionType)
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = "arrow",
                        tint = DarkGrey,
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 5.dp, 0.dp)
                            .size(20.dp)
                    )
                    Text(
                        text = transaction.source.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrey
                    )
                }
                if (transaction.linkedTransaction != null) {
                    Row(
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "arrow",
                            tint = DarkGrey,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 5.dp, 0.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = transaction.linkedTransaction.source.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkGrey
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f),
                horizontalAlignment = Alignment.End
            ) {
                // AMOUNT DATA
                Text(
                    text = "${transaction.amount} ${transaction.currency.symbol}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.amount > 0) Green else Red
                )
                Text(
                    text = "${transaction.amountInBaseCurrency} ${mainCurrency.symbol}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrey
                )
                Spacer(modifier = Modifier.weight(1f))

                //DATE DATA
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = Utils.getFormatDate(transaction.transactionDate),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                    Text(
                        text = Utils.getFormatHours(transaction.transactionDate),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionTypeLabel(
    type: TransactionType
){
    val color = when(type){
        TransactionType.INCOME -> Green
        TransactionType.EXPENSE -> Red
        TransactionType.TRANSFER -> Orange
        TransactionType.ADJUSTMENT -> PurpleBlue
    }
    
    val icon = when(type){
        TransactionType.INCOME -> Icons.Default.Add
        TransactionType.EXPENSE -> Icons.Default.RemoveCircle
        TransactionType.TRANSFER -> Icons.Default.MoveDown
        TransactionType.ADJUSTMENT -> Icons.Default.BuildCircle
    }

    val label = when(type){
        TransactionType.INCOME -> "Income"
        TransactionType.EXPENSE -> "Expense"
        TransactionType.TRANSFER -> "Transfer"
        TransactionType.ADJUSTMENT -> "Adjustment"
    }

    Row(
        modifier = Modifier.padding(top = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "transactionType",
            tint = color,
            modifier = Modifier
                .padding(0.dp, 0.dp, 5.dp, 0.dp)
                .size(20.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ExampleAccountDetailPreview() {
    FinancialTheme(
        darkTheme = false
    ) {
//        MainAccountCard(
//            account = MockupProvider.getMockAccounts()[0],
//            mainCurrency = MockupProvider.getMockCurrencies()[0]
//        )
        TransactionCard(
            transaction = TransactionDomain(
                id = 0,
                transactionCode = UUID.randomUUID(),
                source = MockupProvider.getMockAccounts()[0].toAuxDomain(),
                amount = 100.00,
                amountInBaseCurrency = 100.00,
                transactionType = com.rpalmar.financialapp.models.TransactionType.EXPENSE,
                transactionDate = Date(),
                currency = MockupProvider.getMockCurrencies()[0],
                exchangeRate = 1.00,
                description = "Example Transaction",
                linkedTransaction = MockupProvider.getMockTransactions()[1]
            ),
            mainCurrency = MockupProvider.getMockCurrencies()[0]
        )
    }
}