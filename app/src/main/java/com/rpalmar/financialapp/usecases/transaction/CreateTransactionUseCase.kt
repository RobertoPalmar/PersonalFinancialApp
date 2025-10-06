package com.rpalmar.financialapp.usecases.transaction

import android.util.Log
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend operator fun invoke(newTransaction: TransactionDomain): Boolean {
        try {
            //MAP TO ENTITY
            val newTransactionEntity = newTransaction.toEntity();

            //SAVE NEW ENTITY
            transactionRepository.insert(newTransactionEntity);

            Log.i("CreateTransactionUseCase", "💳 Entity created: $newTransactionEntity");
            return true;
        } catch (ex: Exception) {
            Log.e("CreateTransactionUseCase", ex.message.toString());
            return false;
        }
    }

}