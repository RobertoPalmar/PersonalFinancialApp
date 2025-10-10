package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.providers.database.DAOs.EnvelopeDAO
import com.rpalmar.financialapp.models.database.relations.EnvelopeWithCurrencyRelation
import javax.inject.Inject
import javax.inject.Singleton

import kotlinx.coroutines.flow.Flow

@Singleton
class EnvelopeRepository @Inject constructor(
    private val envelopeDAO: EnvelopeDAO
):BaseEntityRepository<EnvelopeEntity, EnvelopeDAO>(envelopeDAO){

    fun getByID(id: Long): EnvelopeEntity? {
        return envelopeDAO.getByID(id)
    }

    fun getEnvelopeWithCurrencyByID(id:Long): EnvelopeWithCurrencyRelation?{
        return envelopeDAO.getEnvelopeWithCurrencyByID(id);
    }

    fun getEnvelopeListWithCurrency(): Flow<List<EnvelopeWithCurrencyRelation>> {
        return envelopeDAO.getEnvelopeListWithCurrency()
    }

    suspend fun getAll():List<EnvelopeEntity>{
        return envelopeDAO.getAll()
    }

    suspend fun deleteAll(){
        envelopeDAO.deleteAll()
    }

    suspend fun updateBalance(envelopeId:Long, amount:Double){
        envelopeDAO.updateBalance(envelopeId, amount)
    }
}