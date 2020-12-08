package com.github.avianey.soundmeter

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.IBinder
import androidx.core.app.NotificationCompat

class LocationService: Service() {

    companion object {
        fun startOrStop(context: Context) {
            Intent(context, LocationService::class.java).let { intent ->
                if (isRunning) {
                    context.stopService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }

        var isRunning = false
            private set
    }

    private lateinit var locationManager: LocationManager

    // region lifecycle

    override fun onBind(intent: Intent?): IBinder? {
        throw IllegalStateException("Should not be bound")
    }

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //registerReceivers()
        requestLocationUpdates()
        startForeground(42, getPersistentNotification())
        isRunning = true
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        //unregisterReceivers()
        isRunning = false
    }

    // endregion

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
//        locationManager.requestLocationUpdates(
//            SoundMeterActivity.LOCATION_UPDATE_MS,
//            SoundMeterActivity.LOCATION_UPDATE_RADIUS,
//            Criteria().apply {
//                accuracy = Criteria.ACCURACY_FINE
//            }, locationListener, Looper.myLooper()
//        )
    }

    private fun stopLocationUpdates() {
//        locationManager.removeUpdates(locationListener)
    }

    private fun registerReceivers() {

    }

    private fun unregisterReceivers() {

    }

    private fun getPersistentNotification() =
        NotificationCompat.Builder(this, SoundMeterApplication.NOTIFICATION_CHANNEL)
            .setContentTitle(getString(R.string.notification_title))
            .setContentIntent(getActivityPendingIntent())
            .setColorized(true)
            .setColor(resources.getColor(R.color.purple_500))
            .build()

    private fun getLocationPendingIntent(): PendingIntent {
        TODO()
    }

    private fun getActivityPendingIntent() =
        PendingIntent.getActivity(this, 0,
            Intent(this, SoundMeterActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT)

}