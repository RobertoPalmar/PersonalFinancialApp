package com.rpalmar.financialapp.views.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.providers.sealeds.ScreenSections
import com.rpalmar.financialapp.views.account.AccountDetailsScreen
import com.rpalmar.financialapp.views.transaction.TransactionFormScreen
import com.rpalmar.financialapp.views.ui.componentes.refactor.BottomNavBar
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

    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            NavHost(
                navController = navController,
                startDestination = ScreenSections.Home,
            ) {
                composable(ScreenSections.Home.route) {}
                composable(ScreenSections.Home.route) { AccountDetailsScreen() }
            }

//        navigation(
//            startDestination = "accounts",
//            route = "account_flow"
//        ){
//            composable("accounts"){
//                AccountListScreen(
//                    navController = navController,
//                    onNavigateToForm = { navController.navigate("accountForm") },
//                    onNavigateToAccountDetail = { navController.navigate("accountDetails/${it}") },
//                    onBackPressed = { navController.popBackStack() },
//                )
//            }
//            composable("accountForm"){
//                AccountFormScreen(
//                    navController = navController,
//                    onBackPressed = { navController.popBackStack() }
//                )
//            }
//            composable("accountDetails/{accountId}"){
//                val accountId = it.arguments?.getString("accountId")?.toLong()
//                AccountDetailScreen(
//                    navController = navController,
//                    accountId = accountId!!,
//                    onBackPressed = { navController.popBackStack() }
//                )
//            }
//        }
//
//                composable(
//                    route = "transactionForm/{transactionType}?sourceType={sourceType}&sourceId={sourceId}",
//                    arguments = listOf(
//                        navArgument("sourceType") {
//                            type = NavType.StringType
//                            nullable = true
//                            defaultValue = null
//                        },
//                        navArgument("sourceId") {
//                            type = NavType.StringType
//                            nullable = true
//                            defaultValue = null
//                        }
//                    )
//                ){
//                    val transactionTypeEnum = TransactionType.valueOf(it.arguments?.getString("transactionType")!!)
//                    TransactionFormScreen(
//                        transactionType = transactionTypeEnum,
//                        onBackPressed = { navController.popBackStack() }
//                    )
//                }
//
//            }
//        }
        }
    }
}


