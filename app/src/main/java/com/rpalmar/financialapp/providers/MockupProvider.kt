package com.rpalmar.financialapp.mock

import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.EnvelopeDomain
import com.rpalmar.financialapp.models.EnvelopStatus
import com.rpalmar.financialapp.models.GoalType

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
                currencyPriority = 1
            ),
            CurrencyDomain(
                id = 2L,
                name = "Euro",
                ISO = "EUR",
                symbol = "€",
                exchangeRate = 0.92,
                currencyPriority = 2
            ),
            CurrencyDomain(
                id = 3L,
                name = "Japanese Yen",
                ISO = "JPY",
                symbol = "¥",
                exchangeRate = 145.3,
                currencyPriority = 3
            ),
            CurrencyDomain(
                id = 4L,
                name = "Mexican Peso",
                ISO = "MXN",
                symbol = "$",
                exchangeRate = 17.1,
                currencyPriority = 4
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
                currency = currencies[0], // USD
                style = StyleEntity(
                    backgroundColor = "#E3F2FD",
                    textColor = "#0D47A1",
                    icon = "ic_account_balance_wallet"
                )
            ),
            AccountDomain(
                id = 102L,
                name = "Savings Account",
                description = "Emergency fund and savings",
                balance = 12000.00,
                currency = currencies[0], // USD
                style = StyleEntity(
                    backgroundColor = "#E8F5E9",
                    textColor = "#1B5E20",
                    icon = "ic_savings"
                )
            ),
            AccountDomain(
                id = 103L,
                name = "Travel Fund",
                description = "Money saved for trips",
                balance = 3500.00,
                currency = currencies[1], // EUR
                style = StyleEntity(
                    backgroundColor = "#FFF3E0",
                    textColor = "#E65100",
                    icon = "ic_flight"
                )
            ),
            AccountDomain(
                id = 104L,
                name = "Investments",
                description = "Stock and ETF portfolio",
                balance = 580000.0,
                currency = currencies[2], // JPY
                style = StyleEntity(
                    backgroundColor = "#F3E5F5",
                    textColor = "#4A148C",
                    icon = "ic_trending_up"
                )
            ),
            AccountDomain(
                id = 105L,
                name = "Business Checking",
                description = "Company operations account",
                balance = 185000.25,
                currency = currencies[3], // MXN
                style = StyleEntity(
                    backgroundColor = "#FFEBEE",
                    textColor = "#B71C1C",
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
                    backgroundColor = "#FFFDE7",
                    textColor = "#F57F17",
                    icon = "ic_shopping_cart"
                ),
                goalAmount = 400.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = null,
                parentEnvelopID = null,
                currency = currencies[0] // USD
            ),
            EnvelopeDomain(
                id = 202L,
                name = "Vacation",
                description = "Family summer trip savings",
                balance = 1500.0,
                status = EnvelopStatus.ACTIVE,
                style = StyleEntity(
                    backgroundColor = "#E0F7FA",
                    textColor = "#006064",
                    icon = "ic_beach_access"
                ),
                goalAmount = 5000.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = 20251231.0,
                parentEnvelopID = null,
                currency = currencies[1] // EUR
            ),
            EnvelopeDomain(
                id = 203L,
                name = "Emergency Fund",
                description = "Backup money for unexpected expenses",
                balance = 2500.0,
                status = EnvelopStatus.ACTIVE,
                style = StyleEntity(
                    backgroundColor = "#F3E5F5",
                    textColor = "#4A148C",
                    icon = "ic_health_and_safety"
                ),
                goalAmount = 10000.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = null,
                parentEnvelopID = null,
                currency = currencies[0] // USD
            ),
            EnvelopeDomain(
                id = 204L,
                name = "Entertainment",
                description = "Movies, restaurants, subscriptions",
                balance = 200.0,
                status = EnvelopStatus.ACTIVE,
                style = StyleEntity(
                    backgroundColor = "#E8F5E9",
                    textColor = "#1B5E20",
                    icon = "ic_local_activity"
                ),
                goalAmount = 300.0,
                goalType = GoalType.MONTHLY,
                goalDeadline = null,
                parentEnvelopID = null,
                currency = currencies[3] // MXN
            )
        )
    }
}
