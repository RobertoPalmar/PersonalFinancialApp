package com.rpalmar.financialapp.views.ui.components

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.White

@Composable
fun ModalDialog(
    title:String,
    message:String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {onDismiss()},
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 4.dp,
        containerColor = Black,
        titleContentColor = White,
        textContentColor = White.copy(0.9f),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Text(
                "Sí",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .clickable { onAccept() }
                    .padding(10.dp)
            )
        },
        dismissButton = {
            Text(
                "No",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .clickable {onDismiss()}
                    .padding(10.dp)
            )
        }
    )
}