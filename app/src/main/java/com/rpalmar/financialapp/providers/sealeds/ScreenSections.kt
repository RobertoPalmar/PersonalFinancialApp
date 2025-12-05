package com.rpalmar.financialapp.providers.sealeds

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.HomeSolid
import compose.icons.lineawesomeicons.TagSolid
import compose.icons.lineawesomeicons.CoinsSolid
import compose.icons.lineawesomeicons.User
import compose.icons.lineawesomeicons.UserSolid

sealed class ScreenSections(val route: String, val label: String, val icon: ImageVector) {
    object Home : ScreenSections("home", "Inicio", LineAwesomeIcons.HomeSolid)
    object AccountForm : ScreenSections("accountForm", "AccountForm", LineAwesomeIcons.UserSolid)
    object CategoryForm : ScreenSections("categoryForm", "CategoryForm", LineAwesomeIcons.TagSolid)
    object CurrencyForm : ScreenSections("currencyForm", "CurrencyForm", LineAwesomeIcons.CoinsSolid)
    object TransactionForm : ScreenSections("transactionForm", "TransactionForm", LineAwesomeIcons.UserSolid)
//    object Stats : ScreenSections("stats", "Estadísticas", LineAwesomeIcons.ChartPieSolid)
//    object Settings : ScreenSections("settings", "Ajustes", LineAwesomeIcons.ToolsSolid)
}
