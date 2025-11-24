package com.rpalmar.financialapp.views.mainMenu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rpalmar.financialapp.mock.MockupProvider
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.ui.MainMenuItem
import com.rpalmar.financialapp.providers.sealeds.MainSectionContent
import com.rpalmar.financialapp.views.account.AccountDataCard
import com.rpalmar.financialapp.views.ui.animations.CarouselAnimatedSummary
import com.rpalmar.financialapp.views.ui.components.refactor.AccountRow
import com.rpalmar.financialapp.views.ui.components.refactor.CreditCardIcon
import com.rpalmar.financialapp.views.ui.components.refactor.CurrencyRow
import com.rpalmar.financialapp.views.ui.components.refactor.DefaultIcon
import com.rpalmar.financialapp.views.ui.components.refactor.IncomeExpenseSection
import com.rpalmar.financialapp.views.ui.components.refactor.MainLayout
import com.rpalmar.financialapp.views.ui.components.refactor.TransactionRow
import com.rpalmar.financialapp.views.ui.components.refactor.formatAmount
import com.rpalmar.financialapp.views.ui.theme.Blue
import com.rpalmar.financialapp.views.ui.theme.DarkGrey
import com.rpalmar.financialapp.views.ui.theme.FinancialTheme
import com.rpalmar.financialapp.views.ui.theme.LightGreen
import com.rpalmar.financialapp.views.ui.theme.Orange
import com.rpalmar.financialapp.views.ui.theme.Red
import com.rpalmar.financialapp.views.ui.theme.White
import compose.icons.LineAwesomeIcons
import compose.icons.Octicons
import compose.icons.lineawesomeicons.ArrowLeftSolid
import compose.icons.lineawesomeicons.CoinsSolid
import compose.icons.lineawesomeicons.ExchangeAltSolid
import compose.icons.lineawesomeicons.TagsSolid
import compose.icons.lineawesomeicons.WalletSolid
import compose.icons.octicons.PlusCircle24

