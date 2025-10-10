package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomain
import java.util.Date
import java.util.UUID

data class TransactionDomain (
    val id:Long = 0,
    val transactionCode: UUID,
    val source: SimpleTransactionSourceAux,
    val amount: Double = 0.0,
    val amountInBaseCurrency: Double = 0.0,
    val transactionType: TransactionType,
    val transactionDate:Date,
    val currency: CurrencyDomain,
    val exchangeRate: Double,
    val description:String,
    val linkedTransaction: TransactionDomain? = null
): IDomain {
    override fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id,
            transactionCode = transactionCode,
            sourceID = source.id,
            sourceType = source.transactionEntityType,
            amount = amount,
            transactionType = transactionType,
            transactionDate = transactionDate,
            currencyID = currency.id,
            exchangeRate = exchangeRate,
            description = description,
            linkedTransactionID = linkedTransaction?.id,
            isDelete = false
        )
    }
}