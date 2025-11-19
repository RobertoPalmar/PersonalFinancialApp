package com.rpalmar.financialapp.views.ui.componentes.refactor

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rpalmar.financialapp.models.NavScreen
import com.rpalmar.financialapp.providers.sealeds.ScreenSections

@Composable
fun BottomNavBar(navController: NavHostController) {

    val screens = listOf(
        ScreenSections.Home,
        ScreenSections.Stats,
        ScreenSections.Settings
    )

    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}
