package com.github.avianey.soundmeter

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager

class SoundMeterActivity: AppCompatActivity() {

    companion object {
        const val POPUP_DISPLAYED = "popupAlreadyDisplayed"
        const val TAG_PERMISSION_FRAGMENT = "permissionDialogFragment"
        const val REQUEST_CODE_PERMISSION = 1
        const val LOCATION_UPDATE_MS = 10_000L
        const val LOCATION_UPDATE_RADIUS = 100f
    }

    private var popupAlreadyDisplayed = false
    private var locationManager: LocationManager? = null

    private lateinit var startStopBtn: Button
    private lateinit var coordinatesView: TextView
    private lateinit var speedView: TextView

    private var locationListener = LocationListener { location ->
        coordinatesView.text = getString(R.string.location_template,
            String.format("%.6f", location.latitude),
            String.format("%.6f", location.longitude))

        speedView.text = getString(R.string.speed_template,
            String.format("%.2f", location.speed ?: 0))

        /*
        PreferenceManager.getDefaultSharedPreferences(this).edit()
            .putInt(SoundMeterSettings.SETTING_SPEED, 60)
            .apply()
         */

        val threshold =
            PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(SoundMeterSettings.SETTING_SPEED, resources.getInteger(R.integer.speed_default))
        if (location.speed > threshold) {
            // threshold reached
            (getSystemService(VIBRATOR_SERVICE) as Vibrator).apply {
                vibrate(500)
            }
            Toast.makeText(this@SoundMeterActivity, "vibrate", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_meter)
        /*
        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(ImageView(this@SoundMeterActivity).apply {
                setImageResource(R.mipmap.ic_launcher)
            })
        })*/
        findViewById<Button>(R.id.open_settings).setOnClickListener {
            startActivity(Intent(this, SoundMeterSettings::class.java))
        }
        startStopBtn = findViewById<Button>(R.id.start)
        startStopBtn.setOnClickListener {
            startOrStop()
        }
        popupAlreadyDisplayed = savedInstanceState?.getBoolean(POPUP_DISPLAYED) ?: false
        coordinatesView = findViewById(R.id.coordinates)
        speedView = findViewById(R.id.speed)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            if (supportFragmentManager.findFragmentByTag(TAG_PERMISSION_FRAGMENT) == null
                && !popupAlreadyDisplayed) {
                supportFragmentManager
                    .beginTransaction()
                    .add(PermissionPopupFragment(), TAG_PERMISSION_FRAGMENT)
                    .commit()
                popupAlreadyDisplayed = true
            }
        }
        syncUI()
        startStopBtn.isEnabled =
            ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED
    }

    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates(locationListener)
        locationManager = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(POPUP_DISPLAYED, popupAlreadyDisplayed)
    }


    @SuppressLint("MissingPermission")
    private fun startOrStop() {
        if (locationManager == null) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager?.requestLocationUpdates(
                LOCATION_UPDATE_MS,
                LOCATION_UPDATE_RADIUS,
                Criteria().apply {
                    accuracy = Criteria.ACCURACY_FINE
                }, locationListener, Looper.myLooper()
            )
        } else {
            locationManager?.removeUpdates(locationListener)
            locationManager = null
        }
        syncUI()
    }

    private fun syncUI() {
        if (locationManager == null) {
            startStopBtn.text = getString(R.string.btn_start)
        } else {
            startStopBtn.text = getString(R.string.btn_stop)
        }
    }

}