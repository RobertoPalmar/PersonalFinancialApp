package com.rpalmar.financialapp.models.database.relations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.EnvelopeDomain

data class EnvelopeWithCurrencyRelation(
    @Embedded val envelope: EnvelopeEntity,
    @Embedded(prefix = "currency_") val currency:CurrencyEntity,
    @ColumnInfo(name = "exchangeRate") val exchangeRate: Double
){
    fun toDomain(): EnvelopeDomain{
        return EnvelopeDomain(
            id = envelope.id,
            name = envelope.name,
            description = envelope.description,
            balance = envelope.balance,
            balanceInMainCurrency = envelope.balance / exchangeRate,
            status = envelope.status,
            style = envelope.style,
            goalAmount = envelope.goalAmount,
            goalType = envelope.goalType,
            goalDeadline = envelope.goalDeadline,
            parentEnvelopID = envelope.parentEnvelopID,
            currency = currency.toDomain(exchangeRate),
        )
    }
}