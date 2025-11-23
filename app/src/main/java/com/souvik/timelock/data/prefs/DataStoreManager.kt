package com.souvik.timelock.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("applimiter_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val KEY_PIN = stringPreferencesKey("user_pin")
    private val KEY_ONBOARDED = booleanPreferencesKey("onboarded")
    private val KEY_SELECTED_APPS = stringSetPreferencesKey("selected_apps")
    private val keyLimit = { pkg: String -> longPreferencesKey("limit_$pkg") }

    // ---------- SELECTED APPS ----------
    val selectedAppsFlow: Flow<Set<String>> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_SELECTED_APPS] ?: emptySet()
        }

    suspend fun addSelectedApp(pkg: String) {
        context.dataStore.edit { prefs ->
            val old = prefs[KEY_SELECTED_APPS] ?: emptySet()
            prefs[KEY_SELECTED_APPS] = old + pkg
        }
    }

    // ---------- PIN ----------
    val pinFlow: Flow<String?> =
        context.dataStore.data.map { it[KEY_PIN] }

    suspend fun savePin(pin: String) {
        context.dataStore.edit { it[KEY_PIN] = pin }
    }

    // ---------- ONBOARDING ----------
    val onboardedFlow: Flow<Boolean> =
        context.dataStore.data.map { it[KEY_ONBOARDED] ?: false }

    suspend fun setOnboarded() {
        context.dataStore.edit { it[KEY_ONBOARDED] = true }
    }

    // ---------- LIMITS ----------
    fun limitFlow(pkg: String): Flow<Long?> =
        context.dataStore.data.map { it[keyLimit(pkg)] }

    suspend fun setLimit(pkg: String, minutes: Long) {
        context.dataStore.edit { prefs ->
            prefs[keyLimit(pkg)] = minutes
        }
    }

    suspend fun clearLimit(pkg: String) {
        context.dataStore.edit { prefs ->
            prefs.remove(keyLimit(pkg))
        }
    }
}
