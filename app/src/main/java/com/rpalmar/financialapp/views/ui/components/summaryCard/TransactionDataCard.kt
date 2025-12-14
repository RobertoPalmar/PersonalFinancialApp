package com.rpalmar.financialapp.views.ui.components.summaryCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.views.navigation.LocalAppViewModel
import com.rpalmar.financialapp.views.transaction.data.TransactionViewModel
import com.rpalmar.financialapp.views.ui.components.DefaultIcon
import com.rpalmar.financialapp.views.ui.components.ModalDialog
import com.rpalmar.financialapp.views.ui.components.PreferenceStarIcon
import com.rpalmar.financialapp.views.ui.components.formatAmount
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.LightGrey
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.Octicons
import compose.icons.octicons.Pencil24
import compose.icons.octicons.Trash24

@Composable
fun TransactionDataCard(
    transaction: TransactionDomain,
    transactionViewModel: TransactionViewModel
) {
    val mainCurrency by LocalAppViewModel.current.mainCurrency.collectAsState()

    fun onDeleteTransactionClick(){
        //TODO
    }

    fun onEditTransactionClick(){
        //TODO
    }

    //DELETE DIALOG
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        ModalDialog(
            title = "Delete Transaction",
            message = "Are you sure you want to delete this transaction?",
            onAccept = { onDeleteTransactionClick() },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(vertical = 10.dp, horizontal = 15.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGrey),
            shape = RoundedCornerShape(14.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 0.dp)
                        .background(transaction.category.style.uiColor.copy(alpha = 0.10f))
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
                            //TRANSACTION DATA
                            Column {
                                Text(
                                    text = "Transaction",
                                    color = LightGrey,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = transaction.transactionType.name,
                                    color = LightGrey,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Column {
                                    Text(
                                        text = formatAmount(transaction.amount, transaction.currency.symbol),
                                        color = White,
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    if (!transaction.currency.mainCurrency) {
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = formatAmount(transaction.amountInBaseCurrency, mainCurrency!!.symbol),
                                            color = White,
                                            style = MaterialTheme.typography.titleSmall
                                        )
                                    }
                                }
                            }

                            //TRANSACTION ICON
                            DefaultIcon(
                                title = transaction.description,
                                icon = transaction.category.style.uiIcon,
                                color = transaction.category.style.uiColor,
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


                                when (transaction.transactionType) {
                                    TransactionType.INCOME,
                                    TransactionType.EXPENSE,
                                    TransactionType.ADJUSTMENT -> {
                                        Text(
                                            text = "Account",
                                            color = White,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = transaction.source.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = White,
                                            maxLines = 1,
                                        )
                                    }

                                    TransactionType.TRANSFER -> {
                                        Text(
                                            text = "Origin Account",
                                            color = White,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = transaction.source.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = White,
                                            maxLines = 1,
                                        )

                                        Text(
                                            text = "Destination Account",
                                            color = White,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = transaction.linkedTransaction!!.source.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = White,
                                            maxLines = 1,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            //ACTION BUTTONS SECTION
                            Row {

                                IconButton(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier.size(35.dp)
                                ) {
                                    Icon(
                                        painter = rememberVectorPainter(image = Octicons.Trash24),
                                        contentDescription = "Delete",
                                        tint = White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {onEditTransactionClick()},
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
fun TransactionDataCardPreview() {

    MaterialTheme {
        TransactionDataCard(
            transaction = MockupProvider.getMockTransactions()[0],
            transactionViewModel = hiltViewModel()
        )
    }
}
