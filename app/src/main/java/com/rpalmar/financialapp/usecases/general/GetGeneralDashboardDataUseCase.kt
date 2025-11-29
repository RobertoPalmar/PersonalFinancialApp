package com.rpalmar.financialapp.usecases.general

import android.util.Log
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.auxiliar.DashboardData
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import com.rpalmar.financialapp.utils.Utils
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetGeneralDashboardDataUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    val LOG_TAG = "GetGeneralDashboardDataUseCase"

    suspend operator fun invoke(): DashboardData? {
        try {
            Log.i(LOG_TAG, "📊 Start get dashboard data")

            // GET ACCOUNTS
            val accountList = accountRepository.getAccountListWithCurrency().first()

            //GET MONTH TRANSACTIONS
            val currentMonthDate = Utils.getCurrentMonthDateRange()
            val currentMonthTransactionList =
                transactionRepository.getTransactionWithCurrencyByDateRange(currentMonthDate.startDate, currentMonthDate.endDate)

            // CALCULATE GENERAL BALANCE
            val totalBalance =
                accountList.sumOf { it.account.balance / it.currency.currentExchangeRate }

            // CALCULATE INCOME
            val incomeBalance = currentMonthTransactionList
                .filter { it.transaction.transactionType == TransactionType.INCOME }
                .sumOf { it.transaction.amount / it.currency.currentExchangeRate }

            // CALCULATE EXPENSE
            val expenseBalance = currentMonthTransactionList
                .filter { it.transaction.transactionType == TransactionType.EXPENSE }
                .sumOf { it.transaction.amount / it.currency.currentExchangeRate }

            // RESULT
            val dashboardData = DashboardData(
                totalBalance = totalBalance,
                generalIncome = incomeBalance,
                generalExpense = expenseBalance
            )

            Log.i(LOG_TAG, "📊 finish get dashboard data: $dashboardData")
            return dashboardData
        } catch (ex: Exception) {
            Log.e(LOG_TAG, ex.message.toString())
            return null
        }
    }
}