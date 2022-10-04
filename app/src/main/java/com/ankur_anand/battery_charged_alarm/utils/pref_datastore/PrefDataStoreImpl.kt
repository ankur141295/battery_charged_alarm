package com.ankur_anand.battery_charged_alarm.utils.pref_datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import java.io.IOException


const val DATASTORE_NAME = "battery_charged_alarm_datastore"

class PrefDataStoreImpl(
    private val context : Context
): PrefDataStore {

    private val Context.myDataStore by preferencesDataStore(DATASTORE_NAME)

    override suspend fun savePercentage(value: String) {
        context.myDataStore.edit {preferences ->
            preferences[PreferencesKeys.PERCENTAGE_SAVED] = value
        }
    }


    override suspend fun getSavedPercentage(): String {
        val prefObj = context.myDataStore.data.catch {throwable ->
            if(throwable is IOException){
                emit(emptyPreferences())
            }else{
                throw throwable
            }
        }.first()

        return prefObj[PreferencesKeys.PERCENTAGE_SAVED] ?: ""
    }


    private object PreferencesKeys {
        val PERCENTAGE_SAVED = stringPreferencesKey("percentage_saved")
    }
}