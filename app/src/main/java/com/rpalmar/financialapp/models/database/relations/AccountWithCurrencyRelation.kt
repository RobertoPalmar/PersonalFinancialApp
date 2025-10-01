package com.rpalmar.financialapp.models.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.domain.AccountDomain

data class AccountWithCurrencyRelation(
    @Embedded
    val account: AccountEntity,
    @Relation(
        parentColumn = "currencyID",
        entityColumn = "id"
    )
    val currency:CurrencyEntity
){
    fun toDomain(): AccountDomain{
        return AccountDomain(
            id = account.id,
            name = account.name,
            description = account.description,
            balance = account.balance,
            balanceInBaseCurrency = account.balance * currency.exchangeRate,
            style = account.style,
            currency = currency.toDomain()
        )
    }
}