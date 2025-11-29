package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.White


@Composable
fun SummaryAmountCard(
    modifier: Modifier = Modifier,
    title: String,
    amount: String,
    amountInAltCurrency: String? = null,
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
                if (amountInAltCurrency != null) {
                    Text(
                        text = amountInAltCurrency,
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
