package com.rpalmar.financialapp.views.mainMenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.ui.MainMenuItem
import com.rpalmar.financialapp.providers.sealeds.MainSectionContent
import com.rpalmar.financialapp.views.account.AccountDataCard
import com.rpalmar.financialapp.views.ui.componentes.refactor.CreditCardIcon
import com.rpalmar.financialapp.views.ui.componentes.refactor.DefaultIcon
import com.rpalmar.financialapp.views.ui.componentes.refactor.IncomeExpenseSection
import com.rpalmar.financialapp.views.ui.componentes.refactor.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.refactor.TransactionRow
import com.rpalmar.financialapp.views.ui.componentes.refactor.formatAmount
import com.rpalmar.financialapp.views.ui.componentes.refactor.formatDate
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.LightGreen
import com.rpalmar.financialapp.views.ui.theme.Orange
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.CoinsSolid
import compose.icons.lineawesomeicons.ExchangeAltSolid
import compose.icons.lineawesomeicons.HomeSolid
import compose.icons.lineawesomeicons.TagsSolid
import compose.icons.lineawesomeicons.WalletSolid

@Composable
fun MainMenuScreen() {

    var currentSection by remember { mutableStateOf<MainSectionContent>(MainSectionContent.Home) }

    MainLayout {
        Column {

            //SUMARY SECTION
            when (currentSection) {
                is MainSectionContent.Home,
                is MainSectionContent.Accounts -> {
                    GeneralSummaryBalance()
                }

                is MainSectionContent.AccountDetail -> {
                    val account = (currentSection as MainSectionContent.AccountDetail).account
                    AccountSummaryBalance(account)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            BackgroundWhiteCardSection {

                when (currentSection) {
                    is MainSectionContent.Home -> {
                        MainMenuGridSection(
                            items = listOf(
                                MainMenuItem("Accounts", LineAwesomeIcons.WalletSolid, Orange),
                                MainMenuItem("Categories", LineAwesomeIcons.TagsSolid, LightGreen),
                                MainMenuItem("Currencies", LineAwesomeIcons.CoinsSolid, Red),
                                MainMenuItem("Transactions", LineAwesomeIcons.ExchangeAltSolid, Blue)
                            ),
                            onItemClick = { item ->
                                when (item.title) {
                                    "Accounts" -> currentSection = MainSectionContent.Accounts
                                }
                            }
                        )
                        TransactionsListSection(
                            transactions = MockupProvider.getMockTransactions(),
                            onClick = {}
                        )
                    }

                    is MainSectionContent.Accounts -> {
                        AccountsListSection(
                            accounts = MockupProvider.getMockAccounts(),
                            onClick = { account ->
                                currentSection = MainSectionContent.AccountDetail(account)
                            }
                        )
                    }

                    is MainSectionContent.AccountDetail -> {
                        TransactionsListSection(
                            transactions = MockupProvider.getMockTransactions(),
                            onClick = {}
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun GeneralSummaryBalance() {
    val totalBalance = 153215.12;
    val currencySymbol = "$"

    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .padding(top = 10.dp)
    ) {
        GreetingSection()
        TotalBalanceCard(
            totalBalance = totalBalance,
            totalBalanceInPrimaryCurrency = 1565.21,
            currencySymbol = currencySymbol
        )
        IncomeExpenseSection(
            income = 18250.00,
            expenses = 7420.25,
            firstCurrency = MockupProvider.getMockCurrencies()[0],
            altCurrency = MockupProvider.getMockCurrencies()[1]
        )
    }
}

@Composable
fun AccountSummaryBalance(account: AccountDomain) {
    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .padding(top = 10.dp)
    ) {
        AccountDataCard(
            account = MockupProvider.getMockAccounts()[0],
            onAddClick = { },
            onDeleteClick = {},
            onEditClick = { }
        )
        IncomeExpenseSection(
            income = 18250.00,
            expenses = 7420.25,
            firstCurrency = MockupProvider.getMockCurrencies()[0],
            altCurrency = MockupProvider.getMockCurrencies()[1]
        )
    }
}

@Composable
fun BackgroundWhiteCardSection(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 12.dp))
        content()
    }
}

@Composable
fun MainMenuGridSection(
    items: List<MainMenuItem>,
    onItemClick: (MainMenuItem) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
    ) {
        SectionTitle("Sections")
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(items.size) { index ->
                MainMenuCard(
                    item = items[index],
                    onClick = { onItemClick(items[index]) }
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String
) {
    // Title
    Text(
        text = title,
        color = DarkGrey,
        style = MaterialTheme.typography.titleMedium
    )

    Spacer(modifier = Modifier.height(5.dp))
}


@Composable
fun MainMenuCard(
    item: MainMenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(bottom = 8.dp, top = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 18.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ICON
            DefaultIcon(
                title = item.title,
                icon = item.icon,
                color = item.color,
                circleSize = 38.dp,
                iconSize = 22.dp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // TITLE
            Text(
                text = item.title,
                color = DarkGrey,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun GreetingSection() {
    Text(
        text = "Hi, Roberto",
        style = MaterialTheme.typography.titleLarge,
        color = White,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Here’s your financial overview for today.",
        style = MaterialTheme.typography.bodyLarge,
        color = White
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TotalBalanceCard(
    totalBalance: Double,
    totalBalanceInPrimaryCurrency: Double? = null,
    currencySymbol: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //GENERAL DATA
                Column {
                    Text(
                        text = "General Balance",
                        style = MaterialTheme.typography.bodyLarge,
                        color = White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = formatAmount(totalBalance, currencySymbol),
                        style = MaterialTheme.typography.titleLarge,
                        color = White
                    )
                    if (totalBalanceInPrimaryCurrency != null) {
                        Text(
                            text = formatAmount(totalBalanceInPrimaryCurrency, currencySymbol),
                            style = MaterialTheme.typography.bodyLarge,
                            color = White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                CreditCardIcon(size = 50.dp)
            }
        }
    }
}

@Composable
fun TransactionsListSection(
    transactions: List<TransactionDomain>,
    onClick: (TransactionDomain) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        SectionTitle("Last Transactions")
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 5.dp)
        ) {
            transactions.forEach { transaction ->
                TransactionRow(
                    transaction,
                    onClick = { onClick(transaction) }
                )
            }
        }
    }
}

@Composable
fun AccountsListSection(
    accounts: List<AccountDomain>,
    onClick: (AccountDomain) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        SectionTitle("Accounts")
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 5.dp)
        ) {
            accounts.forEach { account ->
                AccountRow(
                    account,
                    onClick = { onClick(account) }
                )
            }
        }
    }
}

@Composable
fun AccountRow(
    account: AccountDomain,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row {
                //ACCOUNT ICON
                DefaultIcon(
                    title = account.name,
                    icon = LineAwesomeIcons.HomeSolid,
                    color = LightGreen,
                    circleSize = 40.dp,
                    iconSize = 25.dp
                )

                Spacer(modifier = Modifier.width(10.dp))

                //DETAIL DATA
                Column {
                    Text(
                        text = account.name,
                        color = DarkGrey,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = account.description,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                //AMOUNT DATA
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = formatAmount(account.balance, account.currency.symbol),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkGrey
                    )
                    Text(
                        text = formatAmount(account.balanceInMainCurrency, account.currency.symbol),
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    FinancialTheme(darkTheme = false) {
        MainMenuScreen()
    }
}
