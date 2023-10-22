package com.example.weather.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.stringPreferencesKey

val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveLocation(location: String) {
        dataStore.edit { preferences ->
            preferences[LOCATION_KEY] = location
        }
    }

    val locationFlow = dataStore.data.map { preferences ->
        preferences[LOCATION_KEY] ?: "London"
    }

    companion object {
        private val LOCATION_KEY = stringPreferencesKey("location_key")
    }
}