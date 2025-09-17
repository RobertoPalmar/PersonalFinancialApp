package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.providers.database.DAOs.EnvelopeDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EnvelopeRepository @Inject constructor(
    private val envelopeDAO: EnvelopeDAO
):BaseEntityRepository<EnvelopeEntity, EnvelopeDAO>(envelopeDAO){

    suspend fun getAll():List<EnvelopeEntity>{
        return envelopeDAO.getAll()
    }

    suspend fun deleteAll(){
        envelopeDAO.deleteAll()
    }
}