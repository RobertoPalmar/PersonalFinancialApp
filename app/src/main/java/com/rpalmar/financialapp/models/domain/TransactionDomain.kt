package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomain
import java.util.Date
import java.util.UUID

data class TransactionDomain (
    val id:Long = 0,
    val transactionCode: UUID,
    val originSource: SimpleTransactionSourceAux? = null,
    val destinationSource:SimpleTransactionSourceAux? = null,
    val transactionDate:Date,
    val amount: Double,
    val currency: CurrencyDomain,
    val amountInBaseCurrency: Double,
    val description:String
): IDomain {
    override fun toEntity(): TransactionEntity {
        return TransactionEntity(
            id = id,
            transactionCode = transactionCode,
            originSourceID = originSource?.id,
            originSourceType = originSource?.transactionEntityType,
            destinationSourceID = destinationSource?.id,
            destinationSourceType = destinationSource?.transactionEntityType,
            transactionDate = transactionDate,
            amount = amount,
            description = description,
            currencyID = currency.id
        )
    }
}