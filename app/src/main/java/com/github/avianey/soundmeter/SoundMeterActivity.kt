package com.github.avianey.soundmeter

import android.Manifest
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

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

    private lateinit var coordinatesView: TextView

    private var locationListener = LocationListener { location ->
        coordinatesView.text = getString(R.string.sample_template_string,
                String.format("%.6f", location.latitude),
                String.format("%.6f", location.longitude))
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
        popupAlreadyDisplayed = savedInstanceState?.getBoolean(POPUP_DISPLAYED) ?: false
        coordinatesView = findViewById(R.id.coordinates)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (supportFragmentManager.findFragmentByTag(TAG_PERMISSION_FRAGMENT) == null
                && !popupAlreadyDisplayed) {
                supportFragmentManager
                    .beginTransaction()
                    .add(PermissionPopupFragment(), TAG_PERMISSION_FRAGMENT)
                    .commit()
                popupAlreadyDisplayed = true
            }
        } else {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager?.requestLocationUpdates(
                LOCATION_UPDATE_MS,
                LOCATION_UPDATE_RADIUS,
                Criteria().apply {
                     accuracy = Criteria.ACCURACY_FINE
                }, locationListener, Looper.myLooper())
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager?.removeUpdates(locationListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(POPUP_DISPLAYED, popupAlreadyDisplayed)
    }

}