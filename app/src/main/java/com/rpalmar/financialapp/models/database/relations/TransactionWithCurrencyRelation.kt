package com.rpalmar.financialapp.models.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux

data class TransactionWithCurrencyRelation(
    @Embedded
    val transaction: TransactionEntity,
    @Relation(
        parentColumn = "currencyID",
        entityColumn = "id"
    )
    val currency: CurrencyEntity,
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
            amountInBaseCurrency = transaction.amount * currency.exchangeRate,
            transactionType = transaction.transactionType,
            transactionDate = transaction.transactionDate,
            currency = currency.toDomain(),
            exchangeRate = transaction.exchangeRate,
            description = transaction.description,
            linkedTransaction = linkedTransaction
        )
    }
}