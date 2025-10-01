package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.EnvelopStatus
import com.rpalmar.financialapp.models.GoalType
import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.interfaces.IDomain
import com.rpalmar.financialapp.models.interfaces.IDomainTransaction
import com.rpalmar.financialapp.models.interfaces.IEntityTransaction
import java.util.Date

data class EnvelopeDomain(
    val id: Long = 0,
    override val name: String,
    override val description: String,
    val balance: Double,
    val status: EnvelopStatus,
    val style:StyleEntity?,

    //GOAL DATA
    val goalAmount: Double,
    val goalType:GoalType,
    val goalDeadline: Double? = null,

    val parentEnvelopID: Long? = null,
    val currency:CurrencyDomain,
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
            createAt = Date()
        )
    }
}