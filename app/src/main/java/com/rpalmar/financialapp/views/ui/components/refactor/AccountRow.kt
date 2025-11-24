package com.rpalmar.financialapp.views.ui.components.refactor

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
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.views.ui.theme.DarkGrey


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
                    icon = account.style.uiIcon,
                    color = account.style.uiColor,
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
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}