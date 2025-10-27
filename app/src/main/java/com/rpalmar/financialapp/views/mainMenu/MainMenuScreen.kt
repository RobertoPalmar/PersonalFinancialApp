package com.rpalmar.financialapp.views.mainMenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.models.ButtonType
import com.rpalmar.financialapp.models.ui.NavigationItem
import com.rpalmar.financialapp.views.ui.componentes.SimpleButton
import com.rpalmar.financialapp.views.ui.componentes.MainLayout
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Orange
import androidx.compose.foundation.lazy.items
import com.rpalmar.financialapp.views.ui.theme.Green

@Composable
fun MainMenuScreen(
    onNavigateToCurrencies: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToEnvelopes: () -> Unit,
) {

    MainLayout {
        NavigationSection(
            onNavigateToCurrencies = onNavigateToCurrencies,
            onNavigateToAccounts = onNavigateToAccounts,
            onNavigateToEnvelopes = onNavigateToEnvelopes
        )
    }

}

@Composable
fun NavigationSection(
    onNavigateToCurrencies: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToEnvelopes: () -> Unit,
){
    val overscrollEffect = rememberOverscrollEffect()

    val navigationItems = listOf(
        NavigationItem(
            title = "Envelopes",
            subtitle = "Organiza tu dinero en sobres de presupuesto",
            buttonName = "Manage",
            mainColor = Orange,
            backgroundImage = ImageVector.vectorResource(id = R.drawable.ic_envelope),
            onNavigate = onNavigateToEnvelopes
        ),
        NavigationItem(
            title = "Accounts",
            subtitle = "Consulta tus saldos y movimientos por cuenta",
            buttonName = "Manage",
            mainColor = Blue,
            backgroundImage = ImageVector.vectorResource(id = R.drawable.ic_account),
            onNavigate = onNavigateToAccounts
        ),
        NavigationItem(
            title = "Currencies",
            subtitle = "Administra y consulta las diferentes monedas que utilizas",
            buttonName = "Manage",
            mainColor = Green,
            backgroundImage = ImageVector.vectorResource(id = R.drawable.ic_currency),
            onNavigate = onNavigateToCurrencies
        )
        // NavigationItem(
        //     title = "Transactions",
        //     subtitle = "Revisa el historial de ingresos y gastos",
        //     buttonName = "View",
        //     mainColor = Green,
        //     backgroundImage = ImageVector.vectorResource(id = R.drawable.ic_transactions),
        //     onNavigate = onNavigateToTransactions
        // )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(1f)
            .overscroll(overscrollEffect),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(items = navigationItems) { item ->
            NavigationCard(
                title = item.title,
                subtitle = item.subtitle,
                buttonName = item.buttonName,
                mainColor = item.mainColor,
                backgroundImage = item.backgroundImage,
                onNavigate = item.onNavigate
            )
        }
    }
}

@Composable
fun NavigationCard(
    title: String,
    subtitle: String,
    buttonName: String,
    mainColor: Color,
    backgroundImage: ImageVector,
    onNavigate:() -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = mainColor),
        modifier = Modifier
            .height(170.dp)
            .fillMaxWidth(1f),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .clipToBounds()
        ) {
            Icon(
                imageVector = backgroundImage,
                contentDescription = title,
                tint = Color.Black.copy(alpha = 0.1f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(180.dp)
                    .offset(x = 40.dp, y = 10.dp)
            )

            Row() {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .fillMaxHeight(1f)
                        .padding(15.dp)
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    SimpleButton(
                        onClick = { onNavigate() },
                        text = buttonName,
                        color = mainColor,
                        type = ButtonType.OUTLINE
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ExamplePreview() {
    FinancialTheme(
        darkTheme = false
    ) {
        MainMenuScreen(
            onNavigateToAccounts = {},
            onNavigateToEnvelopes = {},
            onNavigateToCurrencies = {}
        )
    }
}

