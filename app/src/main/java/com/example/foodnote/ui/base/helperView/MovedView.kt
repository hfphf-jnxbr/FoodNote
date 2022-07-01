package com.example.foodnote.ui.base.helperView

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

@SuppressLint("ClickableViewAccessibility")
class MovedView(private val listView : ArrayList<View>, root : ConstraintLayout) : View.OnTouchListener , MovedViewInterface{

    init {
        root.setOnTouchListener(this)
        listView.forEach { view ->  view.elevation = 15f }
    }

    private var dx = 0f
    private var dy = 0f
    private var flag = true
    private var flagBlock = true
    private val listElevation = ArrayList<View>()

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event != null && flagBlock) {

            when (event.action) {
                0 -> focusView(event)
                2 -> movedView(event)
                1 -> deleteFocusView()
            }
        }
        return true
    }

    private fun movedView(event: MotionEvent) {
        listView.forEach { view ->

            if (view.isPressed) {
                if (flag) {
                    dx = event.x - view.x
                    dy = event.y - view.y

                    flag = false
                }
                view.x = event.x - dx
                view.y = event.y - dy
            }
        }
    }

    private fun focusView(event: MotionEvent) {
        listView.forEach { view ->
            if((event.x > view.x && event.x < view.x + view.width)
                &&(event.y > view.y && event.y < view.y + view.height)) {

                listElevation.add(view)
            }
        }

        if(listElevation.isNotEmpty()) {
            var maxElevationView = listElevation[0]

            listElevation.forEach { view ->
                if (view.elevation > maxElevationView.elevation) {
                    maxElevationView = view
                }
            }

            maxElevationView.isPressed = true
            maxElevationView.elevation = 60f
        }

        listElevation.clear()
    }

    private fun deleteFocusView() {
        listView.forEach { view ->

            view.apply {
                if(isPressed) {
                    isPressed = false
                    elevation = 16f
                } else {
                    elevation = 15f
                }
            }
        }
        flag = true
    }

    override fun addView(view: View){
        listView.add(view)
    }

    override fun removeView(view: View) {
        listView.remove(view)
    }

    override fun blockMove(flag: Boolean) {
        flagBlock = flag
    }
}

