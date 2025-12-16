package com.rpalmar.financialapp.models

import java.util.Currency
import java.util.Locale

/**
 * Data class representing currency information
 */
data class CurrencyInfo(
    val name: String,
    val isoCode: String,
    val symbol: String
)

/**
 * Helper object for working with currency data
 */
object CurrencyDataHelper {
    
    /**
     * Get all available currencies sorted by name
     */
    fun getAllCurrencies(): List<CurrencyInfo> {
        return Currency.getAvailableCurrencies()
            .mapNotNull { currency ->
                try {
                    CurrencyInfo(
                        name = currency.getDisplayName(Locale.getDefault()),
                        isoCode = currency.currencyCode,
                        symbol = currency.symbol
                    )
                } catch (e: Exception) {
                    // Some currencies might not have all information
                    null
                }
            }
            .sortedBy { it.name }
    }
    
    /**
     * Search currencies by name or ISO code
     */
    fun searchCurrencies(query: String): List<CurrencyInfo> {
        if (query.isBlank()) return getAllCurrencies()
        
        val lowerQuery = query.lowercase()
        return getAllCurrencies().filter {
            it.name.lowercase().contains(lowerQuery) ||
            it.isoCode.lowercase().contains(lowerQuery)
        }
    }
    
    /**
     * Get currency by ISO code
     */
    fun getCurrencyByCode(isoCode: String): CurrencyInfo? {
        return try {
            val currency = Currency.getInstance(isoCode)
            CurrencyInfo(
                name = currency.getDisplayName(Locale.getDefault()),
                isoCode = currency.currencyCode,
                symbol = currency.symbol
            )
        } catch (e: Exception) {
            null
        }
    }
}
