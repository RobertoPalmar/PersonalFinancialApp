package com.rpalmar.financialapp.usecases.currency

import android.util.Log
import com.rpalmar.financialapp.models.domain.CurrencyDomain
import com.rpalmar.financialapp.providers.database.repositories.CurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveMainCurrencyUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    operator fun invoke(): Flow<CurrencyDomain?> =
        currencyRepository.getMainCurrencyAsFlow()
            .map { entity ->
                if (entity == null) {
                    Log.e("ObserveMainCurrencyUseCase", "No se encontró moneda base")
                    null
                } else {
                    val domain = entity.toDomain()
                    Log.i(
                        "ObserveMainCurrencyUseCase",
                        "💳 Currency observed: $domain"
                    )
                    domain
                }
            }
            .catch { ex ->
                Log.e(
                    "ObserveMainCurrencyUseCase",
                    ex.message ?: "Error observando moneda base"
                )
                emit(null)
            }
}
