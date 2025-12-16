package com.rpalmar.financialapp.usecases.transaction

import com.rpalmar.financialapp.models.TransactionSourceType
import com.rpalmar.financialapp.models.domain.TransactionDomain
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import com.rpalmar.financialapp.providers.database.repositories.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTransactionByIDUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
) {
    suspend operator fun invoke(id: Long): TransactionDomain? {
        val transactionWithCurrency = transactionRepository.getTransactionWithCurrencyByID(id)
        if (transactionWithCurrency != null) {
            val transaction = transactionWithCurrency.transaction

            val sourceAux: SimpleTransactionSourceAux? = when (transaction.sourceType) {
                TransactionSourceType.ACCOUNT -> {
                    val account = accountRepository.getAccountWithCurrencyByID(transaction.sourceID)
                    account?.toDomain()?.toAuxDomain()
                }
            }

            var linkedTransactionDomain: TransactionDomain? = null
            if (transaction.linkedTransactionID != null) {
                linkedTransactionDomain = invoke(transaction.linkedTransactionID)
            }

            return transactionWithCurrency.toDomain(sourceAux!!, linkedTransactionDomain)
        }
        return null
    }
}
