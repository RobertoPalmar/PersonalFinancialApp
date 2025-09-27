package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.models.domain.auxiliar.AccountDashboardData
import com.rpalmar.financialapp.usecases.currency.GetMainCurrencyUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAccountDashboardDataUseCase @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getMainCurrencyUseCase: GetMainCurrencyUseCase
) {
    suspend operator fun invoke(): AccountDashboardData?{
        try{
            //GET ACCOUNT DATA
            var accountListFlow = getAccountsUseCase();
            if(accountListFlow == null){
                Log.e("GetAccountDashboardDataUseCase", "Error al obtener la lista de cuentas")
                return null;
            }
            var accountList = accountListFlow.first();

            //GET ACCOUNT BALANCE
            var totalAccountBalance = accountList.sumOf{ it.initBalance * it.currency.exchangeRate }

            //GET MAIN CURRENCY
            var mainCurrency = getMainCurrencyUseCase();
            if(mainCurrency == null){
                Log.e("GetAccountDashboardDataUseCase", "Error al obtener la moneda principal")
                return null;
            }

            //RETURN DATA
            return AccountDashboardData(
                accountList = accountList,
                mainCurrency = mainCurrency,
                totalAccountBalance = totalAccountBalance
            )
        } catch (ex: Exception) {
            Log.e("GetAccountDashboardDataUseCase", ex.message.toString());
            return null;
        }
    }
}