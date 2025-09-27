package com.rpalmar.financialapp.models.domain.auxiliar

import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain

data class AccountDashboardData(
    var accountList: List<AccountDomain>,
    var mainCurrency: CurrencyDomain,
    var totalAccountBalance: Double,
)
