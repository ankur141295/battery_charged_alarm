package com.ankur_anand.battery_charged_alarm.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.ankur_anand.battery_charged_alarm.BuildConfig
import com.ankur_anand.battery_charged_alarm.R
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BatteryChargedAlarm : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channels = getNotificationChannels()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(channels)
    }

    private fun getNotificationChannels(): List<NotificationChannel> {
        val generalChannel = NotificationChannel(
            applicationContext.getString(R.string.general_channel_id),
            applicationContext.getString(R.string.general_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = applicationContext.getString(R.string.general_channel_description)
        }

        val alertChannel = NotificationChannel(
            applicationContext.getString(R.string.alert_channel_id),
            applicationContext.getString(R.string.alert_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = applicationContext.getString(R.string.alert_channel_description)
        }

        return listOf(generalChannel, alertChannel)
    }
}