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
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorWithSearchBar(
    placeholder: String,
    itemList: List<String>,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Filtrado por búsqueda
    val filteredItems = remember(searchQuery, itemList) {
        itemList.filter { it.contains(searchQuery, ignoreCase = true) }
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                expanded = true // abre menú solo al escribir
            },
            label = { Text(placeholder) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (filteredItems.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No se encontraron resultados") },
                    onClick = {}
                )
            } else {
                filteredItems.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            searchQuery = option
                            expanded = false
                            onItemSelected(option)
                        }
                    )
                }
            }
        }
    }
}


@Composable
@Preview
fun PreviewSelectorWithBar(){
    SelectorWithSearchBar(
        itemList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5"),
        onItemSelected = {},
        placeholder = "Select a Currency"
    )
}