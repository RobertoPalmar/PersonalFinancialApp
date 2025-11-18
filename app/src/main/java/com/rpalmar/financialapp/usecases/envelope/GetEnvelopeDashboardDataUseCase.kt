package com.rpalmar.financialapp.usecases.envelope

import android.util.Log
import com.rpalmar.financialapp.models.domain.auxiliar.AccountDashboardData
import com.rpalmar.financialapp.models.domain.auxiliar.EnvelopeDashboardData
import com.rpalmar.financialapp.usecases.currency.GetMainCurrencyUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetEnvelopeDashboardDataUseCase @Inject constructor(
    private val getEnvelopeListUseCase: GetEnvelopeListUseCase,
    private val getMainCurrencyUseCase: GetMainCurrencyUseCase
) {

    val TAG = "GetEnvelopeDashboardDataUseCase"

    suspend operator fun invoke():EnvelopeDashboardData?{
        try{
            //GET ACCOUNT DATA
            var envelopeListFlow = getEnvelopeListUseCase();
            if(envelopeListFlow == null){
                Log.e(TAG, "Error al obtener la lista de sobres")
                return null;
            }
            var envelopeList = envelopeListFlow.first();

            //GET ACCOUNT BALANCE
            var totalEnvelopeBalance = envelopeList.sumOf{ it.balanceInMainCurrency }

            //GET MAIN CURRENCY
            var mainCurrency = getMainCurrencyUseCase();
            if(mainCurrency == null){
                Log.e(TAG, "Error al obtener la moneda principal")
                return null;
            }

            //RETURN DATA
            return EnvelopeDashboardData(
                envelopeList = envelopeList,
                mainCurrency = mainCurrency,
                totalEnvelopeBalance = totalEnvelopeBalance
            )
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }
}