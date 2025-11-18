package com.rpalmar.financialapp.providers.database.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpalmar.financialapp.models.database.CategoryEntity
import com.rpalmar.financialapp.providers.database.DAOs.CategoryDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDAO: CategoryDAO
): BaseEntityRepository<CategoryEntity, CategoryDAO>(categoryDAO) {
    fun getByID(id: Long): CategoryEntity? {
        return categoryDAO.getByID(id)
    }

    fun getAll():List<CategoryEntity>{
        return categoryDAO.getAll()
    }

    fun getPaginated(pageSize:Int = 20):Flow<PagingData<CategoryEntity>>{
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { categoryDAO.getCategoryListPaginated()}
        ).flow
    }

    suspend fun softDelete(ID:Long){
        categoryDAO.softDelete(ID);
    }
}
