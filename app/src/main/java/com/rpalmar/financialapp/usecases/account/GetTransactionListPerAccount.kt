package com.rpalmar.financialapp.usecases.account

import android.util.Log
import androidx.paging.PagingData
import com.rpalmar.financialapp.models.database.relations.TransactionWithCurrencyRelation
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTransactionListPerAccount @Inject constructor(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(accountID: Long, pageSize:Int = 20):  Flow<PagingData<TransactionWithCurrencyRelation>>{
        return flow {
            //GET ACCOUNT
            val account = accountRepository.getByID(accountID)

            //VALIDATE ACCOUNT
            if (account == null) {
                Log.e("GetTransactionListPerAccount", "Account not found")

                //RETURN A EMPTY FLOW
                emitAll(emptyFlow())
            } else {
                //RETURN FLOW WITH DATA
                emitAll(transactionRepository.getByAccountID(accountID, pageSize))
            }
        }
    }
}