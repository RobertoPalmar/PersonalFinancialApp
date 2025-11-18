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
    }

    fun getLastSyncTimestamp(): Long {
        return sharedPreferences.getLong(LAST_SYNC_TIMESTAMP_KEY, 0)
    }

    fun updateLastSyncTimestamp(timestamp: Long) {
        sharedPreferences.edit { putLong(LAST_SYNC_TIMESTAMP_KEY, timestamp) }
    }
}
