package com.rpalmar.financialapp.models.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.views.ui.components.toHex

data class StyleDomain(
    val uiColor: Color,
    val uiIcon: ImageVector
){
    fun toEntity(): StyleEntity {
        return StyleEntity(
            stringColor = uiColor.toHex(),
            icon = uiIcon.name
        )
    }
}