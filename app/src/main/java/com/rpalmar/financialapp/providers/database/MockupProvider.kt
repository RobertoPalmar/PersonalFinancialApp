package com.rpalmar.financialapp.mock

import com.rpalmar.financialapp.models.CategoryType
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.StyleDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.views.ui.components.IconMapper
import com.rpalmar.financialapp.views.ui.theme.*
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
                mainCurrency = true
            ),
            CurrencyDomain(
                id = 2L,
                name = "Euro",
                ISO = "EUR",
                symbol = "€",
                exchangeRate = 0.92,
                mainCurrency = false
            ),
            CurrencyDomain(
                id = 3L,
                name = "Japanese Yen",
                ISO = "JPY",
                symbol = "¥",
                exchangeRate = 145.3,
                mainCurrency = false
            ),
            CurrencyDomain(
                id = 4L,
                name = "Mexican Peso",
                ISO = "MXN",
                symbol = "$",
                exchangeRate = 17.1,
                mainCurrency = false
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
                currency = currencies[1],
                style = StyleDomain(
                    uiColor = Green,                 // 🌱 Verde
                    uiIcon = IconMapper.fromName("AccountBalanceWallet")
                )
            ),
            AccountDomain(
                id = 102L,
                name = "Savings Account",
                description = "Emergency fund and savings",
                balance = 12000.0,
                balanceInMainCurrency = 5161.65,
                currency = currencies[0],
                style = StyleDomain(
                    uiColor = Indigo,               // 🔵 Índigo
                    uiIcon = IconMapper.fromName("ic_savings")
                )
            ),
            AccountDomain(
                id = 103L,
                name = "Travel Fund",
                description = "Money saved for trips",
                balance = 3500.0,
                balanceInMainCurrency = 5161.65,
                currency = currencies[1],
                style = StyleDomain(
                    uiColor = Blue,                 // 🟦 Azul
                    uiIcon = IconMapper.fromName("ic_flight")
                )
            ),
            AccountDomain(
                id = 104L,
                name = "Investments",
                description = "Stock and ETF portfolio",
                balance = 580000.0,
                balanceInMainCurrency = 5161.65,
                currency = currencies[2],
                style = StyleDomain(
                    uiColor = Purple,               // 🟪 Morado
                    uiIcon = IconMapper.fromName("ic_trending_up")
                )
            ),
            AccountDomain(
                id = 105L,
                name = "Business Checking",
                description = "Company operations account",
                balance = 185000.25,
                balanceInMainCurrency = 5161.65,
                currency = currencies[3],
                style = StyleDomain(
                    uiColor = Orange,               // 🟧 Naranja
                    uiIcon = IconMapper.fromName("ic_business")
                )
            )
        )
    }

    fun getMockTransactions(): List<TransactionDomain> {
        val accounts = getMockAccounts()
        val currencies = getMockCurrencies()
        val categories = getMockCategories() // ⬅️ USAMOS LAS CATEGORÍAS MOCK

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

            // ⬅️ Escoger una categoría válida para el tipo de transacción
            val category = categories
                .filter { it.type == CategoryType.TRANSACTION }
                .random()

            transactions.add(
                TransactionDomain(
                    id = i.toLong(),
                    transactionCode = UUID.randomUUID(),
                    source = source,
                    amount = amount,
                    amountInBaseCurrency = amount * currency.exchangeRate,
                    transactionType = type,
                    transactionDate = date,
                    currency = currency,
                    exchangeRate = currency.exchangeRate,
                    description = "Mock ${type.name.lowercase()} transaction $i",
                    category = category
                )
            )
        }

        // -------- Transferencia con linkedTransaction --------
        val sourceAccount = accountSources[0]
        val destAccount = accountSources[1]
        val currencySource = currencies[0]
        val currencyDest = currencies[1]

        val transferAmount = 300.0
        val transferDate = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L)

        val transferCategory = categories.random() // cualquier categoría mock

        val linkedTransaction = TransactionDomain(
            id = 100L,
            transactionCode = UUID.randomUUID(),
            source = destAccount,
            amount = transferAmount * currencyDest.exchangeRate / currencySource.exchangeRate,
            amountInBaseCurrency = transferAmount * currencyDest.exchangeRate,
            transactionType = TransactionType.INCOME,
            transactionDate = transferDate,
            currency = currencyDest,
            exchangeRate = currencyDest.exchangeRate,
            description = "Linked transfer to ${destAccount.name}",
            category = transferCategory
        )

        val mainTransaction = TransactionDomain(
            id = 101L,
            transactionCode = UUID.randomUUID(),
            source = sourceAccount,
            amount = transferAmount,
            amountInBaseCurrency = transferAmount * currencySource.exchangeRate,
            transactionType = TransactionType.EXPENSE,
            transactionDate = transferDate,
            currency = currencySource,
            exchangeRate = currencySource.exchangeRate,
            description = "Transfer to ${destAccount.name}",
            linkedTransaction = linkedTransaction,
            category = transferCategory
        )

        transactions.add(linkedTransaction)
        transactions.add(mainTransaction)

        return transactions
    }

    fun getMockCategories(): List<CategoryDomain> {
        return listOf(
            CategoryDomain(
                id = 1,
                type = CategoryType.TRANSACTION,
                name = "Comida",
                style = StyleDomain(
                    uiColor = Orange,
                    uiIcon = IconMapper.fromName("UtensilsSolid")
                )
            ),
            CategoryDomain(
                id = 2,
                type = CategoryType.TRANSACTION,
                name = "Salario",
                style = StyleDomain(
                    uiColor = Green,
                    uiIcon = IconMapper.fromName("MoneyBillWaveSolid")
                )
            ),
            CategoryDomain(
                id = 3,
                type = CategoryType.TRANSACTION,
                name = "Transporte",
                style = StyleDomain(
                    uiColor = Blue,
                    uiIcon = IconMapper.fromName("CarSolid")
                )
            ),
            CategoryDomain(
                id = 4,
                type = CategoryType.TRANSACTION,
                name = "Compras",
                style = StyleDomain(
                    uiColor = Purple,
                    uiIcon = IconMapper.fromName("ShoppingBagSolid")
                )
            ),
            CategoryDomain(
                id = 5,
                type = CategoryType.TRANSACTION,
                name = "Salud",
                style = StyleDomain(
                    uiColor = Red,
                    uiIcon = IconMapper.fromName("HeartSolid")
                )
            ),
            CategoryDomain(
                id = 6,
                type = CategoryType.TRANSACTION,
                name = "Servicios",
                style = StyleDomain(
                    uiColor = Orange,
                    uiIcon = IconMapper.fromName("BoltSolid")
                )
            ),
            CategoryDomain(
                id = 7,
                type = CategoryType.TRANSACTION,
                name = "Entretenimiento",
                style = StyleDomain(
                    uiColor = Purple,
                    uiIcon = IconMapper.fromName("GiftSolid")
                )
            ),
            CategoryDomain(
                id = 8,
                type = CategoryType.TRANSACTION,
                name = "Educación",
                style = StyleDomain(
                    uiColor = Indigo,
                    uiIcon = IconMapper.fromName("BookSolid")
                )
            ),
            CategoryDomain(
                id = 9,
                type = CategoryType.TRANSACTION,
                name = "Regalos",
                style = StyleDomain(
                    uiColor = Amber,
                    uiIcon = IconMapper.fromName("GiftSolid")
                )
            ),
            CategoryDomain(
                id = 10,
                type = CategoryType.TRANSACTION,
                name = "Ahorro",
                style = StyleDomain(
                    uiColor = LightGreen,
                    uiIcon = IconMapper.fromName("WalletSolid")
                )
            )
        )
    }

}