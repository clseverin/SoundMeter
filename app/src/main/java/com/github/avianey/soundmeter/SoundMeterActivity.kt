package com.github.avianey.soundmeter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SoundMeterActivity: AppCompatActivity() {

    private var popupDisplayed = false

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
        if (savedInstanceState?.getBoolean("popupAlreadyDisplayed") != true) {
            // afficher popup

            popupDisplayed = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("popupAlreadyDisplayed", popupDisplayed)
    }

}