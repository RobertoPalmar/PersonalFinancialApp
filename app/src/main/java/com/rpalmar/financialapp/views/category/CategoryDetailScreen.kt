package com.rpalmar.financialapp.views.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.views.category.data.CategoryViewModel
import com.rpalmar.financialapp.views.ui.componentes.ConfirmDialog
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.theme.Blue

@Composable
fun CategoryDetailScreen(
    navController: NavHostController,
    categoryId: Long
) {
    val backStackEntry = remember(navController.currentBackStackEntry) { navController.getBackStackEntry("category_flow") }
    val viewModel: CategoryViewModel = hiltViewModel(backStackEntry)
    val categoryState = viewModel.categoryUIState.collectAsState()

    LaunchedEffect(key1 = categoryId) {
        viewModel.setCurrentCategory(categoryId)
    }

    fun handleDeleteCategory() {
        viewModel.handleDeleteCategory()
        navController.navigate("categories")
    }

    fun handleUpdateCategory() {
        viewModel.handleUpdateCategoryForm()
        navController.navigate("categoryForm")
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    MainLayout {
        if (categoryState.value.currentSelectedCategory == null) {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            MainCategoryCard(
                category = categoryState.value.currentSelectedCategory!!,
                handleShowConfirmDeleteDialog = { showDeleteDialog = !showDeleteDialog },
                handleEditCategory = { handleUpdateCategory() }
            )
        }

        ConfirmDialog(
            show = showDeleteDialog,
            onConfirm = { handleDeleteCategory() },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun MainCategoryCard(
    category: CategoryDomain,
    handleShowConfirmDeleteDialog: () -> Unit = {},
    handleEditCategory: () -> Unit = {}
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(Blue),
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        )
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize(1f)
                    .padding(15.dp)
            ) {
                Row {
                    Column(
                        modifier = Modifier.fillMaxWidth(1f)
                    ) {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(1f),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                        IconButton(onClick = { handleShowConfirmDeleteDialog() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Category"
                            )
                        }
                        IconButton(onClick = { handleEditCategory() }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Update Category"
                            )
                        }
                    }
                }
            }
        }
    }
}