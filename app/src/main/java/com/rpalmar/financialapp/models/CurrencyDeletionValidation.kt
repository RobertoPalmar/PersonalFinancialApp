package com.rpalmar.financialapp.models

/**
 * Represents the result of currency deletion validation
 */
sealed class CurrencyDeletionValidation {
    /**
     * Currency can be safely deleted
     */
    object CanDelete : CurrencyDeletionValidation()
    
    /**
     * Currency has associated accounts and cannot be deleted
     * @param count Number of accounts using this currency
     */
    data class HasAccounts(val count: Int) : CurrencyDeletionValidation()
    
    /**
     * This is the last active currency and cannot be deleted
     */
    object LastActiveCurrency : CurrencyDeletionValidation()
    
    /**
     * This is the main currency - will auto-assign another as main
     */
    object IsMainCurrency : CurrencyDeletionValidation()
}
