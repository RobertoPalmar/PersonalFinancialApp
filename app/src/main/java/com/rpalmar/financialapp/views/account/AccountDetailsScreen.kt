package com.rpalmar.financialapp.views.account

import compose.icons.Octicons
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.mock.MockupProvider.getMockTransactions
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.views.ui.componentes.refactor.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.refactor.CreditCardIcon
import com.rpalmar.financialapp.views.ui.componentes.refactor.DefaultIcon
import com.rpalmar.financialapp.views.ui.componentes.refactor.formatAmount
import com.rpalmar.financialapp.views.ui.componentes.refactor.formatDate
import com.rpalmar.financialapp.views.ui.componentes.refactor.transactionColor
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.LightGreen
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ArrowDownSolid
import compose.icons.lineawesomeicons.ArrowUpSolid
import compose.icons.lineawesomeicons.HomeSolid
import compose.icons.octicons.Pencil24
import compose.icons.octicons.PlusCircle24
import compose.icons.octicons.Trash24

@Composable
fun AccountDetailsScreen(
    onAddClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onEditClick: () -> Unit = {}
) {

    MainLayout {
        Column {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                AccountDataCard(
                    account = MockupProvider.getMockAccounts()[0],
                    onAddClick = onAddClick,
                    onDeleteClick = onDeleteClick,
                    onEditClick = onEditClick
                )
                IncomeExpenseSummarySection()
            }
            Spacer(modifier = Modifier.height(2.dp))
            TransactionListSection(
                transactions = getMockTransactions()
            )
        }
    }
}

@Composable
fun AccountDataCard(
    account: AccountDomain,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    //FORMAT BALANCE
    val balanceFormatted = formatAmount(
        account.balance,
        account.currency.symbol
    )

    //FORMAT BALANCE IN MAIN CURRENCY
    val balanceInPrimaryFormatted = if (!account.currency.mainCurrency) {
        formatAmount(
            account.balanceInMainCurrency,
            account.currency.symbol
        )
    } else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .padding(bottom = 20.dp, start = 15.dp, end = 15.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    //ACCOUNT DATA
                    Column {
                        Text(
                            text = account.name,
                            color = White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Total Balance",
                            color = White,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = balanceFormatted,
                            style = MaterialTheme.typography.titleLarge,
                            color = White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (balanceInPrimaryFormatted != null) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = balanceInPrimaryFormatted,
                                style = MaterialTheme.typography.bodyLarge,
                                color = White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // ACCOUNT ICON
                    DefaultIcon(
                        title = "Account",
                        icon = LineAwesomeIcons.HomeSolid,
                        color = LightGreen,
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

                    //ACTION BUTTONS SECTION
                    Row {
                        IconButton(
                            onClick = onAddClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = Octicons.PlusCircle24),
                                contentDescription = "Add",
                                tint = White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = Octicons.Trash24),
                                contentDescription = "Delete",
                                tint = White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = rememberVectorPainter(image = Octicons.Pencil24),
                                contentDescription = "Edit",
                                tint = White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    //UI CREDIT CARD CANVAS
                    CreditCardIcon(size = 50.dp)
                }
            }
        }
    }
}

@Composable
fun IncomeExpenseSummarySection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
        ) {

            //INCOME
            SummaryAmountCard(
                modifier = Modifier.weight(1f),
                icon = LineAwesomeIcons.ArrowUpSolid,
                color = Green,
                title = "Income",
                amount = "$ 61,550.48",
                amountInPrimaryCurrency = "Bs 17548.15"
            )

            Spacer(modifier = Modifier.padding(5.dp))

            //EXPENSES
            SummaryAmountCard(
                modifier = Modifier.weight(1f),
                icon = LineAwesomeIcons.ArrowDownSolid,
                color = Red,
                title = "Expenses",
                amount = "$ 12,000.25",
                amountInPrimaryCurrency = "Bs 548.15"
            )
        }
    }
}

@Composable
fun SummaryAmountCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    amountInPrimaryCurrency: String? = null,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //ICON
            DefaultIcon(
                circleSize = 32.dp,
                color = color,
                icon = icon,
                title = title
            )

            Spacer(modifier = Modifier.width(10.dp))

            //AMOUNT TEXT
            Column(
                modifier = Modifier.width(100.dp)
            ) {
                val amountSize = when (amount.length) {
                    in 0..12 ->
                        16.sp

                    else ->
                        14.sp
                }

                Text(text = title, color = White, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = amount,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = amountSize,
                    color = White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (amountInPrimaryCurrency != null) {
                    Text(
                        text = amountInPrimaryCurrency,
                        style = MaterialTheme.typography.bodyLarge,
                        color = White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionListSection(transactions: List<TransactionDomain>) {
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

            transactions.forEach { transaction ->
                TransactionRow(transaction)
            }
        }
    }
}


@Composable
fun TransactionRow(t: TransactionDomain) {
    val color = transactionColor(t.transactionType)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row() {
            //CATEGORY ICON
            DefaultIcon(
                title = t.description,
                icon = LineAwesomeIcons.HomeSolid,
                color = color,
                circleSize = 40.dp,
                iconSize = 25.dp
            )

            Spacer(modifier = Modifier.width(10.dp))

            //DESCRIPTION AND CATEGORY
            Column {

                Text(
                    text = t.description,
                    color = DarkGrey,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )

                Text(
                    text = t.category.name,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        //AMOUNT AND DATE
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formatAmount(t.amountInBaseCurrency, t.currency.symbol),
                color = color,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatDate(t.transactionDate),
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }

    }
}


@Composable
@Preview(showBackground = true)
fun ExamplePreview() {
    FinancialTheme(
        darkTheme = false
    ) {
        AccountDetailsScreen()
    }
}

