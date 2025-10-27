package com.rpalmar.financialapp.mock

import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.EnvelopeDomain
import com.rpalmar.financialapp.models.EnvelopStatus
import com.rpalmar.financialapp.models.GoalType
import com.rpalmar.financialapp.models.RateMode
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import java.util.Date
import java.util.UUID

/**
 * Proveedor de datos mock para pruebas de UI y lógica
 */
object MockupProvider {

    fun getMockCurrencies(): List<CurrencyDomain> {
        return listOf(
            CurrencyDomain(
                id = 1L,
                name = "US Dollar",
                ISO = "USD",
                symbol = "$",
                exchangeRate = 1.0,
                mainCurrency = true,
                rateMode = RateMode.MANUAL
            ),
            CurrencyDomain(
                id = 2L,
                name = "Euro",
                ISO = "EUR",
                symbol = "€",
                exchangeRate = 0.92,
                mainCurrency = false,
                rateMode = RateMode.MANUAL
            ),
            CurrencyDomain(
                id = 3L,
                name = "Japanese Yen",
                ISO = "JPY",
                symbol = "¥",
                exchangeRate = 145.3,
                mainCurrency = false,
                rateMode = RateMode.MANUAL
            ),
            CurrencyDomain(
                id = 4L,
                name = "Mexican Peso",
                ISO = "MXN",
                symbol = "$",
                exchangeRate = 17.1,
                mainCurrency = false,
                rateMode = RateMode.MANUAL
            )
        )
    }

    fun getMockAccounts(): List<AccountDomain> {
        val currencies = getMockCurrencies()
        return listOf(
            AccountDomain(
                id = 101L,
                name = "Personal Checking",
                description = "Main account for daily expenses",
                balance = 2450.75,
                balanceInMainCurrency = 5161.65,
                currency = currencies[0], // USD
                style = StyleEntity(
                    color = "#E3F2FD",
                    icon = "ic_account_balance_wallet"
                )
            ),
            AccountDomain(
                id = 102L,
                name = "Savings Account",
                description = "Emergency fund and savings",
                balance = 12000.00,
                balanceInMainCurrency = 5161.65,
                currency = currencies[0], // USD
                style = StyleEntity(
                    color = "#1B5E20",
                    icon = "ic_savings"
                )
            ),
            AccountDomain(
                id = 103L,
                name = "Travel Fund",
                description = "Money saved for trips",
                balance = 3500.00,
                balanceInMainCurrency = 5161.65,
                currency = currencies[1], // EUR
                style = StyleEntity(
                    color = "#FFF3E0",
                    icon = "ic_flight"
                )
            ),
            AccountDomain(
                id = 104L,
                name = "Investments",
                description = "Stock and ETF portfolio",
                balance = 580000.0,
                balanceInMainCurrency = 5161.65,
                currency = currencies[2], // JPY
                style = StyleEntity(
                    color = "#F3E5F5",
                    icon = "ic_trending_up"
                )
            ),
            AccountDomain(
                id = 105L,
                name = "Business Checking",
                description = "Company operations account",
                balance = 185000.25,
                balanceInMainCurrency = 5161.65,
                currency = currencies[3], // MXN
                style = StyleEntity(
                    color = "#FFEBEE",
                    icon = "ic_business"
                )
            )
        )
    }

