package com.rpalmar.financialapp.views.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.sealeds.ScreenSections
import com.rpalmar.financialapp.views.AppViewModel
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.category.CategoryFormScreen
import com.rpalmar.financialapp.views.category.data.CategoryViewModel
import com.rpalmar.financialapp.views.currency.CurrencyFormScreen
import com.rpalmar.financialapp.views.currency.data.CurrencyViewModel
import com.rpalmar.financialapp.views.mainMenu.MainMenuScreen
import com.rpalmar.financialapp.views.transaction.data.TransactionViewModel
import com.rpalmar.financialapp.views.ui.components.AccountFormScreen
import com.rpalmar.financialapp.views.ui.components.TransactionFormScreen
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)

        setContent {
            FinancialTheme(darkTheme = false) {
                AppNavigation()
            }
        }
    }
}

val LocalMainCurrency = staticCompositionLocalOf<CurrencyDomain?> { null }

@Composable
fun AppNavigation() {
    val appViewModel: AppViewModel = hiltViewModel()
    val navController = rememberNavController()

    val mainCurrency by appViewModel.mainCurrency.collectAsState()

    CompositionLocalProvider(
        LocalMainCurrency provides mainCurrency
    ) {
        NavHost(
            navController = navController,
            startDestination = ScreenSections.Home.route,
        ) {
            //MAIN MENU
            composable(ScreenSections.Home.route) { backStackEntry ->
                val accountViewModel: AccountViewModel = hiltViewModel(backStackEntry)
                val transactionViewModel : TransactionViewModel = hiltViewModel(backStackEntry)
                val categoryViewModel: CategoryViewModel = hiltViewModel(backStackEntry)
                val currencyViewModel: CurrencyViewModel = hiltViewModel(backStackEntry)

                MainMenuScreen(
                    navController = navController,
                    accountViewModel = accountViewModel,
                    transactionViewModel = transactionViewModel,
                    categoryViewModel = categoryViewModel,
                    currencyViewModel = currencyViewModel
                )
            }

            //ACCOUNT FORM
            composable(ScreenSections.AccountForm.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {navController.getBackStackEntry(ScreenSections.Home.route)}
                val accountViewModel: AccountViewModel = hiltViewModel(parentEntry)

                AccountFormScreen(
                    navController = navController,
                    accountViewModel = accountViewModel
                )
            }

            //CATEGORY FORM
            composable(ScreenSections.CategoryForm.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(ScreenSections.Home.route) }
                val categoryViewModel: CategoryViewModel = hiltViewModel(parentEntry)

                CategoryFormScreen(
                    navController = navController,
                    categoryViewModel = categoryViewModel
                )
            }

            //CURRENCY FORM
            composable(ScreenSections.CurrencyForm.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(ScreenSections.Home.route) }
                val currencyViewModel: CurrencyViewModel = hiltViewModel(parentEntry)

                CurrencyFormScreen(
                    navController = navController,
                    currencyViewModel = currencyViewModel
                )
            }

            //TRANSACTION FORM
            composable(ScreenSections.TransactionForm.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(ScreenSections.Home.route) }
                val transactionViewModel: TransactionViewModel = hiltViewModel(parentEntry)

                TransactionFormScreen(
                    navController = navController,
                    transactionViewModel = transactionViewModel
                )
            }

        }
    }
}
