package com.rpalmar.financialapp.usecases.account

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
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
class GetTransactionListUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
) {
    fun getPagingDataByAccount(accountID: Long, pageSize: Int = 20): Flow<PagingData<TransactionDomain>> {
        return flow {
            //GET ACCOUNT
            val account = accountRepository.getByID(accountID)

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
                            val auxOriginSource = accountAuxMap[transaction.sourceID]

                            //IF TRANSFER, GET RELATED TRANSACTION
                            var linkedTransactionDomain: TransactionDomain? = null;
                            if(transaction.transactionType == TransactionType.TRANSFER){
                                val linkedTransaction = transactionRepository.getTransactionWithCurrencyByID(transaction.linkedTransactionID!!)
                                val linkedTransactionSource = accountAuxMap[linkedTransaction!!.transaction.sourceID]
                                linkedTransactionDomain = linkedTransaction.toDomain(linkedTransactionSource!!);
                            }

                            //MAP TO DOMAIN
                            transactionWithCurrency.toDomain(auxOriginSource!!, linkedTransactionDomain)
                        }
                    }

                //RETURN FLOW WITH DATA
                emitAll(transactionFlow)
            }
        }
    }

    fun getPagingData(pageSize: Int = 20): Flow<PagingData<TransactionDomain>> {
        return flow {
            //GET ACCOUNT AUX MAP
            var accountList = accountRepository.getAccountListWithCurrencyWithDelete().first();
            val accountAuxMap = accountList.map { it.toDomain().toAuxDomain() }.associateBy { it.id }

            //FORMAT TRANSACTION AND RETURN DOMAIN
            val transactionFlow = transactionRepository.getLastPaginated(pageSize)
                .map { pagingData ->
                    pagingData.map { transactionWithCurrency ->
                        //GET AUX DATA
                        val transaction = transactionWithCurrency.transaction;
                        val auxOriginSource = accountAuxMap[transaction.sourceID]

                        //IF TRANSFER, GET RELATED TRANSACTION
                        var linkedTransactionDomain: TransactionDomain? = null;
                        if(transaction.transactionType == TransactionType.TRANSFER){
                            val linkedTransaction = transactionRepository.getTransactionWithCurrencyByID(transaction.linkedTransactionID!!)
                            val linkedTransactionSource = accountAuxMap[linkedTransaction!!.transaction.sourceID]
                            linkedTransactionDomain = linkedTransaction.toDomain(linkedTransactionSource!!);
                        }

                        //MAP TO DOMAIN
                        transactionWithCurrency.toDomain(auxOriginSource!!,linkedTransactionDomain)
                    }
                }

            //RETURN FLOW WITH DATA
            emitAll(transactionFlow)
        }
    }
}