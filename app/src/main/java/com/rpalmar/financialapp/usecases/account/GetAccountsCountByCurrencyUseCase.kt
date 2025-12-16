package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAccountsCountByCurrencyUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(currencyId: Long): Int? {
        try {
            //GET ACCOUNT COUNT
            val accountCountByCurrency = accountRepository.getAccountsCountByCurrency(currencyId)

            return accountCountByCurrency;
        } catch (ex: Exception) {
            Log.e("GetAccountsCountByCurrencyUseCase", ex.message.toString());
            return null;
        }
    }
}
