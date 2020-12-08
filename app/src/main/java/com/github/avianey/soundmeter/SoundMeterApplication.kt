package com.github.avianey.soundmeter

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import io.reactivex.subjects.BehaviorSubject

class SoundMeterApplication: Application() {

    companion object {
        const val NOTIFICATION_CHANNEL = "service"

        val serviceStateObservable = BehaviorSubject.createDefault(LocationService.State.IDLE)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            (getSystemService(NotificationManager::class.java) as NotificationManager)
                .createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL, "channel name", NotificationManager.IMPORTANCE_HIGH))
        }
    }

}