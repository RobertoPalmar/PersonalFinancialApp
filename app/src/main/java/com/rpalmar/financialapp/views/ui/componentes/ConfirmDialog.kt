package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme

@Composable
fun ConfirmDialog(
    show: Boolean,
    title: String = "¿Estás seguro?",
    message: String = "Esta acción no se puede deshacer.",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            shape = MaterialTheme.shapes.medium,
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(
                        text = "Confirmar",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(
                        text = "Cancelar",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ConfirmDialogPreview() {
    FinancialTheme {
        ConfirmDialog(show = true, onConfirm = {}, onDismiss = {})
    }
}
