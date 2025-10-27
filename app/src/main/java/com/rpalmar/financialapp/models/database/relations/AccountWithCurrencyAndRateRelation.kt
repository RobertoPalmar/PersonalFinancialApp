package com.rpalmar.financialapp.models.database.relations

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.domain.AccountDomain

data class AccountWithCurrencyAndRateRelation(
    @Embedded val account: AccountEntity,
    @Embedded(prefix = "currency_") val currency:CurrencyEntity,
    @ColumnInfo(name = "exchangeRate") val exchangeRate: Double
){
    fun toDomain(): AccountDomain{
        return AccountDomain(
            id = account.id,
            name = account.name,
            description = account.description,
            balance = account.balance,
            balanceInMainCurrency = account.balance / exchangeRate,
            style = account.style,
            currency = currency.toDomain(exchangeRate)
        )
    }
}