    fun getMockEnvelopes(): List<EnvelopeDomain> {
        val currencies = getMockCurrencies()
        return listOf(
            EnvelopeDomain(
                id = 201L,
                name = "Groceries",
                description = "Monthly groceries and household supplies",
                balance = 300.0,
                status = EnvelopStatus.ACTIVE,
                style = StyleEntity(
                    color = "#FFFDE7",
                    icon = "ic_shopping_cart"
                ),
                goalAmount = 400.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = null,
                parentEnvelopID = null,
                currency = currencies[0],
                balanceInMainCurrency = TODO() // USD
            ),
            EnvelopeDomain(
                id = 202L,
                name = "Vacation",
                description = "Family summer trip savings",
                balance = 1500.0,
                status = EnvelopStatus.ACTIVE,
                style = StyleEntity(
                    color = "#006064",
                    icon = "ic_beach_access"
                ),
                goalAmount = 5000.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = 20251231.0,
                parentEnvelopID = null,
                currency = currencies[1],
                balanceInMainCurrency = TODO() // EUR
            ),
            EnvelopeDomain(
                id = 203L,
                name = "Emergency Fund",
                description = "Backup money for unexpected expenses",
                balance = 2500.0,
                status = EnvelopStatus.ACTIVE,
                style = StyleEntity(
                    color = "#F3E5F5",
                    icon = "ic_health_and_safety"
                ),
                goalAmount = 10000.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = null,
                parentEnvelopID = null,
                currency = currencies[0],
                balanceInMainCurrency = TODO() // USD
            ),
            EnvelopeDomain(
                id = 204L,
                name = "Entertainment",
                description = "Movies, restaurants, subscriptions",
                balance = 200.0,
                status = EnvelopStatus.ACTIVE,
                style = StyleEntity(
                    color = "#1B5E20",
                    icon = "ic_local_activity"
                ),
                goalAmount = 300.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = null,
                parentEnvelopID = null,
                currency = currencies[3],
                balanceInMainCurrency = TODO() // MXN
            )
        )
    }

    fun getMockTransactions(): List<TransactionDomain> {
        val accounts = getMockAccounts()
        val currencies = getMockCurrencies()

        // Crear SimpleTransactionSourceAux a partir de las cuentas
        val accountSources = accounts.map { acc ->
            SimpleTransactionSourceAux(
                id = acc.id,
                name = acc.name,
                description = acc.description,
                transactionEntityType = TransactionSourceType.ACCOUNT,
                currency = acc.currency,
                balance = acc.balance
            )
        }

        val transactions = mutableListOf<TransactionDomain>()

        // Generar transacciones simples (INCOME / EXPENSE)
        for (i in 1..10) {
            val source = accountSources.random()
            val type = if (i % 2 == 0) TransactionType.INCOME else TransactionType.EXPENSE
            val currency = currencies[1]
            val amount = (50..1000).random() + (0..99).random() / 100.0
            val date = Date(System.currentTimeMillis() - (0..30).random() * 24 * 60 * 60 * 1000L)

            transactions.add(
                TransactionDomain(
                    id = i.toLong(),
                    transactionCode = UUID.randomUUID(),
                    source = source,
                    amount = amount,
                    amountInBaseCurrency = amount / currency.exchangeRate,
                    transactionType = type,
                    transactionDate = date,
                    currency = currency,
                    exchangeRate = currency.exchangeRate,
                    description = "Mock ${type.name.lowercase()} transaction $i"
                )
            )
        }

        // Generar una transferencia con linkedTransaction
        val sourceAccount = accountSources[0]
        val destAccount = accountSources[1]
        val currencySource = currencies[0]
        val currencyDest = currencies[1]

        val transferAmount = 300.0
        val transferDate = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L)

        val linkedTransaction = TransactionDomain(
            id = 100L,
            transactionCode = UUID.randomUUID(),
            source = destAccount,
            amount = transferAmount * currencyDest.exchangeRate / currencySource.exchangeRate,
            amountInBaseCurrency = transferAmount / currencyDest.exchangeRate,
            transactionType = TransactionType.INCOME,
            transactionDate = transferDate,
            currency = currencyDest,
            exchangeRate = currencyDest.exchangeRate,
            description = "Linked transfer to ${destAccount.name}"
        )

        val mainTransaction = TransactionDomain(
            id = 101L,
            transactionCode = UUID.randomUUID(),
            source = sourceAccount,
            amount = transferAmount,
            amountInBaseCurrency = transferAmount / currencySource.exchangeRate,
            transactionType = TransactionType.EXPENSE,
            transactionDate = transferDate,
            currency = currencySource,
            exchangeRate = currencySource.exchangeRate,
            description = "Transfer to ${destAccount.name}",
            linkedTransaction = linkedTransaction
        )

        transactions.add(linkedTransaction)
        transactions.add(mainTransaction)

        return transactions
    }

}
