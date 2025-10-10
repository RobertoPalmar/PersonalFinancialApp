package com.rpalmar.financialapp.usecases.account

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomainTransaction
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.providers.database.repositories.EnvelopeRepository
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTransactionListPerAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val envelopeRepository: EnvelopeRepository
) {
    fun getPagingData(accountID: Long, pageSize:Int = 20):  Flow<PagingData<TransactionDomain>>{
        return flow {
            //GET ACCOUNT
            val account = accountRepository.getByID(accountID)

            //GET ENVELOPS AUX MAP
            var envelopeList = envelopeRepository.getEnvelopeListWithCurrencyWithDelete().first();
            val envelopeAuxMap = envelopeList.map { it.toDomain().toAuxDomain() }.associateBy { it.id }

            //GET ACCOUNT AUX MAP
            var accountList = accountRepository.getAccountListWithCurrencyWithDelete().first();
            val accountAuxMap = accountList.map { it.toDomain().toAuxDomain() }.associateBy { it.id }

            //VALIDATE ACCOUNT
            if (account == null) {
                Log.e("GetTransactionListPerAccount", "Account not found")

                //RETURN A EMPTY FLOW
                emitAll(emptyFlow())
            } else {
                //FORMAT TRANSACTION AND RETURN DOMAIN
                val transactionFlow = transactionRepository.getByAccountIDPaginated(accountID, pageSize)
                    .map { pagingData ->
                        pagingData.map { transactionWithCurrency ->
                            //GET AUX DATA
                            val transaction = transactionWithCurrency.transaction;
                            val auxOriginSource =
                                when(transaction.sourceType){
                                    TransactionSourceType.ACCOUNT -> accountAuxMap[transaction.sourceID]
                                    TransactionSourceType.ENVELOPE -> envelopeAuxMap[transaction.sourceID]
                                    else -> null
                                }

                            var linkedTransactionDomain: TransactionDomain? = null
                            //PROCESO PARA OBTENER TRANSACCION ASOCIADA CON EL LISTADO
                            //TODO: REFACTORIZAR PARA MEJORAR PERFORMANCE
//                            if(transaction.linkedTransactionID != null){
//                                val linkedTransactionWithCurrency = transactionRepository.getTransactionWithCurrencyByID(transaction.linkedTransactionID)
//                                if (linkedTransactionWithCurrency != null) {
//                                    val linkedTransaction = linkedTransactionWithCurrency.transaction
//                                    val linkedTransactionSource = when(linkedTransaction.sourceType){
//                                        TransactionSourceType.ACCOUNT -> accountAuxMap[linkedTransaction.sourceID]
//                                        TransactionSourceType.ENVELOPE -> envelopeAuxMap[linkedTransaction.sourceID]
//                                        else -> null
//                                    }
//                                    linkedTransactionDomain = linkedTransactionWithCurrency.toDomain(linkedTransactionSource!!)
//                                }
//                            }

                            //MAP TO DOMAIN
                            transactionWithCurrency.toDomain(auxOriginSource!!, linkedTransactionDomain)
                        }
                    }

                //RETURN FLOW WITH DATA
                emitAll(transactionFlow)
            }
        }
    }
}