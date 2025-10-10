package com.rpalmar.financialapp.views.ui.componentes

import android.R.attr.textStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.views.account.AccountFormScreen
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Grey
import com.rpalmar.financialapp.views.ui.theme.Red

@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    leadingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
    errorMessage: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = MaterialTheme.shapes.medium,

    ) {
        BasicTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(1f),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxSize(1f)
                        .border(
                            width = 1.dp,
                            color = DarkGrey,
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(1f),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (value.isEmpty()) {
                                if (!leadingText.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.width(22.dp))
                                }
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = DarkGrey
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxSize(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            if (!leadingText.isNullOrEmpty()) {
                                Text(
                                    text = leadingText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Black,
                                    modifier = Modifier.padding(end = 6.dp)
                                )
                            }

                            innerTextField()
                        }
                    }
                }
            }
        )
        ErrorFieldSection(errorMessage = errorMessage)
    }
}

@Composable
@Preview(showBackground = true)
fun ExampleBaseTextFieldPreview() {
    FinancialTheme(
        darkTheme = false
    ) {
        BaseTextField(
            value = "",
            onValueChange = {},
            label = "Inserta un Valor",
            enabled = false,
            errorMessage = "Campo requerido"
        )
    }
}
