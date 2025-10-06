package com.rpalmar.financialapp.views.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.views.account.AccountDetailScreen
import com.rpalmar.financialapp.views.account.AccountFormScreen
import com.rpalmar.financialapp.views.account.AccountListScreen
import com.rpalmar.financialapp.views.currency.CurrencyScreen
import com.rpalmar.financialapp.views.envelopes.EnvelopeScreen
import com.rpalmar.financialapp.views.mainMenu.MainMenuScreen
import com.rpalmar.financialapp.views.transaction.TransactionFormScreen
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_menu",
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(300)) }
    ){
        composable("main_menu"){
            MainMenuScreen(
                onNavigateToCurrencies = { navController.navigate("currencies") },
                onNavigateToAccounts = { navController.navigate("account_flow") },
                onNavigateToEnvelopes = { navController.navigate("envelopes") }
            )
        }

        navigation(
            startDestination = "accounts",
            route = "account_flow"
        ){
            composable("accounts"){
                AccountListScreen(
                    navController = navController,
                    onNavigateToForm = { navController.navigate("accountForm") },
                    onNavigateToAccountDetail = { navController.navigate("accountDetails/${it}") },
                    onBackPressed = { navController.popBackStack() },
                )
            }
            composable("accountForm"){
                AccountFormScreen(
                    navController = navController,
                    onBackPressed = { navController.popBackStack() }
                )
            }
            composable("accountDetails/{accountId}"){
                val accountId = it.arguments?.getString("accountId")?.toLong()
                AccountDetailScreen(
                    navController = navController,
                    accountId = accountId!!,
                    onNavigateToTransactionForm = { navController.navigate("transactionForm/${it}") },
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }

        composable("envelopes"){
            EnvelopeScreen()
        }
        composable("currencies"){
            CurrencyScreen()
        }

        composable("transactionForm/{accountId}/{transactionType}"){
            val accountId = it.arguments?.getString("accountId")?.toLong()
            val transactionTypeString = it.arguments?.getString("transactionType");
            val transactionTypeEnum = TransactionType.valueOf(transactionTypeString!!)
            TransactionFormScreen(
                transactionType = transactionTypeEnum,
                accountId = accountId!!,
                onBackPressed = { navController.popBackStack() }
            )
        }

    }
}


