package com.passportv3locity.applocal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppLocalStore(val context: Context) : Application() {
    val PREFERENCE_NAME = "MyDataStore"

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

    suspend fun saveEmail(email: String) {

        try {
            context.dataStore.edit { mutablePreferences ->
                mutablePreferences[EMAIL_PREFERENCE] = email
            }
        } catch (e: Exception) {
            // Handle any exceptions here
            // For example, log the error or perform error handling
            e.printStackTrace()
            Log.d(
                "TAG", "saveEmail: " + e.printStackTrace()
            )
        }
    }

    suspend fun getEmail(): String? {
        val preferences = context.dataStore.data.first()
        return preferences[EMAIL_PREFERENCE]
    }

    companion object {
        var mLocalStore: AppLocalStore? = null

        val EMAIL_PREFERENCE = stringPreferencesKey("email")

        @JvmStatic
        fun getInstance(context: Context): AppLocalStore? {
            if (mLocalStore == null) {
                mLocalStore = AppLocalStore(context)
            }
            return mLocalStore
        }
    }
}