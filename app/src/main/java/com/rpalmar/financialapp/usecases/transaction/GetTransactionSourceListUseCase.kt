package com.rpalmar.financialapp.usecases.transaction

import android.util.Log
import com.rpalmar.financialapp.models.domain.auxiliar.SimpleTransactionSourceAux
import com.rpalmar.financialapp.providers.database.repositories.AccountRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTransactionSourceListUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
){
    suspend operator fun invoke(): List<SimpleTransactionSourceAux>? {
        try {
            //GET ACCOUNTS
            val accounts = accountRepository.getAccountListWithCurrency().first();
//            val envelopes = envelopeRepository.getEnvelopeListWithCurrency().first();

            //MAP TO AUX
            val accountsDomainList = accounts.map { it.toDomain().toAuxDomain()  };
//            val envelopesDomainList = envelopes.map { it.toDomain().toAuxDomain() };

            //JOIN DATA
            val transactionSourceList = accountsDomainList // + envelopesDomainList;

            //RETURN DATA
            Log.i("GetTransactionSourceListUseCase", "💳 Transaction Source Obtain: ${transactionSourceList.map { it.toString() }}")
            return transactionSourceList;
        } catch (ex: Exception) {
            Log.e("GetTransactionSourceListUseCase", ex.message.toString());
            return null;
        }
    }
}