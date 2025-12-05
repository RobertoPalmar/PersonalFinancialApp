package com.rpalmar.financialapp.views.ui.components.summaryCard

import com.rpalmar.financialapp.models.domain.CategoryDomain
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.views.ui.components.DefaultIcon
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.LightGrey
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.Octicons
import compose.icons.octicons.Pencil24
import compose.icons.octicons.PlusCircle24
import compose.icons.octicons.Trash24

@Composable
fun CategoryDataCard(
    category: CategoryDomain,
    onDeleteCategoryClick: (() -> Unit)? = null,
    onEditCategoryClick: (() -> Unit)? = null
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(bottom = 10.dp, start = 15.dp, end = 15.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // 🔵 BANDA HORIZONTAL DE FONDO (tipo tarjeta de crédito)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 0.dp)
                    .background(category.style.uiColor.copy(alpha = 0.10f))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp, horizontal = 18.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // HEADER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        //ACCOUNT DATA
                        Column {
                            Text(
                                text = "Category",
                                color = LightGrey,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = category.name,
                                color = White,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        // CATEGORY ICON
                        DefaultIcon(
                            title = "Category",
                            icon = category.style.uiIcon,
                            color = category.style.uiColor,
                            circleSize = 50.dp,
                            iconSize = 30.dp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // FOOTER
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {

                            //TODO
//                            Text(
//                                text = "Balance per Category",
//                                color = White,
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                            Text(
//                                text = balanceFormatted,
//                                style = MaterialTheme.typography.titleLarge,
//                                color = White,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        //ACTION BUTTONS SECTION
                        Row {

                            if (onDeleteCategoryClick != null) {
                                IconButton(
                                    onClick = onDeleteCategoryClick,
                                    modifier = Modifier.size(35.dp)
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(image = Octicons.Trash24),
                                        contentDescription = "Delete",
                                        tint = White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            if (onEditCategoryClick != null) {
                                IconButton(
                                    onClick = onEditCategoryClick,
                                    modifier = Modifier.size(35.dp)
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(image = Octicons.Pencil24),
                                        contentDescription = "Edit",
                                        tint = White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@Preview(showBackground = false)
@Composable
fun CategoryDataCardPreview() {

    MaterialTheme {
        CategoryDataCard(
            category = MockupProvider.getMockCategories()[0],
            onDeleteCategoryClick = {},
            onEditCategoryClick = {}
        )
    }
}
