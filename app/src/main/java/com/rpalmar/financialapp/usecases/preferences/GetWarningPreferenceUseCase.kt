package com.rpalmar.financialapp.usecases.preferences

import com.rpalmar.financialapp.providers.database.repositories.SharedPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetWarningPreferenceUseCase @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {
    operator fun invoke(key: String): Boolean {
        return sharedPreferencesRepository.shouldShowWarning(key)
    }
}
