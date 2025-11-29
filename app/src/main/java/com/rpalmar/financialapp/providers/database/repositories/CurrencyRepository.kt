package com.rpalmar.financialapp.providers.database.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpalmar.financialapp.models.database.CurrencyEntity
import com.rpalmar.financialapp.models.database.relations.AccountWithCurrencyRelation
import com.rpalmar.financialapp.providers.database.DAOs.CurrencyDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val currencyDAO: CurrencyDAO
): BaseEntityRepository<CurrencyEntity, CurrencyDAO>(currencyDAO) {

    fun getAll():Flow<List<CurrencyEntity>>{
        return currencyDAO.getAll()
    }

    fun getPaginated(pageSize:Int = 20):Flow<PagingData<CurrencyEntity>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { currencyDAO.getCurrencyListPaginated()}
        ).flow
    }

    suspend fun deleteAll(){
        currencyDAO.deleteAll()
    }

    fun getMainCurrency(): CurrencyEntity?{
        return currencyDAO.getMainCurrency()
    }
}