package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.Octicons
import compose.icons.octicons.Pencil24
import compose.icons.octicons.PlusCircle24
import compose.icons.octicons.Trash24

@Composable
fun AccountDataCard(
    account: AccountDomain,
    mainCurrency: CurrencyDomain,
    onAddTransactionClick: (() -> Unit)? = null,
    onDeleteAccountClick: (() -> Unit)? = null,
    onEditAccountClick: (() -> Unit)? = null
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
            mainCurrency.symbol
        )
    } else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(173.dp)
            .padding(bottom = 10.dp, start = 15.dp, end = 15.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 18.dp)
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

                        Text(
                            text = account.description,
                            color = White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // ACCOUNT ICON
                    DefaultIcon(
                        title = "Account",
                        icon = account.style.uiIcon,
                        color = account.style.uiColor,
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
//                    //UI CREDIT CARD CANVAS
//                    CreditCardIcon(size = 50.dp, modifier = Modifier.padding(start = 10.dp))

                    Column {
                        Text(
                            text = "Account Balance",
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
                            Text(
                                text = balanceInPrimaryFormatted,
                                style = MaterialTheme.typography.bodyLarge,
                                color = White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    //ACTION BUTTONS SECTION
                    Row {

                        if (onAddTransactionClick != null) {
                            IconButton(
                                onClick = onAddTransactionClick,
                                modifier = Modifier.size(35.dp)
                            ) {
                                Icon(
                                    painter = rememberVectorPainter(image = Octicons.PlusCircle24),
                                    contentDescription = "Add",
                                    tint = White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        if (onDeleteAccountClick != null) {
                            IconButton(
                                onClick = onDeleteAccountClick,
                                modifier = Modifier.size(35.dp)
                            ) {
                                Icon(
                                    painter = rememberVectorPainter(image = Octicons.Trash24),
                                    contentDescription = "Delete",
                                    tint = White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        if (onEditAccountClick != null) {
                            IconButton(
                                onClick = onEditAccountClick,
                                modifier = Modifier.size(35.dp)
                            ) {
                                Icon(
                                    painter = rememberVectorPainter(image = Octicons.Pencil24),
                                    contentDescription = "Edit",
                                    tint = White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun AccountDataCardPreview() {

    MaterialTheme {
        AccountDataCard(
            account = MockupProvider.getMockAccounts()[0],
            mainCurrency = MockupProvider.getMockCurrencies()[0],
            onAddTransactionClick = {},
            onDeleteAccountClick = {},
            onEditAccountClick = {}
        )
    }
}
