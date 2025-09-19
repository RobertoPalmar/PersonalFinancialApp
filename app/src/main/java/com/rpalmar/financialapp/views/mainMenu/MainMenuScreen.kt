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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.R
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Orange

@Composable
fun MainMenuScreen(
    onNavigateToTransactions: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToEnvelopes: () -> Unit,
) {
    val overscrollEffect = rememberOverscrollEffect()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(25.dp)
            .overscroll(overscrollEffect),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            SectionCard(
                title = "Envelopes",
                subtitle = "Organiza tu dinero en sobres de presupuesto",
                buttonName = "Manage",
                mainColor = Orange,
                backgroundImage = ImageVector.vectorResource(id = R.drawable.ic_envelope),
                onNavigate = onNavigateToEnvelopes
            )
        }
        item {
            SectionCard(
                title = "Accounts",
                subtitle = "Consulta tus saldos y movimientos por cuenta",
                buttonName = "Manage",
                mainColor = Blue,
                backgroundImage = ImageVector.vectorResource(id = R.drawable.ic_wallet),
                onNavigate = onNavigateToAccounts
            )
        }
        item {
            SectionCard(
                title = "Transactions",
                subtitle = "Revisa el historial de ingresos y gastos",
                buttonName = "View",
                mainColor = Green,
                backgroundImage = ImageVector.vectorResource(id = R.drawable.ic_transactions),
                onNavigate = onNavigateToTransactions
            )
        }
    }
}

@Composable
fun SectionCard(
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
                    Button(
                        onClick = { onNavigate() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = mainColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(1f),
                            color = mainColor,
                            text = buttonName,
                            maxLines = 2,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
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
            onNavigateToTransactions = {}
        )
    }
}

