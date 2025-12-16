package com.rpalmar.financialapp.providers.database.repositories

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SharedPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val LAST_SYNC_TIMESTAMP_KEY = "last_sync_timestamp"
        
        // Warning dialog preferences
        const val WARNING_CURRENCY_HAS_ACCOUNTS = "warning_currency_has_accounts"
        const val WARNING_LAST_ACTIVE_CURRENCY = "warning_last_active_currency"
        const val WARNING_DELETING_MAIN_CURRENCY = "warning_deleting_main_currency"
        const val WARNING_DESELECTING_MAIN_CURRENCY = "warning_deselecting_main_currency"
    }

    fun getLastSyncTimestamp(): Long {
        return sharedPreferences.getLong(LAST_SYNC_TIMESTAMP_KEY, 0)
    }

    fun updateLastSyncTimestamp(timestamp: Long) {
        sharedPreferences.edit { putLong(LAST_SYNC_TIMESTAMP_KEY, timestamp) }
    }
    
    /**
     * Check if a warning should be shown
     * @param warningKey The warning preference key
     * @return true if warning should be shown, false otherwise
     */
    fun shouldShowWarning(warningKey: String): Boolean {
        return sharedPreferences.getBoolean(warningKey, true)
    }
    
    /**
     * Set warning preference
     * @param warningKey The warning preference key
     * @param show true to show warning, false to hide
     */
    fun setWarningPreference(warningKey: String, show: Boolean) {
        sharedPreferences.edit { putBoolean(warningKey, show) }
    }
}
