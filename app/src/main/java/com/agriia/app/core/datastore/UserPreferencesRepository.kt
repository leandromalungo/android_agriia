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
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHOTO_URL = stringPreferencesKey("user_photo_url")
        val FARM_NAME = stringPreferencesKey("farm_name")
        val CURRENT_MODEL = stringPreferencesKey("current_model")
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.USER_NAME] ?: "Leandro"
    }

    val userEmailFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.USER_EMAIL] ?: "plamaap1@gmail.com"
    }

    val userPhotoUrlFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.USER_PHOTO_URL] ?: ""
    }

    val farmNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.FARM_NAME] ?: "Fazenda Santa Clara"
    }

    val currentModelFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[Keys.CURRENT_MODEL] ?: "Default"
    }

    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.USER_NAME] = name
        }
    }

    suspend fun updateUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.USER_EMAIL] = email
        }
    }

    suspend fun updateUserPhotoUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.USER_PHOTO_URL] = url
        }
    }

    suspend fun updateFarmName(farm: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.FARM_NAME] = farm
        }
    }

    suspend fun updateCurrentModel(model: String) {
        context.dataStore.edit { preferences ->
            preferences[Keys.CURRENT_MODEL] = model
        }
    }
}
