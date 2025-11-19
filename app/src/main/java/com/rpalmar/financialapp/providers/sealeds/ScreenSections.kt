package com.rpalmar.financialapp.providers.sealeds

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ChargingStationSolid
import compose.icons.lineawesomeicons.HomeSolid
import compose.icons.lineawesomeicons.ToolsSolid

sealed class ScreenSections(val route: String, val label: String, val icon: ImageVector) {
    object Home : ScreenSections("home", "Inicio", LineAwesomeIcons.HomeSolid)
    object Stats : ScreenSections("stats", "Estadísticas", LineAwesomeIcons.ChargingStationSolid)
    object Settings : ScreenSections("settings", "Ajustes", LineAwesomeIcons.ToolsSolid)
}
