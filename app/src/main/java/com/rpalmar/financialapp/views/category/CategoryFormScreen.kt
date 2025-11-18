package com.rpalmar.financialapp.views.category

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.views.category.data.CategoryFormEvent
import com.rpalmar.financialapp.views.category.data.CategoryUIState
import com.rpalmar.financialapp.views.category.data.CategoryViewModel
import com.rpalmar.financialapp.views.ui.UIEvent
import com.rpalmar.financialapp.views.ui.componentes.BaseTextField
import com.rpalmar.financialapp.views.ui.componentes.ColorPicker
import com.rpalmar.financialapp.views.ui.componentes.FormNavigatorButtonSection
import com.rpalmar.financialapp.views.ui.componentes.IconPicker
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.TitleSectionCard
import com.rpalmar.financialapp.views.ui.theme.Blue

@Composable
fun CategoryFormScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit
) {
    val backStackEntry = remember(navController.currentBackStackEntry) { navController.getBackStackEntry("category_flow") }
    val viewModel: CategoryViewModel = hiltViewModel(backStackEntry)
    val context = LocalContext.current
    val categoryUIState = viewModel.categoryUIState.collectAsState()

    LaunchedEffect(Unit) {
        if (!categoryUIState.value.isEditing)
            viewModel.onCategoryFormEvent(CategoryFormEvent.Reset)
    }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Success -> onBackPressed()
                is UIEvent.ShowError -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    MainLayout {
        Column(
            modifier = Modifier.fillMaxSize(1f)
        ) {
            TitleSectionCard(
                title = if (categoryUIState.value.isEditing) "Edit Category" else "Create Category",
                backgroundColor = Blue
            )
            CategoryFormSection(
                categoryViewModel = viewModel,
                categoryUIState = categoryUIState.value
            )
            Spacer(modifier = Modifier.weight(1f))
            FormNavigatorButtonSection(
                onBackPressed = onBackPressed,
                isEditing = categoryUIState.value.isEditing,
                isSaving = categoryUIState.value.isSaving,
                onConfirm = { viewModel.onCategoryFormEvent(CategoryFormEvent.Submit) }
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CategoryFormSection(
    categoryViewModel: CategoryViewModel,
    categoryUIState: CategoryUIState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(0.dp, 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        BaseTextField(
            value = categoryUIState.categoryName,
            onValueChange = { categoryViewModel.onCategoryFormEvent(CategoryFormEvent.OnCategoryNameChange(it)) },
            label = "Category Name"
        )
        BaseTextField(
            value = categoryUIState.description,
            onValueChange = { categoryViewModel.onCategoryFormEvent(CategoryFormEvent.OnDescriptionChange(it)) },
            label = "Category Description"
        )
        StyleSection(
            categoryUIState = categoryUIState,
            categoryViewModel = categoryViewModel
        )
    }
}

@Composable
fun StyleSection(
    categoryUIState: CategoryUIState,
    categoryViewModel: CategoryViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(0.dp, 10.dp),
    ) {
        Text(
            text = "Style",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        ColorPicker(
            initialColor = categoryUIState.color,
            onColorSelected = { color ->
                categoryViewModel.onCategoryFormEvent(
                    CategoryFormEvent.OnColorChange("#${Integer.toHexString(color.toArgb())}")
                )
            }
        )
        IconPicker(
            initialIcon = categoryUIState.icon,
            onIconSelected = { icon ->
                categoryViewModel.onCategoryFormEvent(CategoryFormEvent.OnIconChange(icon))
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CategoryFormScreenPreview() {
    StyleSection(
        categoryUIState = CategoryUIState(),
        categoryViewModel = hiltViewModel()
    )
}