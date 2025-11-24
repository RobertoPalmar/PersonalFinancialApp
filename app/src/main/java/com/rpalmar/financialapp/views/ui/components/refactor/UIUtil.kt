package com.rpalmar.financialapp.views.ui.components.refactor

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.providers.sealeds.MainSectionContent
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Red
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.BoltSolid
import compose.icons.lineawesomeicons.CarSolid
import compose.icons.lineawesomeicons.GiftSolid
import compose.icons.lineawesomeicons.HeartSolid
import compose.icons.lineawesomeicons.HomeSolid
import compose.icons.lineawesomeicons.MoneyBillWaveSolid
import compose.icons.lineawesomeicons.PlaneSolid
import compose.icons.lineawesomeicons.QuestionSolid
import compose.icons.lineawesomeicons.ShoppingBagSolid
import compose.icons.lineawesomeicons.UtensilsSolid
import compose.icons.lineawesomeicons.WalletSolid
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

fun formatAmount(amount: Double, currencySymbol: String? = null): String {
    if (currencySymbol == null) return amount.toString()
    return "$currencySymbol " + String.format("%,.2f", amount)
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(date)
}

fun Color.toHex(): String {
    return "#%02X%02X%02X".format(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

fun String.toColor(): Color{
    val cleanHex = this.removePrefix("#")

    return when (cleanHex.length) {
        6 -> { // RRGGBB
            val r = cleanHex.substring(0, 2).toInt(16)
            val g = cleanHex.substring(2, 4).toInt(16)
            val b = cleanHex.substring(4, 6).toInt(16)
            Color(r, g, b)
        }
        8 -> { // AARRGGBB
            val a = cleanHex.substring(0, 2).toInt(16)
            val r = cleanHex.substring(2, 4).toInt(16)
            val g = cleanHex.substring(4, 6).toInt(16)
            val b = cleanHex.substring(6, 8).toInt(16)
            Color(r, g, b, a)
        }
        else -> Color.Gray // fallback
    }
}

object IconMapper {
    private val icons = mapOf(
        "WalletSolid" to LineAwesomeIcons.WalletSolid,
        "ShoppingBagSolid" to LineAwesomeIcons.ShoppingBagSolid,
        "UtensilsSolid" to LineAwesomeIcons.UtensilsSolid,
        "CarSolid" to LineAwesomeIcons.CarSolid,
        "HomeSolid" to LineAwesomeIcons.HomeSolid,
        "HeartSolid" to LineAwesomeIcons.HeartSolid,
        "PlaneSolid" to LineAwesomeIcons.PlaneSolid,
        "MoneyBillWaveSolid" to LineAwesomeIcons.MoneyBillWaveSolid,
        "GiftSolid" to LineAwesomeIcons.GiftSolid,
        "BoltSolid" to LineAwesomeIcons.BoltSolid
    )

    fun fromName(name: String) = icons[name] ?: LineAwesomeIcons.QuestionSolid
}




