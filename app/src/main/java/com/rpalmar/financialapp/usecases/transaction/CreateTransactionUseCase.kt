package com.rpalmar.financialapp.usecases.transaction

import android.util.Log
import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.TransactionType
import com.rpalmar.financialapp.models.database.TransactionEntity
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.models.interfaces.IDomainTransaction
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.providers.database.repositories.EnvelopeRepository
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

@Singleton
class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val envelopeRepository: EnvelopeRepository
) {

    suspend operator fun invoke(
        originTransaction: TransactionDomain,
        originSource: SimpleTransactionSourceAux,
        destinationTransaction: TransactionDomain? = null,
        destinationSource: SimpleTransactionSourceAux? = null,
        exchangeRate:Double? = null
    ): Boolean {
        try {
            //VALIDATE ORIGIN SOURCE
            if(originSource.id != originTransaction.source.id){
                Log.e("CreateTransactionUseCase","💳 Invalid transaction. Transaction Source is not valid")
                return false;
            }

            //SET AMOUNT FOR BASE TRANSACTION
            val sourceTransactionAmount = when (originTransaction.transactionType) {
                TransactionType.INCOME -> abs(originTransaction.amount)
                TransactionType.EXPENSE -> -abs(originTransaction.amount)
                TransactionType.ADJUSTMENT -> originTransaction.amount
                TransactionType.TRANSFER -> -abs(originTransaction.amount)
            }

            //MAP TO ENTITY
            val newOriginTransaction = originTransaction.toEntity().copy(amount = sourceTransactionAmount)

            //CREATE TRANSACTION
            val newOriginTransactionID = transactionRepository.insert(newOriginTransaction)
            
            //UPDATE ORIGIN SOURCE BALANCE
            updateSourceBalance(originTransaction.source.id, originTransaction.source.transactionEntityType, sourceTransactionAmount)

            //CREATE RELATED TRANSACTION IN TRANSFER CASE
            if(originTransaction.transactionType == TransactionType.TRANSFER){
                //VALIDATE TRANSACTION DESTINATION
                if(destinationTransaction == null || destinationSource == null) {
                    Log.e("CreateTransactionUseCase","💳 Invalid transaction. Transaction Destination is null")
                    return false;
                }

                //VALIDATE DESTINATION SOURCE
                if(destinationSource.id != destinationTransaction.source.id){
                    Log.e("CreateTransactionUseCase","💳 Invalid transaction destination. Transaction Source is not valid")
                    return false;
                }

                //CALCULATE DESTINATION TRANSACTION AMOUNT
                val destinationTransactionAmount = calculateDestinationTransactionAmount(originTransaction.amount, exchangeRate!!)

                //MAP TO ENTITY
                val newDestinationTransaction = destinationTransaction.toEntity().copy(
                    amount = destinationTransactionAmount,
                    linkedTransactionID = newOriginTransactionID
                )

                //CREATE TRANSACTION
                val newDestinationTransactionID = transactionRepository.insert(newDestinationTransaction)

                //UPDATE ORIGIN SOURCE BALANCE
                updateSourceBalance(destinationTransaction.source.id, destinationTransaction.source.transactionEntityType, destinationTransactionAmount)

                //RELATE ORIGIN TRANSACTION WITH DESTINATION
                transactionRepository.update(newOriginTransaction.copy(linkedTransactionID = newDestinationTransactionID))
            }

            Log.i("CreateTransactionUseCase", "💳 Transaction created: $newOriginTransaction")
            return true
        } catch (ex: Exception) {
            Log.e("CreateTransactionUseCase", ex.message.toString())
            return false
        }
    }

    /**
     * Calculate the amount of the destination transaction
     */
    private fun calculateDestinationTransactionAmount(originAmount: Double, exchangeRate: Double): Double{
        return originAmount * exchangeRate
    }

    private suspend fun updateSourceBalance(sourceID:Long, sourceType:TransactionSourceType, amount:Double) {
        when (sourceType) {
            TransactionSourceType.ACCOUNT -> {
                accountRepository.updateBalance(sourceID, amount)
            }
            TransactionSourceType.ENVELOPE -> {
                envelopeRepository.updateBalance(sourceID, amount)
            }
        }
    }
}