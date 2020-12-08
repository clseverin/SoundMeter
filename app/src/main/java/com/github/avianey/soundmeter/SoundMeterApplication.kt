package com.github.avianey.soundmeter

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.room.Room
import androidx.room.RoomDatabase
import io.reactivex.subjects.BehaviorSubject

class SoundMeterApplication: Application() {

    companion object {
        const val NOTIFICATION_CHANNEL = "service"

        val serviceStateObservable = BehaviorSubject.createDefault(LocationService.State.IDLE)
        lateinit var db: LocationDb
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        db = Room.databaseBuilder(this, LocationDb::class.java, "location-db").build()
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            (getSystemService(NotificationManager::class.java) as NotificationManager)
                .createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL, "channel name", NotificationManager.IMPORTANCE_HIGH))
        }
    }

}