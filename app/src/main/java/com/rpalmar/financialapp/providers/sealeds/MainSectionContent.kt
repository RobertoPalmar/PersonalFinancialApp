package com.rpalmar.financialapp.providers.sealeds

import com.rpalmar.financialapp.models.domain.AccountDomain

sealed class MainSectionContent {
    object Home : MainSectionContent()
    object Accounts : MainSectionContent()
    data class AccountDetail(val account: AccountDomain) : MainSectionContent()
    object Transactions : MainSectionContent()
    object Categories: MainSectionContent()
    object Currencies: MainSectionContent()
}
