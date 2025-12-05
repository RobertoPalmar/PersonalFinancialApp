package com.rpalmar.financialapp.views.category.data

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpalmar.financialapp.models.CategoryType
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.StyleDomain
import com.rpalmar.financialapp.usecases.category.CreateCategoryUseCase
import com.rpalmar.financialapp.usecases.category.GetCategoryByIDUseCase
import com.rpalmar.financialapp.usecases.category.UpdateCategoryUseCase
import com.rpalmar.financialapp.views.ui.UIEvent
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ShoppingBagSolid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getCategoryByIDUseCase: GetCategoryByIDUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModel() {

    val LOG_TAG = "CategoryViewModel"

    //UI STATE
    private val _categoryUIState = MutableStateFlow(CategoryUIState())
    val categoryUIState = _categoryUIState.asStateFlow()

    //UI EVENT LISTENER
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Handle events from the category form
     */
    fun onCategoryFormEvent(event: CategoryFormEvent) {
        when (event) {
            is CategoryFormEvent.OnCategoryNameChange -> {
                _categoryUIState.value = _categoryUIState.value.copy(
                    categoryName = event.value,
                    errors = _categoryUIState.value.errors - "categoryName"
                )
            }

            is CategoryFormEvent.OnColorChange -> {
                _categoryUIState.value = _categoryUIState.value.copy(
                    color = event.value,
                    errors = _categoryUIState.value.errors - "color"
                )
            }

            is CategoryFormEvent.OnIconChange -> {
                _categoryUIState.value = _categoryUIState.value.copy(
                    icon = event.value,
                    errors = _categoryUIState.value.errors - "icon"
                )
            }

            is CategoryFormEvent.Submit -> {
                if (_categoryUIState.value.isEditing) updateCategory()
                else saveCategory()
            }

            is CategoryFormEvent.Reset -> {
                cleanForm()
            }
        }
    }

    /**
     * Save the category to the database
     */
    fun saveCategory() {
        viewModelScope.launch {
            try {
                //START LOADING
                _categoryUIState.value = _categoryUIState.value.copy(
                    isSaving = true
                )

                //VALIDATE FIELDS
                var validForm = validateFormFields()
                if (!validForm) {
                    Log.e(LOG_TAG, "Formulario Invalido")
                    return@launch
                }

                //MAP TO DOMAIN ENTITY
                val category = CategoryDomain(
                    type = CategoryType.TRANSACTION,
                    name = _categoryUIState.value.categoryName,
                    style = StyleDomain(
                        uiColor = _categoryUIState.value.color,
                        uiIcon = _categoryUIState.value.icon
                    )
                )

                //CREATE CATEGORY
                createCategoryUseCase(category)

                //NOTIFY SUCCESS CREATION
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message.toString())

                //NOTIFY ERROR CREATION
                _uiEvent.send(UIEvent.ShowError("Error al Crear Categoría"))
            } finally {
                //FINISH LOADING
                _categoryUIState.value = _categoryUIState.value.copy(isSaving = false)
            }
        }
    }

    fun updateCategory() {
        viewModelScope.launch {
            try {
                //START LOADING
                _categoryUIState.value = _categoryUIState.value.copy(
                    isSaving = true
                )

                //VALIDATE FIELDS
                var validForm = validateFormFields()
                if (!validForm) {
                    Log.e(LOG_TAG, "Formulario Invalido")
                    return@launch
                }

                //MAP TO DOMAIN ENTITY
                val category = CategoryDomain(
                    id = _categoryUIState.value.id!!,
                    type = CategoryType.TRANSACTION,
                    name = _categoryUIState.value.categoryName,
                    style = StyleDomain(
                        uiColor = _categoryUIState.value.color,
                        uiIcon = _categoryUIState.value.icon
                    )
                )

                //UPDATE CATEGORY
                updateCategoryUseCase(category)

                //NOTIFY SUCCESS UPDATE
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.message.toString())

                //NOTIFY ERROR UPDATE
                _uiEvent.send(UIEvent.ShowError("Error al Actualizar Categoría"))
            } finally {
                //FINISH LOADING
                _categoryUIState.value = _categoryUIState.value.copy(
                    isSaving = false,
                )
            }
        }
    }

    /**
     * Return a boolean indicating if the form fields are valid
     */
    fun validateFormFields(): Boolean {
        var errorCount = 0
        if (_categoryUIState.value.categoryName.isEmpty()) {
            _categoryUIState.value = _categoryUIState.value.copy(
                errors = _categoryUIState.value.errors + ("categoryName" to "Campo Obligatorio")
            )
            errorCount++
        }

        return errorCount == 0
    }

    /**
     * Clean form data to new entries
     */
    fun cleanForm() {
        _categoryUIState.value = _categoryUIState.value.copy(
            id = null,
            categoryName = "",
            color = Color(0xFF81C784),
            icon = LineAwesomeIcons.ShoppingBagSolid,
            errors = emptyMap(),
            isEditing = false
        )
    }

    /**
     * Set the current category fields to update
     */
    fun handleUpdateCategoryForm(category: CategoryDomain) {
        viewModelScope.launch {
            _categoryUIState.value = _categoryUIState.value.copy(
                id = category.id,
                categoryName = category.name,
                color = category.style.uiColor,
                icon = category.style.uiIcon,
                errors = emptyMap(),
                isEditing = true
            )
        }
    }
}

