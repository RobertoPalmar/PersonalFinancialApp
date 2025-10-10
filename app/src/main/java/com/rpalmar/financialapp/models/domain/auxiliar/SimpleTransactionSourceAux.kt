package com.rpalmar.financialapp.models.domain.auxiliar

import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.CurrencyDomain

data class SimpleTransactionSourceAux (
    val id:Long,
    val name:String,
    val description:String,
    val balance: Double,
    val currency: CurrencyDomain,
    val transactionEntityType: TransactionSourceType
)