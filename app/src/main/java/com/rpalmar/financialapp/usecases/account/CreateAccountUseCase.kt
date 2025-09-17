package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(newAccount: AccountDomain): Boolean {
        try {
            //MAP TO ENTITY
            val newAccountEntity = newAccount.toEntity();

            //SAVE NEW ENTITY
            accountRepository.insert(newAccountEntity);

            Log.i("CreateAccountUseCase", "💳 Entity created: $newAccount");
            return true;
        } catch (ex: Exception) {
            Log.e("CreateAccountUseCase", ex.message.toString());
            return false;
        }
    }

}