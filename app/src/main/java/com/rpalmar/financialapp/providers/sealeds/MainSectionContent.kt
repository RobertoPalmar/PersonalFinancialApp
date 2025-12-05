package com.rpalmar.financialapp.providers.sealeds

import androidx.compose.runtime.saveable.Saver
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain

sealed class MainSectionContent {
    object Home : MainSectionContent()
    object Accounts : MainSectionContent()
    data class AccountDetail(val account: AccountDomain) : MainSectionContent()
    object Currencies: MainSectionContent()
    data class CurrencyDetail(val currency: CurrencyDomain) : MainSectionContent()
    object Categories: MainSectionContent()
    data class CategoryDetail(val category: CategoryDomain) : MainSectionContent()
    object Transactions : MainSectionContent()
    data class TransactionDetail(val transaction: TransactionDomain) : MainSectionContent()

    companion object {
        val Saver: Saver<MainSectionContent, String> = Saver(
            save = { section ->
                when (section) {
                    is Home -> "home"
                    is Accounts -> "accounts"
                    is AccountDetail -> "account_detail_${section.account.id}"
                    is Currencies -> "currencies"
                    is CurrencyDetail -> "currency_detail_${section.currency.id}"
                    is Categories -> "categories"
                    is CategoryDetail -> "category_detail_${section.category.id}"
                    is Transactions -> "transactions"
                    is TransactionDetail -> "transaction_detail_${section.transaction.id}"
                }
            },
            restore = { saved ->
                when {
                    saved == "home" -> Home
                    saved == "accounts" -> Accounts
                    saved.startsWith("account_detail_") -> Accounts
                    saved == "currencies" -> Currencies
                    saved.startsWith("currency_detail_") -> Currencies
                    saved == "categories" -> Categories
                    saved.startsWith("category_detail_") -> Categories
                    saved == "transactions" -> Transactions
                    saved.startsWith("transaction_detail_") -> Transactions
                    else -> Home
                }
            }
        )
    }
}
