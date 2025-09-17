package com.rpalmar.financialapp.models.domain.auxiliar

import com.rpalmar.financialapp.models.TransactionSourceType

data class SimpleTransactionSourceAux (
    val id:Long,
    val name:String,
    val description:String,
    val transactionEntityType: TransactionSourceType
)