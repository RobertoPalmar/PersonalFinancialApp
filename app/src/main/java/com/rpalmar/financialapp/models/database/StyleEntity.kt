package com.rpalmar.financialapp.models.database

import com.rpalmar.financialapp.models.domain.StyleDomain
import com.rpalmar.financialapp.views.ui.components.refactor.IconMapper
import com.rpalmar.financialapp.views.ui.components.refactor.toColor

data class StyleEntity (
    val stringColor:String,
    val icon:String
){
    fun toDomain(): StyleDomain {
        return StyleDomain(
            uiColor = stringColor.toColor(),
            uiIcon = IconMapper.fromName(icon)
        )
    }
}