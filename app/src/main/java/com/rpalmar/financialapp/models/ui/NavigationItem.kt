package com.rpalmar.financialapp.models.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val subtitle: String,
    val buttonName: String,
    val mainColor: Color,
    val backgroundImage: ImageVector,
    val onNavigate: () -> Unit
)
