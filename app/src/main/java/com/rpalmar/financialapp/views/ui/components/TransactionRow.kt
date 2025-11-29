package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.views.ui.theme.DarkGrey


@Composable
fun TransactionRow(
    transaction: TransactionDomain,
    onClick: () -> Unit = {}
) {
    val color = transactionColor(transaction.transactionType)

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
                text = formatAmount(transaction.amountInBaseCurrency, transaction.currency.symbol),
                color = color,
                style = MaterialTheme.typography.bodyMedium,
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