package com.shinythinking.applepulser_android.data.local.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface UserSessionLocalDataSource {
    fun getPreferences(): Flow<Preferences>
    suspend fun saveString(key: Preferences.Key<String>, value: String)
    suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean)
    suspend fun clear()
}