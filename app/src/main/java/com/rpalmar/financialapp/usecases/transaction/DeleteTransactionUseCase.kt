package com.rpalmar.financialapp.usecases.transaction

import android.util.Log
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(transactionID:Long): Boolean? {
        try{
            //VALIDATE TRANSACTION
            var transactionToDelete = transactionRepository.getByID(transactionID);

            //VALIDATE TRANSACTION
            if(transactionToDelete == null){
                Log.i("DeleteTransactionUseCase", "Entity not found");
                return false;
            }

            //DELETE TRANSACTION
            transactionRepository.softDelete(transactionToDelete.id)

            //REVERSE ORIGIN SOURCE BALANCE AFFECTED
            reverseSourceBalance(transactionToDelete.sourceID, transactionToDelete.sourceType, transactionToDelete.amount)

            //REMOVE RELATED TRANSACTION IN TRANSFER CASE
            if(transactionToDelete.transactionType == TransactionType.TRANSFER){
                //VALIDATE LINKED TRANSACTION
                if(transactionToDelete.linkedTransactionID == null){
                    Log.i("DeleteTransactionUseCase", "Related destination transaction not found");
                    return false;
                }

                //GET DESTINATION TRANSACTION
                val destinationTransactionToDelete = transactionRepository.getByID(transactionToDelete.linkedTransactionID)

                //VALIDATE DESTINATION TRANSACTION
                if(destinationTransactionToDelete == null) {
                    Log.i("DeleteTransactionUseCase", "Related destination transaction not found");
                    return false
                }

                //DELETE RELATED TRANSACTION
                transactionRepository.softDelete(destinationTransactionToDelete.id)

                //REVERSE RELATED SOURCE BALANCE
                reverseSourceBalance(destinationTransactionToDelete.sourceID, destinationTransactionToDelete.sourceType, destinationTransactionToDelete.amount)
            }

            Log.i("DeleteTransactionUseCase", "💳 Entity deleted")
            return true;
        } catch (ex: Exception) {
            Log.e("DeleteTransactionUseCase", ex.message.toString());
            return null;
        }
    }

    /**
     * Update the source balance base in revert the transaction amount
     */
    private suspend fun reverseSourceBalance(sourceID:Long, sourceType:TransactionSourceType, amount:Double) {
        when (sourceType) {
            TransactionSourceType.ACCOUNT -> {
                accountRepository.updateBalance(sourceID, -amount)
            }
        }
    }
}