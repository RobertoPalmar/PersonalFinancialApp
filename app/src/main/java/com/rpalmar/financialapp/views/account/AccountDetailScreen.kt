package com.rpalmar.financialapp.views.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import java.util.Date
import java.util.UUID

@Composable
fun AccountDetailScreen(
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current

    MainLayout {
        MainAccountCard(
            account = AccountDomain(
                id = 1,
                name = "Banco BDV",
                description = "Cuenta Principal",
                initBalance = 1850.15,
                initBalanceInBaseCurrency = 1850.15,
                currency = CurrencyDomain(
                    id = 1,
                    name = "Dolar",
                    symbol = "$",
                    exchangeRate = 1.0,
                    currencyPriority = 1,
                    ISO = "USD"
                ),
                style = null
            )
        )
    }
}

@Composable
fun MainAccountCard(
    account: AccountDomain
) {

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
                            text = "Banco BDV",
                            style = MaterialTheme.typography.titleLarge,

                            )
                        Text(
                            text = "Cuenta Principal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Balance: 1850.15 $",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun TransactionCard(
    transaction: TransactionDomain,
    mainCurrency: CurrencyDomain
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .height(82.dp)
            .fillMaxWidth(),
//            .clickable(
//                interactionSource = interactionSource,
//                indication = null,
//                onClick = { /* TODO: acción al hacer clic */ }
//            ),
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
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "${transaction.originSource?.name} -> ${transaction.destinationSource?.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkGrey
                )
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