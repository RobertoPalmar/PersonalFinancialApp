package com.rpalmar.financialapp.views.ui.componentes.refactor

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Red
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun transactionColor(type: TransactionType): Color {
    return when (type) {
        TransactionType.INCOME -> Green
        TransactionType.EXPENSE -> Red
        TransactionType.TRANSFER -> Color(0xFF2D8EFF) // azul moderno
        TransactionType.ADJUSTMENT -> Color(0xFFFF9800) // naranja
    }
}

fun formatAmount(amount: Double, currencySymbol: String): String {
    return "$currencySymbol " + String.format("%,.2f", amount)
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(date)
}


