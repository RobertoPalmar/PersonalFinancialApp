package com.rpalmar.financialapp.views.category

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.views.ui.componentes.SimpleButton
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.theme.Blue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.views.ui.theme.White
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rpalmar.financialapp.views.category.data.CategoryViewModel
import com.rpalmar.financialapp.views.ui.componentes.SummarySection
import com.rpalmar.financialapp.views.ui.theme.Grey

@Composable
fun CategoryListScreen(
    navController: NavHostController,
    onNavigateToForm: () -> Unit,
    onNavigateToCategoryDetail: (Long) -> Unit,
) {
    val backStackEntry = remember(navController.currentBackStackEntry) { navController.getBackStackEntry("category_flow") }
    val viewModel: CategoryViewModel = hiltViewModel(backStackEntry)
    val categoryState = viewModel.categoryUIState.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadCategoryListData()
    }

    MainLayout {
        SummarySection(
            sectionName = "Categories",
            totalEntities = categoryState.value.categoryList.size,
            mainSummaryData = "",
            mainColor = Blue,
            icon = ImageVector.vectorResource(id = R.drawable.ic_category)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBarSection()
        Spacer(modifier = Modifier.height(3.dp))
        ButtonsSection(onNavigateToForm = onNavigateToForm)

        if (categoryState.value.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(.7f))
                CircularProgressIndicator(
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            CategoryListSection(
                viewModel = viewModel,
                onNavigateToCategoryDetails = { onNavigateToCategoryDetail(it) }
            )
        }
    }
}

@Composable
fun SearchBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(48.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search Category") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f),
            shape = RoundedCornerShape(10.dp),
        )
    }
}

@Composable
fun ButtonsSection(
    onNavigateToForm: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        SimpleButton(
            onClick = { onNavigateToForm() },
            icon = Icons.Default.AddBox,
            text = "New",
            color = Blue
        )
    }
}

@Composable
fun CategoryListSection(
    viewModel: CategoryViewModel,
    onNavigateToCategoryDetails: (Long) -> Unit
) {
    val categories = viewModel.getCategories().collectAsLazyPagingItems()
    val overscrollEffect = rememberOverscrollEffect()

    Text(
        text = "Category List",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(0.dp, 10.dp)
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(1f)
            .overscroll(overscrollEffect),
        contentPadding = PaddingValues(
            top = 10.dp,
            bottom = 10.dp
        ),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        when {
            categories.loadState.refresh is LoadState.Loading -> {
                item {
                    Column {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                }
            }
            categories.itemCount == 0 -> {
                item {
                    Text(
                        text = "Aún no hay categorias",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = Grey
                    )
                }
            }
            else -> {
                items(categories.itemCount) { index ->
                    val category = categories[index]
                    category?.let {
                        CategoryItemCard(
                            category = category,
                            onClick = {
                                onNavigateToCategoryDetails(category.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale", "UseKtx", "LocalContextResourcesRead")
@Composable
fun CategoryItemCard(
    category: CategoryDomain,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val iconResourceID = remember(category.style.icon) {
        category.style.icon.let {
            context.resources.getIdentifier(it, "drawable", context.packageName)
        }
    }

    val styleColor = remember(category.style.color) {
        category.style.color.let { Color(android.graphics.Color.parseColor(it)) }
    }

    val interactionSource = remember { MutableInteractionSource() }

    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .height(82.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {}
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25f),
                horizontalAlignment = Alignment.Start
            ) {
                if (iconResourceID != 0) {
                    CircleIcon(
                        painter = painterResource(id = iconResourceID),
                        iconColor = styleColor,
                        contentDescription = category.name,
                        size = 48.dp
                    )
                } else {
                    CircleIcon(
                        painter = painterResource(id = R.drawable.ic_category),
                        iconColor = styleColor,
                        contentDescription = category.name,
                        size = 48.dp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CircleIcon(
    painter: Painter,
    iconColor: Color,
    contentDescription: String?,
    size: Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(iconColor.copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(size - 8.dp),
            imageVector = Icons.Default.Category,
            contentDescription = contentDescription,
            tint = iconColor
        )
    }
}