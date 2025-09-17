package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(accountToUpdate: AccountDomain): Boolean? {
        try {
            //MAP TO ENTITY
            val accountEntity = accountToUpdate.toEntity();

            //UPDATE ENTITY
            accountRepository.update(accountEntity);

            Log.i("UpdateAccountUseCase", "💳 Entity updated");
            return true;
        } catch (ex: Exception) {
            Log.e("UpdateAccountUseCase", ex.message.toString());
            return null;
        }
    }

}