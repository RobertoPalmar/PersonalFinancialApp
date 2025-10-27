package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.database.AccountEntity
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomain
import com.rpalmar.financialapp.models.interfaces.IDomainTransaction
import java.util.Date

data class AccountDomain(
    override val id: Long = 0,
    override val name: String,
    override val description: String,
    override val balance: Double,
    override val currency: CurrencyDomain,
    val balanceInMainCurrency: Double,
    val style:StyleEntity?,
): IDomain, IDomainTransaction {
    override fun toEntity(): AccountEntity{
        return AccountEntity(
            id = id,
            name = name,
            description = description,
            balance = balance,
            currencyID = currency.id,
            style = style,
            createAt = Date(),
            isDelete = false
        );
    }

    override fun toAuxDomain(): SimpleTransactionSourceAux {
        return SimpleTransactionSourceAux(
            id = id,
            name = name,
            description = description,
            currency = currency,
            transactionEntityType = TransactionSourceType.ACCOUNT,
            balance = balance
        )
    }
}