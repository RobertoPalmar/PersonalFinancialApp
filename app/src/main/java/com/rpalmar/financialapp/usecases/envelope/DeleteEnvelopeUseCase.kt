package com.rpalmar.financialapp.usecases.envelope

import android.util.Log
import com.rpalmar.financialapp.providers.database.repositories.EnvelopeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteEnvelopeUseCase @Inject constructor(
    private val envelopeRepository: EnvelopeRepository
) {

    suspend operator fun invoke(envelopeID: Long): Boolean? {
        try {
            //VALIDATE ENTITY
            var envelopeToDelete = envelopeRepository.getByID(envelopeID);

            //VALIDATE ENVELOPE
            if(envelopeToDelete == null){
                Log.i("DeleteEnvelopeUseCase", "Entity not found");
                return false;
            }

            //DELETE ENTITY
            envelopeRepository.softDelete(envelopeToDelete.id);

            Log.i("DeleteEnvelopeUseCase", "💳 Entity deleted")
            return true;
        }catch (ex: Exception){
            Log.e("DeleteEnvelopeUseCase", ex.message.toString());
            return null;
        }
    }

}