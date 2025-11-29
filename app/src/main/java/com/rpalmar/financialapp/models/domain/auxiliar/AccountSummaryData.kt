package com.rpalmar.financialapp.models.domain.auxiliar

import com.rpalmar.financialapp.models.domain.AccountDomain

data class AccountSummaryData(
    val account: AccountDomain,
    val income: Double,
    val expense: Double
)