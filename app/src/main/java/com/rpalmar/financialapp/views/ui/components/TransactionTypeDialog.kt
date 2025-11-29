package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.models.TransactionType
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ArrowsAltSolid
import compose.icons.lineawesomeicons.ChartBar
import compose.icons.lineawesomeicons.MinusSolid
import compose.icons.lineawesomeicons.PlusSolid

@Composable
fun TransactionTypeDialog(
    onDismiss: () -> Unit,
    onTypeSelected: (TransactionType) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color(0xFF1E1E1E),           // Fondo oscuro estilo app
        titleContentColor = Color.White,
        textContentColor = Color.White.copy(0.9f),

        title = {
            Text(
                "Seleccionar tipo de transacción",
                color = Color.White
            )
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                TransactionType.values().forEach { type ->
                    TransactionTypeRow(
                        type = type,
                        onClick = {
                            onTypeSelected(type)
                            onDismiss()
                        }
                    )
                }
            }
        },

        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Cancelar",
                    color = Color.White.copy(0.8f)
                )
            }
        }
    )
}

@Composable
private fun TransactionTypeRow(
    type: TransactionType,
    onClick: () -> Unit
) {
    val icon = when (type) {
        TransactionType.INCOME -> LineAwesomeIcons.PlusSolid
        TransactionType.EXPENSE -> LineAwesomeIcons.MinusSolid
        TransactionType.TRANSFER -> LineAwesomeIcons.ArrowsAltSolid
        TransactionType.ADJUSTMENT -> LineAwesomeIcons.ChartBar
    }

    val label = when (type) {
        TransactionType.INCOME -> "Ingreso"
        TransactionType.EXPENSE -> "Gasto"
        TransactionType.TRANSFER -> "Transferencia"
        TransactionType.ADJUSTMENT -> "Ajuste"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(26.dp)
        )

        Text(
            text = label,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
    }
}
