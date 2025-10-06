package com.rpalmar.financialapp.usecases.account

import android.util.Log
import com.rpalmar.financialapp.models.domain.AccountDomain
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
){

    suspend operator fun invoke(accountID: Long): Boolean? {
        try {
            //VALIDATE ENTITY
            var accountToDelete = accountRepository.getByID(accountID);

            if(accountToDelete == null){
                Log.i("DeleteAccountUseCase", "Entity not found");
                return false;
            }

            //DELETE ENTITY
            accountRepository.softDelete(accountToDelete.id);

            Log.i("DeleteAccountUseCase", "💳 Entity deleted")
            return true;
        } catch (ex: Exception) {
            Log.e("UpdateAccountUseCase", ex.message.toString());
            return null;
        }
    }

}