package com.shinythinking.applepulser_android.data.local.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class UserSessionLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserSessionLocalDataSource {
    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val ROOM_ID = stringPreferencesKey("room_id")
        val IS_HOST = booleanPreferencesKey("is_host")
        val PLAYER_TYPE = stringPreferencesKey("player_type")
    }

    companion object {
        private const val TAG = "UserSessionLocalDataSourceImpl"
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val ROOM_ID_KEY = stringPreferencesKey("room_id")
        val IS_HOST_KEY = booleanPreferencesKey("is_host")
        val PLAYER_TYPE_KEY = stringPreferencesKey("player_type")
    }

    override fun getPreferences(): Flow<Preferences> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.e(TAG, "IOException: ${exception.message}")
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
    }

    override suspend fun saveString(key: Preferences.Key<String>, value: String) {
        try {
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error saving string preference: $key")
        }
    }

    override suspend fun saveBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        try {
            dataStore.edit { preferences ->
                preferences[key] = value
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error saving boolean preference: $key")
        }
    }

    override suspend fun clear() {
        try {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error clearing preferences")
        }
    }
}