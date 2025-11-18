package com.rpalmar.financialapp.models.domain

import com.rpalmar.financialapp.models.CategoryType
import com.rpalmar.financialapp.models.database.CategoryEntity
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.interfaces.IDomain
import com.rpalmar.financialapp.models.interfaces.IEntity

data class CategoryDomain(
    val id: Long = 0,
    val type: CategoryType,
    val name: String,
    val style: StyleEntity
): IDomain{
    override fun toEntity(): CategoryEntity {
        return CategoryEntity(
            id = id,
            type = type,
            name = name,
            style = style,
            isDelete = false
        )
    }
}
