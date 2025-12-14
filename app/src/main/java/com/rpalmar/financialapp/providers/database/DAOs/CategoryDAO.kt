package com.rpalmar.financialapp.providers.database.DAOs

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rpalmar.financialapp.models.database.CategoryEntity

@Dao
interface CategoryDAO: BaseDao<CategoryEntity> {
    @Query("SELECT * FROM category_table WHERE id = :id")
    fun getByID(id: Long): CategoryEntity?

    @Query("SELECT * FROM category_table WHERE isDelete = 0")
    fun getAll():List<CategoryEntity>

    @Query("SELECT * FROM category_table WHERE isDelete = 0")
    fun getCategoryListPaginated(): PagingSource<Int, CategoryEntity>

    @Query("UPDATE category_table SET isDelete = 1 WHERE id = :id")
    suspend fun softDelete(id:Long)
}
