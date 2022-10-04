package com.ankur_anand.battery_charged_alarm.utils.pref_datastore

interface PrefDataStore {

    suspend fun savePercentage(value : String)

    suspend fun getSavedPercentage(): String

}