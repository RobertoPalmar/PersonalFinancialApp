package com.rpalmar.financialapp.views.ui.components.itemRows

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.views.ui.components.DefaultIcon
import com.rpalmar.financialapp.views.ui.components.formatAmount
import com.rpalmar.financialapp.views.ui.components.formatDate
import com.rpalmar.financialapp.views.ui.components.transactionColor
import com.rpalmar.financialapp.views.ui.theme.DarkGrey


@Composable
fun TransactionRow(
    transaction: TransactionDomain,
    mainCurrency: CurrencyDomain,
    onClick: () -> Unit = {}
) {
    val color = transactionColor(transaction.transactionType)

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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row() {
                //CATEGORY ICON
                DefaultIcon(
                    title = transaction.description,
                    icon = transaction.category.style.uiIcon,
                    color = transaction.category.style.uiColor,
                    circleSize = 40.dp,
                    iconSize = 25.dp
                )

                Spacer(modifier = Modifier.width(10.dp))

                //DESCRIPTION AND CATEGORY
                Column {
                    Text(
                        text = transaction.description,
                        color = DarkGrey,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Text(
                        text = transaction.category.name,
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
                    text = formatAmount(transaction.amount, transaction.currency.symbol),
                    color = color,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "(${formatAmount(transaction.amountInBaseCurrency, mainCurrency.symbol)})",
                    color = DarkGrey,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formatDate(transaction.transactionDate),
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}