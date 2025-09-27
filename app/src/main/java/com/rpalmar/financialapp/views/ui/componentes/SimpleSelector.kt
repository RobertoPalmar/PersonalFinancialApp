package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SimpleSelector(
    modifier: Modifier = Modifier,
    placeholder: String,
    itemList: List<T>,
    selectedItem: T? = null,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelection by remember { mutableStateOf(selectedItem) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = currentSelection?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(placeholder) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemList.forEach { option ->
                DropdownMenuItem(
                    text = { Text(itemLabel(option)) },
                    onClick = {
                        currentSelection = option
                        expanded = false
                        onItemSelected(option)
                    }
                )
            }
        }
    }
}
