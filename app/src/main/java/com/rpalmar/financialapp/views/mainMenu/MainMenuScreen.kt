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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.ui.MainMenuItem
import com.rpalmar.financialapp.views.account.SummaryAmountCard
import com.rpalmar.financialapp.views.ui.componentes.refactor.DefaultIcon
import com.rpalmar.financialapp.views.ui.componentes.refactor.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.refactor.formatAmount
import com.rpalmar.financialapp.views.ui.theme.Cyan
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Indigo
import com.rpalmar.financialapp.views.ui.theme.LightGreen
import com.rpalmar.financialapp.views.ui.theme.Magenta
import com.rpalmar.financialapp.views.ui.theme.Orange
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.SoftTeal
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ArrowDownSolid
import compose.icons.lineawesomeicons.ArrowUpSolid
import compose.icons.lineawesomeicons.ChartBarSolid
import compose.icons.lineawesomeicons.CogSolid
import compose.icons.lineawesomeicons.CoinsSolid
import compose.icons.lineawesomeicons.ExchangeAltSolid
import compose.icons.lineawesomeicons.FolderSolid
import compose.icons.lineawesomeicons.HomeSolid

@Composable
fun MainMenuScreen(
    onAccountClick: (AccountDomain) -> Unit = {},
    onAddAccountClick: () -> Unit = {}
) {
    val totalBalance = 153215.12;
    val currencySymbol = "$"


    MainLayout {
        Column {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                GreetingSection()
                TotalBalanceCard(
                    totalBalance = totalBalance,
                    totalBalanceInPrimaryCurrency = 1565.21,
                    currencySymbol = currencySymbol,
                    onAddAccountClick = onAddAccountClick
                )
                GlobalSummarySection()
            }
            Spacer(modifier = Modifier.height(10.dp))
            MainMenuGridSection(
                items = listOf(
                    MainMenuItem("Accounts", LineAwesomeIcons.HomeSolid, Green),
                    MainMenuItem("Categories", LineAwesomeIcons.FolderSolid, Orange),
                    MainMenuItem("Currencies", LineAwesomeIcons.CoinsSolid, Cyan),
                    MainMenuItem("Transactions", LineAwesomeIcons.ExchangeAltSolid, Magenta),
                    MainMenuItem("Reports", LineAwesomeIcons.ChartBarSolid, Indigo),
                    MainMenuItem("Settings", LineAwesomeIcons.CogSolid, SoftTeal)
                ),
                onItemClick = {}
            )
        }
    }
}

@Composable
fun MainMenuGridSection(
    title: String = "Menu",
    items: List<MainMenuItem>,
    onItemClick: (MainMenuItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 20.dp)
        ) {
            // Title
            Text(
                text = title,
                color = DarkGrey,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Grid with cards inside your Column
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
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
}


@Composable
fun MainMenuCard(
    item: MainMenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
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
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun GreetingSection(){
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
    totalBalanceInPrimaryCurrency:Double? = null,
    currencySymbol: String,
    onAddAccountClick: () -> Unit
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

//                IconButton(
//                    onClick = onAddAccountClick,
//                    modifier = Modifier.size(40.dp)
//                ) {
//                    Icon(
//                        painter = rememberVectorPainter(Octicons.PlusCircle24),
//                        tint = White,
//                        contentDescription = "Add Account",
//                        modifier = Modifier.size(26.dp)
//                    )
//                }
            }
        }
    }
}

@Composable
fun GlobalSummarySection() {
    Row(modifier = Modifier.fillMaxWidth()) {

        SummaryAmountCard(
            modifier = Modifier.weight(1f),
            icon = LineAwesomeIcons.ArrowUpSolid,
            color = Green,
            title = "Income",
            amount = "$ 18,250.00",
            amountInPrimaryCurrency = "Bs 10,584.00"
        )

        Spacer(modifier = Modifier.width(8.dp))

        SummaryAmountCard(
            modifier = Modifier.weight(1f),
            icon = LineAwesomeIcons.ArrowDownSolid,
            color = Red,
            title = "Expenses",
            amount = "$ 7,420.25",
            amountInPrimaryCurrency = "Bs 87,515.26"
        )
    }
}

@Composable
fun AccountsListSection(
    accounts: List<AccountDomain>,
    onAccountClick: (AccountDomain) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 20.dp)
        ) {

            Text(
                text = "Transactions",
                color = DarkGrey,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            accounts.forEach { account ->
                AccountRow(account, onClick = {onAccountClick(account)})
            }
        }
    }
}

@Composable
fun AccountRow(account: AccountDomain, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                DefaultIcon(
                    title = account.name,
                    icon = LineAwesomeIcons.HomeSolid,
                    color = LightGreen,
                    circleSize = 40.dp,
                    iconSize = 22.dp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = DarkGrey
                    )
                    Text(
                        text = account.currency.symbol,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = formatAmount(account.balance, account.currency.symbol),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = DarkGrey
            )
        }
    }
}

// ===========================================================
// PREVIEW
// ===========================================================
@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    FinancialTheme(darkTheme = false) {
        MainMenuScreen()
    }
}
