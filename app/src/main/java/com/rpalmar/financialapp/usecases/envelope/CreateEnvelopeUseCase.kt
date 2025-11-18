package com.rpalmar.financialapp.usecases.envelope

import android.util.Log
import com.rpalmar.financialapp.models.domain.EnvelopeDomain
import com.rpalmar.financialapp.providers.database.repositories.EnvelopeRepository
import javax.inject.Inject

class CreateEnvelopeUseCase @Inject constructor(
    private val envelopeRepository: EnvelopeRepository
) {

    suspend operator fun invoke(newEnvelope: EnvelopeDomain): Boolean{
        try{
            //MAP TO ENTITY
            val newEnvelopeEntity = newEnvelope.toEntity();

            //SAVE NEW ENTITY
            envelopeRepository.insert(newEnvelopeEntity);

            Log.i("CreateEnvelopeUseCase", "💳 Entity created: $newEnvelope");
            return true;
        }catch (ex: Exception){
            Log.e("CreateEnvelopeUseCase", ex.message.toString())
            return false;
        }
    }
}