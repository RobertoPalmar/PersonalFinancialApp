package com.rpalmar.financialapp.views.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rpalmar.financialapp.views.ui.theme.*

@Composable
fun ColorPicker(
    initialColor: String,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
       Orange, Yellow, DarkBlue, Blue, Green, PurpleBlue, Purple, Pink, Red
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedColor by remember {
        mutableStateOf(
            try {
                Color(android.graphics.Color.parseColor(initialColor))
            } catch (e: Exception) {
                colors.first()
            }
        )
    }

    LaunchedEffect(initialColor) {
        try {
            selectedColor = Color(android.graphics.Color.parseColor(initialColor))
        } catch (e: Exception) {
            // Fallback or log error if needed
        }
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
                text = "Color",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = Black
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(selectedColor, CircleShape)
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
                        Text("Select a color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal)
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                        ) {
                            items(colors) { color ->
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .padding(4.dp)
                                        .background(color, CircleShape)
                                        .clickable {
                                            selectedColor = color
                                            onColorSelected(color)
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
fun PickerColorPreview(){
    FinancialTheme {
        ColorPicker(
            initialColor = "",
            onColorSelected = {}
        )
    }
}
