package com.rpalmar.financialapp.views.ui.components.refactor

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.views.account.SummaryAmountCard
import com.rpalmar.financialapp.views.ui.theme.Green
import com.rpalmar.financialapp.views.ui.theme.Red
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.ArrowDownSolid
import compose.icons.lineawesomeicons.ArrowUpSolid

@Composable
fun IncomeExpenseSection(
    income: Double,
    expenses: Double,
    firstCurrency: CurrencyDomain,
    altCurrency: CurrencyDomain
) {
    val formatIncome = formatAmount(income, firstCurrency.symbol)
    val formatIncomeAlt = formatAmount(
        income * firstCurrency.exchangeRate / altCurrency.exchangeRate,
        altCurrency.symbol
    )
    val formatExpense = formatAmount(expenses, firstCurrency.symbol)
    val formatExpenseAlt = formatAmount(
        income * firstCurrency.exchangeRate / altCurrency.exchangeRate,
        altCurrency.symbol
    )

    Row(
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {

        SummaryAmountCard(
            modifier = Modifier.weight(1f),
            icon = LineAwesomeIcons.ArrowUpSolid,
            color = Green,
            title = "Income",
            amount = formatIncome,
            amountInPrimaryCurrency = formatIncomeAlt
        )

        Spacer(modifier = Modifier.width(8.dp))

        SummaryAmountCard(
            modifier = Modifier.weight(1f),
            icon = LineAwesomeIcons.ArrowDownSolid,
            color = Red,
            title = "Expenses",
            amount = formatExpense,
            amountInPrimaryCurrency = formatExpenseAlt
        )
    }
}
