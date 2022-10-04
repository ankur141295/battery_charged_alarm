package com.ankur_anand.battery_charged_alarm.application

import android.app.Application
import com.ankur_anand.battery_charged_alarm.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BatteryChargedAlarm : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}