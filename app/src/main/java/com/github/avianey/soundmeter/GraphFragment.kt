package com.github.avianey.soundmeter

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class GraphFragment: Fragment() {

    private lateinit var graphDrawable: GraphDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        graphDrawable = GraphDrawable()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return ImageView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view as ImageView
        view.setBackgroundColor(354321)

        view.setImageDrawable(graphDrawable)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
    }


    inner class GraphDrawable: Drawable() {

        override fun draw(canvas: Canvas) {
            canvas.drawRect(bounds, Paint().apply {
                color = resources.getColor(R.color.purple_500)
                style = Paint.Style.FILL
            })
        }

        override fun setBounds(bounds: Rect) {
            this.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
        }

        override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
            super.setBounds(left, top, right, bottom)
        }

        override fun setAlpha(alpha: Int) {}

        override fun setColorFilter(colorFilter: ColorFilter?) {}

        override fun getOpacity() = PixelFormat.UNKNOWN

    }


}