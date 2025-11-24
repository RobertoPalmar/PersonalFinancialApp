package com.rpalmar.financialapp.views.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.models.ButtonType
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.White


@Composable
fun FormNavigatorButtonSection(
    isSaving: Boolean,
    isEditing: Boolean = false,
    onConfirm: () -> Unit,
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
    ) {
        SimpleButton(
            text = "Go Back",
            color = Black,
            onClick = { onBackPressed() },
            type = ButtonType.SECONDARY,
            modifier = Modifier.weight(1f),
            enable = !isSaving
        )
        Spacer(modifier = Modifier.width(10.dp))
        SimpleButton(
            modifier = Modifier.weight(1f),
            text = if (isEditing) "Update" else "Create",
            color = Black,
            onClick = { onConfirm() },
            type = ButtonType.PRIMARY,
            enable = !isSaving,
            content = if (isSaving) {
                {
                    CircularProgressIndicator(
                        color = White,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else null
        )
    }
}

@Composable
@Preview
fun FormNavigatorButtonSectionPreview(){
    FinancialTheme {
        FormNavigatorButtonSection(
            isSaving = false,
            isEditing = false,
            onConfirm = {},
            onBackPressed = {}
        )
    }
}