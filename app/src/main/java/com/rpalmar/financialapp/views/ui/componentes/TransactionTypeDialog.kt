package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.MoveDown
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.models.TransactionType

@Composable
fun TransactionTypeDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onTypeSelected: (TransactionType) -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Select Transaction Type",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Column {
                    TransactionType.values().forEach { type ->
                        TransactionTypeRow(type = type) {
                            onTypeSelected(type)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        )
    }
}

@Composable
private fun TransactionTypeRow(
    type: TransactionType,
    onClick: () -> Unit
) {
    val icon = when (type) {
        TransactionType.INCOME -> Icons.Default.Add
        TransactionType.EXPENSE -> Icons.Default.RemoveCircle
        TransactionType.TRANSFER -> Icons.Default.MoveDown
        TransactionType.ADJUSTMENT -> Icons.Default.BuildCircle
    }

    val label = when (type) {
        TransactionType.INCOME -> "Income"
        TransactionType.EXPENSE -> "Expense"
        TransactionType.TRANSFER -> "Transfer"
        TransactionType.ADJUSTMENT -> "Adjustment"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal
        )
    }
}
