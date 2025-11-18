package com.rpalmar.financialapp.views.category.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rpalmar.financialapp.models.CategoryType
import com.rpalmar.financialapp.models.database.StyleEntity
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.usecases.category.CreateCategoryUseCase
import com.rpalmar.financialapp.usecases.category.DeleteCategoryUseCase
import com.rpalmar.financialapp.usecases.category.GetCategoriesUseCase
import com.rpalmar.financialapp.usecases.category.GetCategoryByIDUseCase
import com.rpalmar.financialapp.usecases.category.UpdateCategoryUseCase
import com.rpalmar.financialapp.views.ui.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getCategoryByIDUseCase: GetCategoryByIDUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
) : ViewModel() {

    private val _categoryUIState = MutableStateFlow(CategoryUIState())
    val categoryUIState = _categoryUIState.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onCategoryFormEvent(event: CategoryFormEvent) {
        when (event) {
            is CategoryFormEvent.OnCategoryNameChange -> {
                _categoryUIState.value = _categoryUIState.value.copy(
                    categoryName = event.value,
                    errors = _categoryUIState.value.errors - "categoryName"
                )
            }

            is CategoryFormEvent.OnDescriptionChange -> {
                _categoryUIState.value = _categoryUIState.value.copy(
                    description = event.value,
                    errors = _categoryUIState.value.errors - "description"
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

    private fun saveCategory() {
        viewModelScope.launch {
            try {
                _categoryUIState.value = _categoryUIState.value.copy(isSaving = true)

                if (!validateFormFields()) {
                    Log.e("CategoryViewModel", "Formulario Invalido")
                    return@launch
                }

                val category = CategoryDomain(
                    name = _categoryUIState.value.categoryName,
                    type = CategoryType.TRANSACTION,
                    style = StyleEntity(
                        color = _categoryUIState.value.color,
                        icon = _categoryUIState.value.icon
                    )
                )

                createCategoryUseCase(category)
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception) {
                Log.e("CategoryViewModel", e.message.toString())
                _uiEvent.send(UIEvent.ShowError("Error al Crear Categoria"))
            } finally {
                _categoryUIState.value = _categoryUIState.value.copy(isSaving = false)
            }
        }
    }

    private fun updateCategory() {
        viewModelScope.launch {
            try {
                _categoryUIState.value = _categoryUIState.value.copy(isSaving = true)

                if (!validateFormFields()) {
                    Log.e("CategoryViewModel", "Formulario Invalido")
                    return@launch
                }

                val category = CategoryDomain(
                    id = _categoryUIState.value.id!!,
                    name = _categoryUIState.value.categoryName,
                    type = CategoryType.TRANSACTION,
                    style = StyleEntity(
                        color = _categoryUIState.value.color,
                        icon = _categoryUIState.value.icon
                    )
                )

                updateCategoryUseCase(category)
                _uiEvent.send(UIEvent.Success)
            } catch (e: Exception) {
                Log.e("CategoryViewModel", e.message.toString())
                _uiEvent.send(UIEvent.ShowError("Error al Actualizar Categoria"))
            } finally {
                _categoryUIState.value = _categoryUIState.value.copy(isSaving = false)
            }
        }
    }

    private fun validateFormFields(): Boolean {
        var errorCount = 0
        if (_categoryUIState.value.categoryName.isEmpty()) {
            _categoryUIState.value = _categoryUIState.value.copy(
                errors = _categoryUIState.value.errors + ("categoryName" to "Campo Obligatorio")
            )
            errorCount++
        }
        if (_categoryUIState.value.description.isEmpty()) {
            _categoryUIState.value = _categoryUIState.value.copy(
                errors = _categoryUIState.value.errors + ("description" to "Campo Obligatorio")
            )
            errorCount++
        }
        return errorCount == 0
    }

    fun cleanForm() {
        _categoryUIState.value = CategoryUIState()
    }

    fun loadCategoryListData() {
        viewModelScope.launch {
            try {
                _categoryUIState.value = _categoryUIState.value.copy(isLoading = true)
                val categories = getCategoriesUseCase.getPaginated()
                categories.collect { pagingData ->
                    // Not the ideal way to do this, but for now it works to get the list
                }
            } finally {
                _categoryUIState.value = _categoryUIState.value.copy(isLoading = false)
            }
        }
    }

    fun getCategories(): Flow<PagingData<CategoryDomain>> {
        return getCategoriesUseCase.getPaginated().cachedIn(viewModelScope)
    }

    fun setCurrentCategory(categoryID: Long) {
        viewModelScope.launch {
            try {
                _categoryUIState.value = _categoryUIState.value.copy(isLoading = true, currentSelectedCategory = null)
                val currentCategory = getCategoryByIDUseCase(categoryID)
                if (currentCategory == null) Log.e("CategoryViewModel", "Error al obtener la categoria")
                else _categoryUIState.value = _categoryUIState.value.copy(currentSelectedCategory = currentCategory)
            } finally {
                _categoryUIState.value = _categoryUIState.value.copy(isLoading = false)
            }
        }
    }

    fun handleDeleteCategory() {
        viewModelScope.launch {
            deleteCategoryUseCase(_categoryUIState.value.currentSelectedCategory!!.id)
            _categoryUIState.value = _categoryUIState.value.copy(currentSelectedCategory = null)
        }
    }

    fun handleUpdateCategoryForm() {
        viewModelScope.launch {
            val currentCategory = _categoryUIState.value.currentSelectedCategory
            _categoryUIState.value = _categoryUIState.value.copy(
                id = currentCategory!!.id,
                categoryName = currentCategory.name,
                color = currentCategory.style.color,
                icon = currentCategory.style.icon,
                errors = emptyMap(),
                isEditing = true
            )
        }
    }
}