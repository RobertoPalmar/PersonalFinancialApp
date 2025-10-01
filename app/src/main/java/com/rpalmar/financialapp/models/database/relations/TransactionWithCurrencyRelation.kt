package com.rpalmar.financialapp.models.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.ITransaction

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
        originAuxSource: SimpleTransactionSourceAux? = null,
        destinationAuxSource: SimpleTransactionSourceAux? = null
    ): TransactionDomain{
        return TransactionDomain(
            id = transaction.id,
            transactionCode = transaction.transactionCode,
            originSource =  originAuxSource,
            destinationSource = destinationAuxSource,
            transactionDate = transaction.transactionDate,
            amount = transaction.amount,
            amountInBaseCurrency = transaction.amount * currency.exchangeRate,
            description = transaction.description,
            currency = currency.toDomain(),
        )
    }
}