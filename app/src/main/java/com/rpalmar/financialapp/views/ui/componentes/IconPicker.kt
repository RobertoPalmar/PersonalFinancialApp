package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.views.ui.theme.Black
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme

@Composable
fun IconPicker(
    initialIcon: String,
    onIconSelected: (String) -> Unit
) {
    val icons = listOf(
        "ic_account" to R.drawable.ic_account,
        "ic_currency" to R.drawable.ic_currency,
        "ic_envelope" to R.drawable.ic_envelope,
        "ic_financial" to R.drawable.ic_financial,
        "ic_plus" to R.drawable.ic_plus,
        "ic_transactions" to R.drawable.ic_transactions,
        "ic_wallet" to R.drawable.ic_wallet
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedIcon by remember {
        mutableStateOf(icons.find { it.first == initialIcon } ?: icons.first())
    }

    LaunchedEffect(initialIcon) {
        selectedIcon = icons.find { it.first == initialIcon } ?: icons.first()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, DarkGrey),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Text(
                text ="Icon",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = Black
            )
            Icon(
                painter = painterResource(id = selectedIcon.second),
                contentDescription = selectedIcon.first,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { showDialog = true }
            )
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select a icon", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal)
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                        ){
                            items(icons) { icon ->
                                Icon(
                                    painter = painterResource(id = icon.second),
                                    contentDescription = icon.first,
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(4.dp)
                                        .clickable {
                                            selectedIcon = icon
                                            onIconSelected(icon.first)
                                            showDialog = false
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PickerItemPreview() {
    FinancialTheme {
        IconPicker(
            initialIcon = "",
            onIconSelected = {}
        )
    }
}
