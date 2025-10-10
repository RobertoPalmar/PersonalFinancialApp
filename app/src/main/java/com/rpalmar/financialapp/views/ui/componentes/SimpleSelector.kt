package com.rpalmar.financialapp.views.ui.componentes

import android.R
import android.view.Menu
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.models.domain.auxiliar.ItemSelectionUI
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SimpleSelector(
    modifier: Modifier = Modifier,
    placeholder: String,
    itemList: List<T>,
    selectedItem: T? = null,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    itemDetail:((T) -> String)? = null,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelection by remember { mutableStateOf(selectedItem) }

    LaunchedEffect(selectedItem) {
        currentSelection = selectedItem
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(Color.White),
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(1f)
                .border(
                    width = 1.dp,
                    color = DarkGrey,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = currentSelection?.let { itemLabel(it) } ?: placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if(currentSelection != null && itemDetail != null) Bold else Normal,
                        color = if (currentSelection == null) DarkGrey else Color.Black
                    )
                    if(currentSelection != null && itemDetail != null){
                        Text(
                            text = currentSelection?.let { itemDetail(it) } ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        }
        ErrorFieldSection(errorMessage = errorMessage)

        DropdownMenu(
            containerColor = White,
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(.85f)
        ) {
            itemList.forEach { item ->
                DropdownMenuItem(
                    colors = MenuDefaults.itemColors(
                        leadingIconColor = White,
                        trailingIconColor = White
                    ),
                    text = {
                        Column {
                            Text(
                                text = itemLabel(item)
                            )
                            if(itemDetail != null){
                                Text(
                                    text = itemDetail(item),
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                           },
                    onClick = {
                        currentSelection = item
                        expanded = false
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun SimpleSelectorPreview() {
    val items = listOf("Option 1", "Option 2", "Option 3")
    SimpleSelector(
        placeholder = "Select an option",
        itemList = items,
        onItemSelected = {},
        itemLabel = { it },
        errorMessage = "Campo requerido"
    )
}