package com.ankur_anand.battery_charged_alarm.utils.notification

import android.app.NotificationManager
import android.content.Context
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import com.ankur_anand.battery_charged_alarm.R
import com.ankur_anand.battery_charged_alarm.utils.getRandomId

object AppNotification {

    fun createNotification(
        context: Context,
        title: String,
        content: String,
        @StringRes channelId: Int,
    ) {
        val id = getRandomId()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, context.getString(channelId))
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.round_battery_charging_full_24)
            .build()

        notificationManager.notify(id, notification)
    }

    fun generalForegroundNotification(
        context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, context.getString(R.string.general_channel_id))
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.battery_charged_alarm_is_running))
            .setSmallIcon(R.drawable.round_battery_charging_full_24)
    }
}

