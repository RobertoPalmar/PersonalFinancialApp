package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.EnvelopStatus
import com.rpalmar.financialapp.models.GoalType
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomain
import com.rpalmar.financialapp.models.interfaces.IDomainTransaction
import java.util.Date

data class EnvelopeDomain(
    override val id: Long = 0,
    override val name: String,
    override val description: String,
    override val balance: Double,
    override val currency:CurrencyDomain,
    val balanceInMainCurrency:Double,

    //GOAL DATA
    val goalAmount: Double,
    val goalType:GoalType,
    val goalDeadline: Double? = null,
    val status: EnvelopStatus,

    val parentEnvelopID: Long? = null,
    val style:StyleEntity?,
): IDomain, IDomainTransaction {
    override fun toEntity(): EnvelopeEntity{
        return EnvelopeEntity(
            id = id,
            name = name,
            description = description,
            balance = balance,
            status = status,
            style = style,
            goalAmount = goalAmount,
            goalType = goalType,
            goalDeadline = goalDeadline,
            parentEnvelopID = parentEnvelopID,
            currencyID = currency.id,
            createAt = Date(),
            isDelete = false
        )
    }

    override fun toAuxDomain(): SimpleTransactionSourceAux {
        return SimpleTransactionSourceAux(
            id = id,
            name = name,
            description = description,
            currency = currency,
            transactionEntityType = TransactionSourceType.ENVELOPE,
            balance = balance
        )
    }
}