@Composable
fun MainMenuScreen(

) {
    val context = LocalContext.current
    var currentSection by remember { mutableStateOf<MainSectionContent>(MainSectionContent.Home) }

    //ON BACK LOGIC
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        when (currentSection) {
            MainSectionContent.Home -> {
                showExitDialog = true
            }
            else -> {
                currentSection = MainSectionContent.Home
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 4.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            title = {
                Text(
                    text = "Salir",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "¿Deseas salir de la aplicación?",
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Text(
                    "Sí",
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkGrey,
                    modifier = Modifier
                        .clickable {
                            showExitDialog = false
                            // Cerrar la app
                            (context as? Activity)?.finish()
                        }
                        .padding(10.dp)
                )
            },
            dismissButton = {
                Text(
                    "No",
                    style = MaterialTheme.typography.titleSmall,
                    color = DarkGrey,
                    modifier = Modifier
                        .clickable {
                            showExitDialog = false
                        }
                        .padding(10.dp)
                )
            }
        )
    }

    //ANIMATION LOGIC
    var previousSection by remember { mutableStateOf<MainSectionContent>(currentSection) }

    LaunchedEffect(currentSection) {
        previousSection = currentSection
    }

    MainLayout {
        Column {

            //---------------------------SUMMARY SECTION---------------------------//
            CarouselAnimatedSummary(currentSection, previousSection) { section ->
                SummaryContent(section)
            }

            Spacer(modifier = Modifier.height(6.dp))

            //---------------------------WHITE SECTION---------------------------//
            BackgroundWhiteCardSection {

                when (currentSection) {
                    is MainSectionContent.Home -> {
                        MainMenuGridSection(
                            items = listOf(
                                MainMenuItem("Accounts", LineAwesomeIcons.WalletSolid, Orange),
                                MainMenuItem("Categories", LineAwesomeIcons.TagsSolid, LightGreen),
                                MainMenuItem("Currencies", LineAwesomeIcons.CoinsSolid, Red),
                                MainMenuItem("Transactions", LineAwesomeIcons.ExchangeAltSolid, Blue)
                            ),
                            onItemClick = { item ->
                                when (item.title) {
                                    "Accounts" -> currentSection = MainSectionContent.Accounts
                                    "Transactions" -> currentSection = MainSectionContent.Transactions
                                    "Currencies" -> currentSection = MainSectionContent.Currencies
                                    "Categories" -> currentSection = MainSectionContent.Categories
                                }
                            }
                        )
//                        TransactionsListSection(
//                            title = "Last Transactions",
//                            transactions = MockupProvider.getMockTransactions(),
//                            onClick = {}
//                        )
                    }

                    //ACCOUNT SECTION
                    is MainSectionContent.Accounts -> {
                        AccountsListSection(
                            accounts = MockupProvider.getMockAccounts(),
                            onClick = { account ->
                                currentSection = MainSectionContent.AccountDetail(account)
                            },
                            onBackClick = { currentSection = MainSectionContent.Home }
                        )
                    }

                    is MainSectionContent.AccountDetail -> {
                        TransactionsListSection(
                            title = "Account Transactions",
                            transactions = MockupProvider.getMockTransactions(),
                            onClick = {},
                            onBackClick = { currentSection = MainSectionContent.Accounts }
                        )
                    }

                    //TRANSACTION SECTION
                    is MainSectionContent.Transactions -> {
                        TransactionsListSection(
                            title = "Last Transactions",
                            transactions = MockupProvider.getMockTransactions(),
                            onClick = {},
                            onBackClick = { currentSection = MainSectionContent.Home }
                        )
                    }

                    //CATEGORY SECTION
                    is MainSectionContent.Categories -> {
                        CategoriesListSection(
                            categories = MockupProvider.getMockCategories(),
                            onClick = {},
                            onBackClick = { currentSection = MainSectionContent.Home }
                        )
                    }

                    //CURRENCY SECTION
                    is MainSectionContent.Currencies -> {
                        CurrenciesListSection(
                            currencies = MockupProvider.getMockCurrencies(),
                            onClick = {},
                            onBackClick = { currentSection = MainSectionContent.Home }
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun SummaryContent(section: MainSectionContent) {
    when (section) {
        is MainSectionContent.Home,
        is MainSectionContent.Accounts,
        is MainSectionContent.Transactions,
        is MainSectionContent.Currencies,
        MainSectionContent.Categories -> {
            GeneralSummaryBalance()
        }

        is MainSectionContent.AccountDetail -> {
            AccountSummaryBalance(section.account)
        }
    }
}


@Composable
fun GeneralSummaryBalance() {
    val totalBalance = 153215.12;
    val currencySymbol = "$"

    Column(
        modifier = Modifier
            .height(280.dp)
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .padding(top = 10.dp)
    ) {
        GreetingSection()
        TotalBalanceCard(
            totalBalance = totalBalance,
            totalBalanceInPrimaryCurrency = 1565.21,
            currencySymbol = currencySymbol
        )
        IncomeExpenseSection(
            income = 18250.00,
            expenses = 7420.25,
            firstCurrency = MockupProvider.getMockCurrencies()[0],
            altCurrency = MockupProvider.getMockCurrencies()[1]
        )
    }
}

@Composable
fun AccountSummaryBalance(account: AccountDomain) {
    Column(
        modifier = Modifier
            .height(280.dp)
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .padding(top = 10.dp)
    ) {
        AccountDataCard(
            account = MockupProvider.getMockAccounts()[0],
            onAddClick = { },
            onDeleteClick = {},
            onEditClick = { }
        )
        IncomeExpenseSection(
            income = 18250.00,
            expenses = 7420.25,
            firstCurrency = MockupProvider.getMockCurrencies()[0],
            altCurrency = MockupProvider.getMockCurrencies()[1]
        )
    }
}

@Composable
fun BackgroundWhiteCardSection(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(14.dp)
    ) {
        Spacer(modifier = Modifier.padding(top = 12.dp))
        content()
    }
}

@Composable
fun MainMenuGridSection(
    items: List<MainMenuItem>,
    onItemClick: (MainMenuItem) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
    ) {
        SectionTitle("Sections")
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(items.size) { index ->
                MainMenuCard(
                    item = items[index],
                    onClick = { onItemClick(items[index]) }
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actionButton: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .padding(end = 5.dp)
                    .clickable { onBackClick() },
                painter = rememberVectorPainter(LineAwesomeIcons.ArrowLeftSolid),
                contentDescription = "back",
                tint = DarkGrey
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Title
        Text(
            text = title,
            color = DarkGrey,
            style = MaterialTheme.typography.titleMedium
        )

        if (actionButton != null) {
            actionButton()
        }
    }

    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
fun MainMenuCard(
    item: MainMenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(bottom = 8.dp, top = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 18.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ICON
            DefaultIcon(
                title = item.title,
                icon = item.icon,
                color = item.color,
                circleSize = 38.dp,
                iconSize = 22.dp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // TITLE
            Text(
                text = item.title,
                color = DarkGrey,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun GreetingSection() {
    Text(
        text = "Hi, Roberto",
        style = MaterialTheme.typography.titleLarge,
        color = White,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "Here’s your financial overview for today.",
        style = MaterialTheme.typography.bodyLarge,
        color = White
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TotalBalanceCard(
    totalBalance: Double,
    totalBalanceInPrimaryCurrency: Double? = null,
    currencySymbol: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //GENERAL DATA
                Column {
                    Text(
                        text = "General Balance",
                        style = MaterialTheme.typography.bodyLarge,
                        color = White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = formatAmount(totalBalance, currencySymbol),
                        style = MaterialTheme.typography.titleLarge,
                        color = White
                    )
                    if (totalBalanceInPrimaryCurrency != null) {
                        Text(
                            text = formatAmount(totalBalanceInPrimaryCurrency, currencySymbol),
                            style = MaterialTheme.typography.bodyLarge,
                            color = White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                CreditCardIcon(size = 50.dp)
            }
        }
    }
}

@Composable
fun TransactionsListSection(
    title: String,
    transactions: List<TransactionDomain>,
    onClick: (TransactionDomain) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        SectionTitle(title, onBackClick)
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 5.dp)
        ) {
            transactions.forEach { transaction ->
                TransactionRow(
                    transaction,
                    onClick = { onClick(transaction) }
                )
            }
        }
    }
}

@Composable
fun AccountsListSection(
    accounts: List<AccountDomain>,
    onBackClick: () -> Unit,
    onClick: (AccountDomain) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        SectionTitle(
            "Accounts",
            onBackClick,
            actionButton = {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Octicons.PlusCircle24),
                        contentDescription = "Add",
                        tint = DarkGrey,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 5.dp)
        ) {
            accounts.forEach { account ->
                AccountRow(
                    account,
                    onClick = { onClick(account) }
                )
            }
        }
    }
}

@Composable
fun CategoriesListSection(
    categories: List<CategoryDomain>,
    onBackClick: () -> Unit,
    onClick: (CategoryDomain) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        SectionTitle(
            "Categories",
            onBackClick,
            actionButton = {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Octicons.PlusCircle24),
                        contentDescription = "Add",
                        tint = DarkGrey,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = true
        ) {
            items(categories.size) { index ->
                CategoryCard(
                    category = categories[index],
                    onClick = { }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: CategoryDomain,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(bottom = 8.dp, top = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 18.dp)
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ICON
            DefaultIcon(
                title = category.name,
                icon = category.style.uiIcon,
                color = category.style.uiColor,
                circleSize = 38.dp,
                iconSize = 22.dp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // TITLE
            Text(
                text = category.name,
                color = DarkGrey,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
fun CurrenciesListSection(
    currencies: List<CurrencyDomain>,
    onBackClick: () -> Unit,
    onClick: (CurrencyDomain) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        SectionTitle(
            "Currencies",
            onBackClick,
            actionButton = {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Octicons.PlusCircle24),
                        contentDescription = "Add",
                        tint = DarkGrey,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 5.dp)
        ) {
            currencies.forEach { currency ->
                CurrencyRow(
                    currency,
                    onClick = { onClick(currency) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    FinancialTheme(darkTheme = false) {
        MainMenuScreen()
    }
}
