package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.interfaces.IDomain
import com.rpalmar.financialapp.models.interfaces.ITransaction
import java.util.Date

data class AccountDomain(
    val id: Long = 0,
    override val name: String,
    override val description: String,
    val initBalance: Double,
    val currency: CurrencyDomain,
    val initBalanceInBaseCurrency: Double,
    val style:StyleEntity?,
): IDomain, ITransaction {
    override fun toEntity(): AccountEntity{
        return AccountEntity(
            id = id,
            name = name,
            description = description,
            balance = initBalance,
            currencyID = currency.id,
            style = style,
            createAt = Date()
        );
    }
}