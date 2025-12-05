package com.rpalmar.financialapp.providers.sealeds

import androidx.compose.runtime.saveable.Saver
import com.rpalmar.financialapp.models.domain.AccountDomain

sealed class MainSectionContent {
    object Home : MainSectionContent()
    object Accounts : MainSectionContent()
    data class AccountDetail(val account: AccountDomain) : MainSectionContent()
    object Transactions : MainSectionContent()
    object Categories: MainSectionContent()
    object Currencies: MainSectionContent()
    
    companion object {
        val Saver: Saver<MainSectionContent, String> = Saver(
            save = { section ->
                when (section) {
                    is Home -> "home"
                    is Accounts -> "accounts"
                    is Transactions -> "transactions"
                    is Categories -> "categories"
                    is Currencies -> "currencies"
                    is AccountDetail -> "account_detail_${section.account.id}"
                }
            },
            restore = { saved ->
                when {
                    saved == "home" -> Home
                    saved == "accounts" -> Accounts
                    saved.startsWith("account_detail_") -> Accounts
                    saved == "transactions" -> Transactions
                    saved == "categories" -> Categories
                    saved == "currencies" -> Currencies
                    else -> Home
                }
            }
        )
    }
}
