package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.auxiliar.AccountSummaryData
import com.rpalmar.financialapp.models.domain.auxiliar.DashboardData
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import com.rpalmar.financialapp.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAccountSummaryDataUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    val LOG_TAG = "GetAccountSummaryDataUseCase"

    operator fun invoke(account: AccountDomain): AccountSummaryData?{
        try {
            Log.i(LOG_TAG, "📊 Start get account summary data")

            //GET ACCOUNT MONTH TRANSACTIONS
            val currentMonthDate = Utils.getCurrentMonthDateRange()
            val currentAccountMonthTransactionList =
                transactionRepository.getTransactionWithCurrencyByDateRangeAndAccountID(currentMonthDate.startDate, currentMonthDate.endDate, account.id)

            // CALCULATE INCOME
            val incomeBalance = currentAccountMonthTransactionList
                .filter { it.transaction.transactionType == TransactionType.INCOME }
                .sumOf { it.transaction.amount }

            // CALCULATE EXPENSE
            val expenseBalance = currentAccountMonthTransactionList
                .filter { it.transaction.transactionType == TransactionType.EXPENSE }
                .sumOf { it.transaction.amount }

            // RESULT
            val accountSummaryData = AccountSummaryData(
                account = account,
                income = incomeBalance,
                expense = expenseBalance
            )

            Log.i(LOG_TAG, "📊 finish get summary account data: $accountSummaryData")
            return accountSummaryData
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.message.toString())
            return null
        }
    }
}