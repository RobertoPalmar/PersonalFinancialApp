package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAccountsListUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(): Flow<List<AccountDomain>>? {
        try {
            //GET ACCOUNTS
            val accounts = accountRepository.getAccountListWithCurrency();

            //MAP TO DOMAIN
            val accountsDomainList = accounts.map { list -> list.map { it.toDomain() } };

            //RETURN DATA
            Log.i("GetAccountsUseCase", "💳 Account Obtain: ${accountsDomainList.map { it.toString() }}")
            return accountsDomainList;
        } catch (ex: Exception) {
            Log.e("GetAccountsUseCase", ex.message.toString());
            return null;
        }
    }
}