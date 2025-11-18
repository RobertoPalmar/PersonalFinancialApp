package com.rpalmar.financialapp.usecases.envelope

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.rpalmar.financialapp.models.domain.EnvelopeDomain
import com.rpalmar.financialapp.providers.database.repositories.EnvelopeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetEnvelopeListUseCase @Inject constructor(
    private val envelopeRepository: EnvelopeRepository
) {
    val TAG = "GetEnvelopeListUseCase"

    operator fun invoke(): Flow<List<EnvelopeDomain>>? {
        try {
            //GET ACCOUNTS
            val envelopes = envelopeRepository.getEnvelopeListWithCurrency();

            //MAP TO DOMAIN
            val envelopesDomainList = envelopes.map { list -> list.map { it.toDomain() } };

            //RETURN DATA
            Log.i(TAG, "💳 Envelopes Obtain: ${envelopesDomainList.map { it.toString() }}")
            return envelopesDomainList;
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString());
            return null;
        }
    }

    fun getPaginated(pageSize: Int = 20): Flow<PagingData<EnvelopeDomain>> {
        return flow {
            val envelopeFlow = envelopeRepository.getPaginated(pageSize)
                .map { pagingData ->
                    pagingData.map { envelopeWithCurrency ->
                        //MAP TO DOMAIN
                        envelopeWithCurrency.toDomain()
                    }
                }

            //RETURN FLOW WITH DATA
            emitAll(envelopeFlow)
        }
    }
}