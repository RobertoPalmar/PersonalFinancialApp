package com.rpalmar.financialapp.models.interfaces

import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux

interface IEntityTransaction {
    val name: String;
    val balance: Double;
    val description: String;

//    fun toAuxDomain(): SimpleTransactionSourceAux
}

interface IDomainTransaction {
    val id: Long;
    val name: String;
    val description: String;
    val balance: Double;
    val currency:CurrencyDomain;

    fun toAuxDomain(): SimpleTransactionSourceAux
}