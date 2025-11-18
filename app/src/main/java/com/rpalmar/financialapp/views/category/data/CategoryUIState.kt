package com.rpalmar.financialapp.views.category.data

import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain

data class CategoryUIState(
    //FORM INPUT FIELDS
    var id: Long? = null,
    var categoryName: String = "",
    var description: String = "",
    var color: String = "",
    var icon: String = "",
    var errors:Map<String,String?> = emptyMap(),
    var isEditing: Boolean = false,

    //AUX DATA
    var mainCurrency: CurrencyDomain? = null,
    var categoryList: List<CategoryDomain> = emptyList(),
    var isLoading: Boolean = true,
    var isSaving: Boolean = false,

    var currentSelectedCategory: CategoryDomain? = null
)

sealed class CategoryFormEvent{
    data class OnCategoryNameChange(val value:String): CategoryFormEvent()
    data class OnDescriptionChange(val value:String): CategoryFormEvent()
    data class OnColorChange(val value:String): CategoryFormEvent()
    data class OnIconChange(val value:String): CategoryFormEvent()
    object Submit: CategoryFormEvent()
    object Reset: CategoryFormEvent()
}