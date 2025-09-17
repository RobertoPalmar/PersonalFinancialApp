package com.rpalmar.financialapp.providers.database.repositories

import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.providers.database.DAOs.BaseDao
import javax.inject.Singleton

@Singleton
abstract class BaseEntityRepository<T : IEntity, D : BaseDao<T>>(
    private val dao: D
) {

    suspend fun insert(item: T): Long {
        return dao.insert(item)
    }

    suspend fun insertRange(items: List<T>): List<Long> {
        return dao.insertRange(items)
    }

    suspend fun update(item: T) {
        dao.update(item)
    }

    suspend fun updateRange(itemList: List<T>) {
        dao.updateRange(itemList)
    }

    suspend fun remove(item: T) {
        dao.delete(item)
    }
}
