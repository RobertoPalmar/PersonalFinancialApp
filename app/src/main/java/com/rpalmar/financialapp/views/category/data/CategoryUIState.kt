package com.rpalmar.financialapp.views.category.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.StyleDomain
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ShoppingBagSolid

data class CategoryUIState(
    //FORM INPUT FIELDS
    var id: Long? = null,
    var categoryName: String = "",
    var color: Color = Color(0xFF81C784),
    var icon: ImageVector = LineAwesomeIcons.ShoppingBagSolid,
    var errors: Map<String, String?> = emptyMap(),
    var isEditing: Boolean = false,

    //AUX DATA
    var isLoading: Boolean = false,
    var isSaving: Boolean = false
) {
    fun toTemporalCategory(): CategoryDomain {
        return CategoryDomain(
            id = id ?: 0,
            type = com.rpalmar.financialapp.models.CategoryType.TRANSACTION,
            name = if (categoryName.isEmpty()) "Category Name" else categoryName,
            style = StyleDomain(
                uiColor = color,
                uiIcon = icon
            )
        )
    }
}

sealed class CategoryFormEvent {
    data class OnCategoryNameChange(val value: String) : CategoryFormEvent()
    data class OnColorChange(val value: Color) : CategoryFormEvent()
    data class OnIconChange(val value: ImageVector) : CategoryFormEvent()
    object Submit : CategoryFormEvent()
    object Reset : CategoryFormEvent()
}

