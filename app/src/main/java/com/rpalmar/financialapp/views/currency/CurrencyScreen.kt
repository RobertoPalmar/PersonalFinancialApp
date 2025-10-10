//package com.rpalmar.financialapp.views.currency
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.vectorResource
//import androidx.compose.ui.unit.dp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import com.rpalmar.financialapp.R
//import com.rpalmar.financialapp.views.account.AccountListSection
//import com.rpalmar.financialapp.views.account.ButtonsSection
//import com.rpalmar.financialapp.views.account.SearchBarSection
//import com.rpalmar.financialapp.views.account.data.AccountViewModel
//import com.rpalmar.financialapp.views.ui.componentes.MainLayout
//import com.rpalmar.financialapp.views.ui.componentes.SummarySection
//import com.rpalmar.financialapp.views.ui.theme.Blue
//
//@Composable
//fun CurrencyScreen(
//    navController: NavHostController,
//    onNavigateToForm: () -> Unit,
//    onNavigateToCurrencyDetail: (Long) -> Unit,
//    onBackPressed: () -> Unit
//) {
//    //SET UP VIEW MODEL
//    val backStackEntry = remember(navController.currentBackStackEntry) { navController.getBackStackEntry("currency_flow") }
//    val viewModel: CurrencyV = hiltViewModel(backStackEntry)
//
//    //ACCOUNT STATE DATA
//    val accountFormState = viewModel.accountUIState.collectAsState()
//
//    //LOAD ACCOUNT LIST
//    LaunchedEffect(true) {
//        viewModel.loadAccountLisData()
//    }
//
//    var totalAccountBalanceFormated = String.format("%.2f", accountFormState.value.totalAccountBalance)
//
//    MainLayout {
//        SummarySection(
//            sectionName = "Accounts",
//            totalEntities = accountFormState.value.accountList.size,
//            mainSummaryData = "Total Balance: ${totalAccountBalanceFormated} ${accountFormState.value.mainCurrency?.symbol ?: ""}",
//            mainColor = Blue,
//            icon = ImageVector.vectorResource(id = R.drawable.ic_wallet)
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        SearchBarSection()
//        Spacer(modifier = Modifier.height(3.dp))
//        ButtonsSection(onNavigateToForm = onNavigateToForm)
//
//        if (accountFormState.value.isLoading) {
//            Column(
//                modifier = Modifier.fillMaxSize(1f),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Spacer(modifier = Modifier.weight(.7f))
//                CircularProgressIndicator(
//                    strokeWidth = 4.dp,
//                    modifier = Modifier.size(50.dp)
//                )
//                Spacer(modifier = Modifier.weight(1f))
//            }
//        } else if (accountFormState.value.accountList.isEmpty()) {
//            Column(
//                modifier = Modifier.fillMaxSize(1f),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Spacer(modifier = Modifier.weight(.7f))
//                Text(
//                    text = "No Data",
//                    style = MaterialTheme.typography.titleMedium,
//                )
//                Spacer(modifier = Modifier.weight(1f))
//            }
//        } else {
//            AccountListSection(
//                viewModel = viewModel,
//                mainCurrency = accountFormState.value.mainCurrency!!,
//                onNavigateToAccountDetails = { onNavigateToAccountDetail(it) }
//            )
//        }
//    }
//}