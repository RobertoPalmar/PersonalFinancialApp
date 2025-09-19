package com.rpalmar.financialapp.views.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = White,
    secondary = Green,
    tertiary = Orange,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black
)

@Composable
fun FinancialTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
){
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content,
        shapes = Shapes,
    )
}