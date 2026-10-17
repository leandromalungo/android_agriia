package com.agriia.app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "agriia_user_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val USER_NAME = stringPreferencesKey("user_name")
        val CURRENT_MODEL = stringPreferencesKey("current_model")
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.USER_NAME] ?: "Leandro"
    }

    val currentModelFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.CURRENT_MODEL] ?: "Default"
    }

    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.USER_NAME] = name
        }
    }

    suspend fun updateCurrentModel(model: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CURRENT_MODEL] = model
        }
    }
}
