package com.rpalmar.financialapp.providers.database.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpalmar.financialapp.models.database.EnvelopeEntity
import com.rpalmar.financialapp.models.database.relations.AccountWithCurrencyAndRateRelation
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

    fun getEnvelopeListWithCurrencyWithDelete(): Flow<List<EnvelopeWithCurrencyRelation>> {
        return envelopeDAO.getEnvelopeListWithCurrencyWithDelete()
    }

    suspend fun getAll():List<EnvelopeEntity>{
        return envelopeDAO.getAll()
    }

    fun getPaginated(pageSize:Int = 20):Flow<PagingData<EnvelopeWithCurrencyRelation>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { envelopeDAO.getEnvelopeListPaginated()}
        ).flow
    }

    suspend fun deleteAll(){
        envelopeDAO.deleteAll()
    }

    suspend fun softDelete(ID:Long){
        envelopeDAO.softDelete(ID);
    }

    suspend fun updateBalance(envelopeId:Long, amount:Double){
        envelopeDAO.updateBalance(envelopeId, amount)
    }
}