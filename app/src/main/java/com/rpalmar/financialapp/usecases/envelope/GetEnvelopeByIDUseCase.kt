package com.rpalmar.financialapp.usecases.envelope

import android.util.Log
import com.rpalmar.financialapp.models.domain.EnvelopeDomain
import com.rpalmar.financialapp.providers.database.repositories.EnvelopeRepository
import javax.inject.Inject

class GetEnvelopeByIDUseCase @Inject constructor(
    private val envelopeRepository: EnvelopeRepository
) {
    suspend operator fun invoke(id: Long): EnvelopeDomain? {
        try {
            //GET ENVELOPE
            val envelopeEntity = envelopeRepository.getEnvelopeWithCurrencyByID(id);

            //VALIDATE ENVELOPE
            if(envelopeEntity == null){
                Log.e("GetEnvelopeByIDUseCase", "Error al obtener la cuenta")
                return null;
            }

            //MAP TO DOMAIN
            val envelopeDomain = envelopeEntity.toDomain();

            //RETURN DATA
            Log.i("GetEnvelopeByIDUseCase", "💳 Envelope Obtain: $envelopeDomain")
            return envelopeDomain;
        } catch (ex: Exception) {
            Log.e("GetEnvelopeByIDUseCase", ex.message.toString());
            return null;
        }
    }
}
