package com.rpalmar.financialapp.views.mainMenu.data

import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.AccountSummaryData
import com.rpalmar.financialapp.models.domain.auxiliar.DashboardData

data class MainMenuUIState(
    //DASHBOARD DATA
    var dashboardData: DashboardData = DashboardData(
        totalBalance = 0.0,
        generalIncome = 0.0,
        generalExpense = 0.0
    ),

    //ACCOUNT SUMMARY DATA
    var currentAccount: AccountDomain? = null,
    var accountSummaryData: AccountSummaryData? = null,

//    //ENTITY LIST DATA
//    var accountList: List<AccountDomain> = emptyList(),
//    var transactionList: List<TransactionDomain> = emptyList(),
//    var categoryList: List<CategoryDomain> = emptyList(),
//    var currencyList: List<CurrencyDomain> = emptyList(),

    //AUX DATA
    var isLoading: Boolean = true,
)
