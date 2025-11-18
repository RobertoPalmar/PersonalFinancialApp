package com.rpalmar.financialapp.views.currency

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.models.ButtonType
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.views.currency.data.CurrencyViewModel
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.componentes.SimpleButton
import com.rpalmar.financialapp.views.ui.componentes.SummarySection
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Grey
import com.rpalmar.financialapp.views.ui.theme.Orange
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import com.rpalmar.financialapp.views.ui.theme.Yellow

@Composable
fun CurrencyListScreen(
    navController: NavHostController,
    onNavigateToForm: (Long) -> Unit
) {
    val viewModel: CurrencyViewModel = hiltViewModel()
    val currencyState = viewModel.currencyUiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.loadCurrencies()
    }

    MainLayout {
        SummarySection(
            sectionName = "Currencies",
            totalEntities = currencyState.value.currencyList.size,
            mainSummaryData = "Manage your currencies",
            mainColor = Green,
            icon = ImageVector.vectorResource(id = R.drawable.ic_currency)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SearchBarSection()
        Spacer(modifier = Modifier.height(3.dp))
        ButtonsSection(onNavigateToForm = { /* TODO */ }, enabled = false)

        if (currencyState.value.isLoading) {
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
        } else if (currencyState.value.currencyList.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(.7f))
                Text(
                    text = "No Data",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            CurrencyListSection(
                currencies = currencyState.value.currencyList,
                onNavigateToCurrencyDetails = onNavigateToForm
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
            placeholder = { Text("Search Currency") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
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
    onNavigateToForm: () -> Unit,
    enabled: Boolean = true
) {
//    Row(
//        modifier = Modifier.fillMaxWidth(1f)
//    ) {
//        Spacer(modifier = Modifier.weight(1f))
//        SimpleButton(
//            onClick = { onNavigateToForm() },
//            icon = Icons.Default.AddBox,
//            text = "New",
//            color = Green,
//            type = ButtonType.PRIMARY,
//        )
//    }
}

@Composable
fun CurrencyListSection(
    currencies: List<CurrencyDomain>,
    onNavigateToCurrencyDetails: (Long) -> Unit
) {
    val overscrollEffect = rememberOverscrollEffect()

    Text(
        text = "Currency List",
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
        if (currencies.isEmpty()) {
            item {
                Text(
                    text = "No currencies available",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Green
                )
            }
        } else {
            items(currencies) { currency ->
                CurrencyItemCard(
                    currency = currency,
                    onClick = { onNavigateToCurrencyDetails(currency.id) }
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CurrencyItemCard(
    currency: CurrencyDomain,
    onClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier
            .height(82.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                CircleSymbol(
                    symbol = currency.symbol,
                    color = Green,
                    size = 48.dp
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.6f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currency.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = currency.ISO,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DarkGrey
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "ER: ${String.format("%.2f", currency.exchangeRate)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                if(currency.mainCurrency) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "mainCurrency",
                        tint = Yellow,
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 5.dp, 0.dp)
                            .size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CircleSymbol(
    symbol: String,
    color: androidx.compose.ui.graphics.Color,
    size: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(color.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = color,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
