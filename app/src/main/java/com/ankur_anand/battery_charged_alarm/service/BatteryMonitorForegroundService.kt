package com.ankur_anand.battery_charged_alarm.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.ankur_anand.battery_charged_alarm.BuildConfig
import com.ankur_anand.battery_charged_alarm.utils.getRandomId
import com.ankur_anand.battery_charged_alarm.utils.notification.AppNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class BatteryMonitorForegroundService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    companion object {
        const val ACTION_STOP = "${BuildConfig.APPLICATION_ID}.stop"
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent?.action != null && intent.action.equals(ACTION_STOP, ignoreCase = true)) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
        }

        setMonitorService()
        setNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun setMonitorService() {
        Timber.e("Service started")

        scope.launch {
            delay(20000L)
            Timber.e("Service still running after delay")
        }
    }

    private fun setNotification() {
        val id = getRandomId()
        val notification = AppNotification.generalForegroundNotification(this)

        startForeground(id, notification.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}