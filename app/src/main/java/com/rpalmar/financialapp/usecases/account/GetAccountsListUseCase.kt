package com.rpalmar.financialapp.usecases.account

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class GetAccountsListUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    val TAG = "GetAccountsUseCase"

    operator fun invoke(): Flow<List<AccountDomain>>? {
        try {
            //GET ACCOUNTS
            val accounts = accountRepository.getAccountListWithCurrency();

            //MAP TO DOMAIN
            val accountsDomainList = accounts.map { list -> list.map { it.toDomain() } };

            //RETURN DATA
            Log.i(TAG, "💳 Account Obtain: ${accountsDomainList.map { it.toString() }}")
            return accountsDomainList;
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }

    fun getPaginated(pageSize: Int = 20): Flow<PagingData<AccountDomain>> {
        return flow {
            val accountFlow = accountRepository.getPaginated(pageSize)
                .map { pagingData ->
                    pagingData.map { accountWithCurrency ->
                        //MAP TO DOMAIN
                        accountWithCurrency.toDomain()
                    }
                }

            //RETURN FLOW WITH DATA
            emitAll(accountFlow)
        }
    }
}