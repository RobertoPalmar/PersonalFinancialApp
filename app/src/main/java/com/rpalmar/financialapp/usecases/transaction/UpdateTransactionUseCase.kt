package com.rpalmar.financialapp.usecases.transaction

import android.util.Log
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateTransactionUseCase @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) {

    suspend operator fun invoke(
        existingTransactionID: Long,
        newOriginTransaction: TransactionDomain,
        newOriginSource: SimpleTransactionSourceAux,
        newDestinationTransaction: TransactionDomain? = null,
        newDestinationSource: SimpleTransactionSourceAux? = null,
        exchangeRate: Double? = null
    ): Boolean {
        return try {
            //DELETE EXISTING TRANSACTION
            val deleteResult = deleteTransactionUseCase(existingTransactionID)
            if (deleteResult != true) {
                Log.e("UpdateTransactionUseCase", "❌ DELETE FAILED")
                return false
            }

            //CREATE NEW TRANSACTION
            val createResult = createTransactionUseCase(
                originTransaction = newOriginTransaction,
                originSource = newOriginSource,
                destinationTransaction = newDestinationTransaction,
                destinationSource = newDestinationSource,
                exchangeRate = exchangeRate
            )
            if (!createResult) {
                Log.e("UpdateTransactionUseCase", "❌ CREATE FAILED")
                return false
            }

            //TRANSACTION UPDATED SUCCESSFULLY
            Log.i("UpdateTransactionUseCase", "✅ TRANSACTION UPDATED")
            true

        } catch (ex: Exception) {
            Log.e("UpdateTransactionUseCase", ex.message.toString())
            false
        }
    }
}
