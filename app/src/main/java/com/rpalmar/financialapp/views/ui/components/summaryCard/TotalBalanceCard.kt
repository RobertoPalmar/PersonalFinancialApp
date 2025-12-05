package com.rpalmar.financialapp.views.ui.components.summaryCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.views.ui.components.CreditCardIcon
import com.rpalmar.financialapp.views.ui.components.formatAmount
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.White

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