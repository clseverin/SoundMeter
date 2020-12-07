package com.github.avianey.soundmeter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class SoundMeterActivity: AppCompatActivity() {

    companion object {
        const val POPUP_DISPLAYED = "popupAlreadyDisplayed"
        const val TAG_PERMISSION_FRAGMENT = "permissionDialogFragment"
        const val REQUEST_CODE_PERMISSION = 1
    }

    private var popupAlreadyDisplayed = false

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
    }

    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            if (supportFragmentManager.findFragmentByTag(TAG_PERMISSION_FRAGMENT) == null
                && !popupAlreadyDisplayed) {
                supportFragmentManager
                    .beginTransaction()
                    .add(PermissionPopupFragment(), TAG_PERMISSION_FRAGMENT)
                    .commit()
                popupAlreadyDisplayed = true
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(POPUP_DISPLAYED, popupAlreadyDisplayed)
    }

}