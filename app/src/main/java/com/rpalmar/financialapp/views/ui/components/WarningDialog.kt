package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.views.ui.theme.DarkGrey

/**
 * Reusable warning dialog with optional "don't show again" checkbox
 */
@Composable
fun WarningDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    confirmText: String = "Ok",
    dismissText: String = "Cancel",
    onDontShowAgainChanged: ((Boolean) -> Unit)? = null
) {
    var dontShowAgain by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            ) 
        },
        text = {
            Column {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                if (onDontShowAgainChanged != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = dontShowAgain,
                            onCheckedChange = { checked ->
                                dontShowAgain = checked
                                onDontShowAgainChanged.invoke(checked)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Don't show this message again",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (onConfirm != null) {
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    Text(confirmText, color = DarkGrey)
                }
            } else {
                TextButton(onClick = onDismiss) {
                    Text(confirmText, color = DarkGrey)
                }
            }
        },
        dismissButton = {
            if (onConfirm != null) {
                TextButton(onClick = onDismiss) {
                    Text(dismissText, color = DarkGrey)
                }
            }
        }
    )
}
