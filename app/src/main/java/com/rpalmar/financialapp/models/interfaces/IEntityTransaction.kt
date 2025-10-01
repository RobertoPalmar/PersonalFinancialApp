package com.rpalmar.financialapp.models.interfaces

import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux

interface IEntityTransaction {
    val name: String;
    val description: String;

    fun toAuxDomain(): SimpleTransactionSourceAux
}

interface IDomainTransaction {
    val name: String;
    val description: String;
}