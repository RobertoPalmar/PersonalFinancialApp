package com.rpalmar.financialapp.models.database.relations

import androidx.room.Embedded
import com.rpalmar.financialapp.models.database.CategoryEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux

data class TransactionWithCurrencyRelation(
    @Embedded val transaction: TransactionEntity,
    @Embedded(prefix = "currency_") val currency:CurrencyEntity,
    @Embedded(prefix = "category_") val category: CategoryEntity,
) {
    fun toDomain(
        auxSource: SimpleTransactionSourceAux,
        linkedTransaction: TransactionDomain? = null
    ): TransactionDomain{
        return TransactionDomain(
            id = transaction.id,
            transactionCode = transaction.transactionCode,
            source =  auxSource,
            amount = transaction.amount,
            amountInBaseCurrency = transaction.amount / currency.currentExchangeRate,
            transactionType = transaction.transactionType,
            transactionDate = transaction.transactionDate,
            currency = currency.toDomain(),
            exchangeRate = transaction.transactionExchangeRate,
            description = transaction.description,
            linkedTransaction = linkedTransaction,
            category = category.toDomain()
        )
    }
}