package com.rpalmar.financialapp.models.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpalmar.financialapp.models.CategoryType
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.interfaces.IEntity
import com.rpalmar.financialapp.models.interfaces.ISoftDelete

@Entity(tableName = "category_table")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo("type") val type: CategoryType,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("style") val style: StyleEntity,
    @ColumnInfo("isDelete") override val isDelete: Boolean
): IEntity, ISoftDelete{
    fun toDomain(): CategoryDomain{
        return CategoryDomain(
            id = id,
            type = type,
            name = name,
            style = style
        )
    }
}