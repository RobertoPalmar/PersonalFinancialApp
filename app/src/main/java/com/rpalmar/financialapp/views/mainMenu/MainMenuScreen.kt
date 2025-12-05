package com.rpalmar.financialapp.views.mainMenu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.AccountSummaryData
import com.rpalmar.financialapp.models.domain.auxiliar.MainMenuItem
import com.rpalmar.financialapp.providers.sealeds.MainSectionContent
import com.rpalmar.financialapp.providers.sealeds.ScreenSections
import com.rpalmar.financialapp.views.account.data.AccountViewModel
import com.rpalmar.financialapp.views.category.data.CategoryViewModel
import com.rpalmar.financialapp.views.mainMenu.data.MainMenuUIState
import com.rpalmar.financialapp.views.mainMenu.data.MainMenuViewModel
import com.rpalmar.financialapp.views.navigation.LocalAppViewModel
import com.rpalmar.financialapp.views.transaction.data.TransactionViewModel
import com.rpalmar.financialapp.views.ui.animations.CarouselAnimatedSummary
import com.rpalmar.financialapp.views.ui.components.summaryCard.AccountDataCard
import com.rpalmar.financialapp.views.ui.components.itemRows.AccountRow
import com.rpalmar.financialapp.views.ui.components.CreditCardIcon
import com.rpalmar.financialapp.views.ui.components.itemRows.CurrencyRow
import com.rpalmar.financialapp.views.ui.components.DefaultIcon
import com.rpalmar.financialapp.views.ui.components.IncomeExpenseSection
import com.rpalmar.financialapp.views.ui.components.LoadingScreen
import com.rpalmar.financialapp.views.ui.components.MainLayout
import com.rpalmar.financialapp.views.ui.components.ModalDialog
import com.rpalmar.financialapp.views.ui.components.itemRows.TransactionRow
import com.rpalmar.financialapp.views.ui.components.TransactionTypeDialog
import com.rpalmar.financialapp.views.ui.components.formatAmount
import com.rpalmar.financialapp.views.ui.components.summaryCard.TotalBalanceCard
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
    navController: NavController,
    viewModel: MainMenuViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel,
    categoryViewModel: CategoryViewModel,
    transactionViewModel: TransactionViewModel
) {
    //GLOBAL ACCESS
    val context = LocalContext.current
    val uiState by viewModel.mainMenuUIState.collectAsState()

    //TOOL FUNCTIONS
    fun onCreateAccountHandler() {
        accountViewModel.cleanForm()
        navController.navigate(ScreenSections.AccountForm.route)
    }

    fun onCreateCategoryHandler(){
        categoryViewModel.cleanForm()
        navController.navigate(ScreenSections.CategoryForm.route)
    }

    //SECTION UI
    var currentSection by rememberSaveable(stateSaver = MainSectionContent.Saver) { 
        mutableStateOf<MainSectionContent>(MainSectionContent.Home) 
    }

    //ON BACK LOGIC
    var showExitDialog by remember { mutableStateOf(false) }

    //ANIMATION LOGIC
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

    var previousSection by remember { mutableStateOf<MainSectionContent>(currentSection) }
    LaunchedEffect(currentSection) {
        previousSection = currentSection
    }

    //EXIT DIALOG LOGIC
    if (showExitDialog) {
        ModalDialog(
            title = "Salir",
            message = "¿Deseas salir de la aplicación?",
            onDismiss = { showExitDialog = false },
            onAccept = {
                showExitDialog = false
                (context as? Activity)?.finish()
            }
        )
    }

    //LOAD BASIC DATA
    LaunchedEffect(true) {
        viewModel.loadDashboardData()
    }

    MainLayout {
        Column {

            //---------------------------SUMMARY SECTION---------------------------//
            CarouselAnimatedSummary(currentSection, previousSection) { section ->
                if (uiState.isLoading) {
                    LoadingScreen()
                } else {
                    SummaryContent(
                        viewModel = viewModel,
                        accountViewModel = accountViewModel,
                        transactionViewModel = transactionViewModel,
                        uiState = uiState,
                        navController = navController,
                        section = section,
                        onNavigateToSection = { newSection -> currentSection = newSection }
                    )
                }
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
                    }

                    //ACCOUNT SECTION
                    is MainSectionContent.Accounts -> {
                        AccountsListSection(
                            viewModel = viewModel,
                            onAddClick = { onCreateAccountHandler() },
                            onAccountClick = { account -> currentSection = MainSectionContent.AccountDetail(account) },
                            onBackClick = { currentSection = MainSectionContent.Home }
                        )
                    }

                    is MainSectionContent.AccountDetail -> {
                        TransactionsListSection(
                            title = "Account Transactions",
                            account = (currentSection as MainSectionContent.AccountDetail).account,
                            viewModel = viewModel,
                            onClick = {},
                            onBackClick = { currentSection = MainSectionContent.Accounts }
                        )
                    }

                    //TRANSACTION SECTION
                    is MainSectionContent.Transactions -> {
                        TransactionsListSection(
                            title = "Last Transactions",
                            viewModel = viewModel,
                            onClick = {},
                            onBackClick = { currentSection = MainSectionContent.Home }
                        )
                    }

                    //CATEGORY SECTION
                    is MainSectionContent.Categories -> {
                        CategoriesListSection(
                            viewModel = viewModel,
                            onAddClick = { onCreateCategoryHandler() },
                            onClick = {},
                            onBackClick = { currentSection = MainSectionContent.Home }
                        )
                    }

                    //CURRENCY SECTION
                    is MainSectionContent.Currencies -> {
                        CurrenciesListSection(
                            viewModel = viewModel,
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
fun SummaryContent(
    viewModel: MainMenuViewModel,
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionViewModel,
    uiState: MainMenuUIState,
    navController: NavController,
    section: MainSectionContent,
    onNavigateToSection: (MainSectionContent) -> Unit
) {
    when (section) {
        is MainSectionContent.Home,
        is MainSectionContent.Accounts,
        is MainSectionContent.Transactions,
        is MainSectionContent.Currencies,
        MainSectionContent.Categories -> {
            GeneralSummaryBalance(uiState)
        }

        is MainSectionContent.AccountDetail -> {
            LaunchedEffect(section.account.id) {
                viewModel.loadAccountSummaryData(section.account)
            }

            if (uiState.isLoading || uiState.accountSummaryData == null) {
                LoadingScreen()
            } else {
                AccountSummaryBalance(
                    accountViewModel = accountViewModel,
                    transactionViewModel = transactionViewModel,
                    accountSummaryData = uiState.accountSummaryData!!,
                    navController = navController,
                    onBackNavigation = { onNavigateToSection(MainSectionContent.Accounts) },
                    onEditNavigation = { navController.navigate(ScreenSections.AccountForm.route) }
                )
            }
        }
    }
}


@Composable
fun GeneralSummaryBalance(
    uiState: MainMenuUIState
) {
    val mainCurrency by LocalAppViewModel.current.mainCurrency.collectAsState()
    val currencySymbol = mainCurrency!!.symbol;

    Column(
        modifier = Modifier
            .height(280.dp)
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .padding(top = 10.dp)
    ) {
        GreetingSection()
        TotalBalanceCard(
            totalBalance = uiState.dashboardData.totalBalance,
            currencySymbol = currencySymbol
        )
        IncomeExpenseSection(
            income = uiState.dashboardData.generalIncome,
            expenses = uiState.dashboardData.generalExpense,
            firstCurrency = mainCurrency!!
        )
    }
}

@Composable
fun AccountSummaryBalance(
    accountViewModel: AccountViewModel,
    transactionViewModel: TransactionViewModel,
    accountSummaryData: AccountSummaryData,
    onBackNavigation: () -> Unit,
    onEditNavigation: () -> Unit,
    navController: NavController
) {
    val mainCurrency by LocalAppViewModel.current.mainCurrency.collectAsState()

    //DELETE DIALOG
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showTransactionDialog by remember { mutableStateOf(false) }

    fun handleDeleteAccount() {
        accountViewModel.handleDeleteAccount(accountSummaryData.account.id)
        onBackNavigation();
    }

    fun handleEditAccount() {
        accountViewModel.handleUpdateAccountForm(accountSummaryData.account)
        onEditNavigation();
    }

    fun handleCreateAccountTransaction(transactionType: TransactionType){
        val route = "transactionForm/${transactionType.name}?sourceType=${TransactionSourceType.ACCOUNT}&sourceId=${accountSummaryData.account.id}"
        transactionViewModel.cleanForm(transactionType)
        navController.navigate(route)
        showTransactionDialog = false
    }

    if (showDeleteDialog) {
        ModalDialog(
            title = "Delete Account",
            message = "Are you sure you want to delete this account?",
            onAccept = { handleDeleteAccount() },
            onDismiss = { showDeleteDialog = false }
        )
    }

    if (showTransactionDialog) {
        TransactionTypeDialog(
            onDismiss = { showTransactionDialog = false },
            onTypeSelected = { transactionType -> handleCreateAccountTransaction(transactionType)}
        )
    }

    Column(
        modifier = Modifier
            .height(280.dp)
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .padding(top = 10.dp)
    ) {
        AccountDataCard(
            account = accountSummaryData.account,
            mainCurrency = mainCurrency!!,
            onAddTransactionClick = { showTransactionDialog = true },
            onDeleteAccountClick = { showDeleteDialog = true },
            onEditAccountClick = { handleEditAccount() }
        )
        IncomeExpenseSection(
            income = accountSummaryData.income,
            expenses = accountSummaryData.expense,
            firstCurrency = accountSummaryData.account.currency,
            altCurrency =
                if (accountSummaryData.account.currency.id != mainCurrency!!.id) mainCurrency
                else null
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
fun TransactionsListSection(
    title: String,
    account: AccountDomain? = null,
    viewModel: MainMenuViewModel,
    onClick: (TransactionDomain) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val transactions =
        if (account != null) viewModel.getTransactionsPerAccountPaginated(account.id).collectAsLazyPagingItems()
        else viewModel.getTransactionsPaginated().collectAsLazyPagingItems()
    val overscrollEffect = rememberOverscrollEffect()

    val mainCurrency by LocalAppViewModel.current.mainCurrency.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        //TITLE
        SectionTitle(title, onBackClick)

        //ACCOUNT ROWS
        LazyColumn(
            modifier = Modifier
                .overscroll(overscrollEffect),
            contentPadding = PaddingValues(vertical = 5.dp),
            state = rememberLazyListState()
        ) {
            when {
                //HANDLE LOADING STATE
                transactions.loadState.refresh is LoadState.Loading -> {
                    item {
                        Column {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
                //HANDLE NO ITEMS
                transactions.itemCount == 0 -> {
                    item {
                        Text(
                            text = "Aún no hay transacciones",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = DarkGrey
                        )
                    }
                }
                //HANDLE TRANSACTIONS
                else -> {
                    items(transactions.itemCount) { index ->
                        val transaction = transactions[index]
                        transaction?.let {
                            TransactionRow(
                                transaction,
                                mainCurrency = mainCurrency!!,
                                onClick = { onClick(transaction) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountsListSection(
    viewModel: MainMenuViewModel,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
    onAccountClick: (AccountDomain) -> Unit
) {
    val accounts = viewModel.getAccountsPaginated().collectAsLazyPagingItems()
    val overscrollEffect = rememberOverscrollEffect()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        //TITLE
        SectionTitle(
            "Accounts",
            onBackClick,
            actionButton = {
                IconButton(
                    onClick = { onAddClick() },
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

        //ACCOUNT ROWS
        LazyColumn(
            modifier = Modifier
                .overscroll(overscrollEffect),
            contentPadding = PaddingValues(vertical = 5.dp),
            state = rememberLazyListState()
        ) {
            when {
                //HANDLE LOADING STATE
                accounts.loadState.refresh is LoadState.Loading -> {
                    item {
                        Column {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
                //HANDLE NO ITEMS
                accounts.itemCount == 0 -> {
                    item {
                        Text(
                            text = "Aún no hay cuentas",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = DarkGrey
                        )
                    }
                }
                //HANDLE ACCOUNTS
                else -> {
                    items(accounts.itemCount) { index ->
                        val account = accounts[index]
                        account?.let {
                            AccountRow(
                                account,
                                onClick = { onAccountClick(account) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesListSection(
    viewModel: MainMenuViewModel,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit,
    onClick: (CategoryDomain) -> Unit
) {
    val categories = viewModel.getCategoriesPaginated().collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        //TITLE
        SectionTitle(
            "Categories",
            onBackClick,
            actionButton = {
                IconButton(
                    onClick = {onAddClick()},
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

        //CATEGORY GRID
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = true,
            contentPadding = PaddingValues(5.dp)
        ) {
            when {
                //HANDLE LOADING STATE
                categories.loadState.refresh is LoadState.Loading -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                //HANDLE EMPTY LIST
                categories.itemCount == 0 -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "Aún no hay categorías",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = DarkGrey
                        )
                    }
                }

                //HANDLE CATEGORY ITEMS
                else -> {
                    items(categories.itemCount) { index ->
                        val category = categories[index]
                        category?.let {
                            CategoryCard(
                                category = it,
                                onClick = { /* TODO */ }
                            )
                        }
                    }
                }
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
    viewModel: MainMenuViewModel,
    onBackClick: () -> Unit,
    onClick: (CurrencyDomain) -> Unit
) {
    val currencies = viewModel.getCurrenciesPaginated().collectAsLazyPagingItems()
    val overscrollEffect = rememberOverscrollEffect()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(25.dp, 5.dp)
            .padding(bottom = 30.dp)
    ) {
        //TITLE
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

        //ACCOUNT ROWS
        LazyColumn(
            modifier = Modifier
                .overscroll(overscrollEffect),
            contentPadding = PaddingValues(vertical = 5.dp),
            state = rememberLazyListState()
        ) {
            when {
                //HANDLE LOADING STATE
                currencies.loadState.refresh is LoadState.Loading -> {
                    item {
                        Column {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
                //HANDLE NO ITEMS
                currencies.itemCount == 0 -> {
                    item {
                        Text(
                            text = "Aún no hay monedas",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            color = DarkGrey
                        )
                    }
                }
                //HANDLE CURRENCIES
                else -> {
                    items(currencies.itemCount) { index ->
                        val currency = currencies[index]
                        currency?.let {
                            CurrencyRow(
                                currency,
                                onClick = { onClick(currency) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    FinancialTheme(darkTheme = false) {
        MainMenuScreen(
            navController = NavController(LocalContext.current),
            viewModel = hiltViewModel(),
            accountViewModel = hiltViewModel(),
            transactionViewModel = hiltViewModel(),
            categoryViewModel = hiltViewModel()
        )
    }
}
