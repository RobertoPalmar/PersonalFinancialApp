package com.rpalmar.financialapp.views.mainMenu.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.models.domain.CategoryDomain
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.usecases.account.DeleteAccountUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountSummaryDataUseCase
import com.rpalmar.financialapp.usecases.account.GetAccountsListUseCase
import com.rpalmar.financialapp.usecases.account.GetTransactionListUseCase
import com.rpalmar.financialapp.usecases.category.GetCategoryListUseCase
import com.rpalmar.financialapp.usecases.currency.GetCurrencyListUseCase
import com.rpalmar.financialapp.usecases.general.GetGeneralDashboardDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val getGeneralDashboardDataUseCase: GetGeneralDashboardDataUseCase,
    //ACCOUNT
    private val getAccountSummaryDataUseCase: GetAccountSummaryDataUseCase,
    private val getAccountListUseCase: GetAccountsListUseCase,

    private val getTransactionPerAccountUseCase: GetTransactionListUseCase,
    private val getCurrencyListUseCase: GetCurrencyListUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
):ViewModel() {

    val LOG_TAG = "MainMenuViewModel"

    //UI STATE
    private val _mainMenuUIState = MutableStateFlow(MainMenuUIState())
    val mainMenuUIState = _mainMenuUIState.asStateFlow()

    /**
     * Loads the dashboard data
     */
    fun loadDashboardData(){
        viewModelScope.launch {
            //LOAD DASHBOARD CURRENCYES
            _mainMenuUIState.value = _mainMenuUIState.value.copy(isLoading = true)
            try{
                val dashboardData = getGeneralDashboardDataUseCase();
                _mainMenuUIState.value = _mainMenuUIState.value.copy(dashboardData = dashboardData!!)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error loading dashboard data: ${e.message}")
            }finally {
                _mainMenuUIState.value = _mainMenuUIState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Return a flow with account list
     */
    fun getAccountsPaginated(): Flow<PagingData<AccountDomain>> {
        return getAccountListUseCase.getPaginated().cachedIn(viewModelScope)
    }

    /**
     * Return a flow with transactions list per account
     */
    fun getTransactionsPerAccountPaginated(accountID: Long): Flow<PagingData<TransactionDomain>> {
        return getTransactionPerAccountUseCase.getPagingDataByAccount(accountID).cachedIn(viewModelScope)
    }

    /**
     * Return a flow with last transactions list
     */
    fun getTransactionsPaginated(): Flow<PagingData<TransactionDomain>> {
        return getTransactionPerAccountUseCase.getPagingData().cachedIn(viewModelScope)
    }

    /**
     * Load account summary data
     */
    fun loadAccountSummaryData(account: AccountDomain){
        viewModelScope.launch {
            //LOAD ACCOUNT SUMMARY DATA
            _mainMenuUIState.value = _mainMenuUIState.value.copy(isLoading = true)
            try{
                val accountSummaryData = getAccountSummaryDataUseCase(account);
                _mainMenuUIState.value = _mainMenuUIState.value.copy(
                    accountSummaryData = accountSummaryData!!,
                    currentAccount = accountSummaryData.account
                )
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error loading dashboard data: ${e.message}")
            }finally {
                _mainMenuUIState.value = _mainMenuUIState.value.copy(isLoading = false)
            }
        }
    }

    /**
     * Return a flow with currency list
     */
    fun getCurrenciesPaginated(): Flow<PagingData<CurrencyDomain>> {
        return getCurrencyListUseCase.getPaginated().cachedIn(viewModelScope)
    }

    /**
     * Return a flow with category list
     */
    fun getCategoriesPaginated(): Flow<PagingData<CategoryDomain>> {
        return getCategoryListUseCase.getPaginated().cachedIn(viewModelScope)
    }

}