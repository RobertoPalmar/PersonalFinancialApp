package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAccountByIDUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend fun invoke(accountID:Long): AccountDomain?{
        try {
            //GET ACCOUNTS
            val accountEntity = accountRepository.getAccountWithCurrencyByID(accountID);

            //VALIDATE ACCOUNT
            if(accountEntity == null){
                Log.e("GetAccountsUseCase", "Error al obtener la cuenta")
                return null;
            }

            //MAP TO DOMAIN
            val accountDomain = accountEntity.toDomain();

            //RETURN DATA
            Log.i("GetAccountsUseCase", "💳 Account Obtain: $accountDomain")
            return accountDomain;
        } catch (ex: Exception) {
            Log.e("GetAccountsUseCase", ex.message.toString());
            return null;
        }
    }
}