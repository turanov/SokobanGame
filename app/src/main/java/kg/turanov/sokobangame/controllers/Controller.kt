package kg.turanov.sokobangame.controllers

import android.view.*
import kg.turanov.sokobangame.*
import kg.turanov.sokobangame.data.SWIPE_MIN_DISTANCE
import kg.turanov.sokobangame.data.SWIPE_THRESHOLD_VELOCITY
import kg.turanov.sokobangame.data.Direction
import kg.turanov.sokobangame.models.Model
import kg.turanov.sokobangame.Viewer
import kotlin.math.abs

public class Controller : View.OnTouchListener,
    GestureDetector.SimpleOnGestureListener,
    View.OnClickListener {
    private val model: Model
    private val gestureDetector: GestureDetector

    constructor(viewer: Viewer) {
        this.model = Model(viewer)
        gestureDetector = GestureDetector(this)
    }

    fun getModel(): Model {
        return model
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onClick(view: View) {
        var checkTheFirstLevel = true
        when (view.id) {
            R.id.button_next -> model.nextLevel()
            R.id.button_previous -> checkTheFirstLevel = model.previousLevel()
            R.id.button_replay -> model.restartLevel()
            R.id.button_restart -> model.restartGame();
        }
        if (checkTheFirstLevel)
            model.dismissDialog()
    }

    fun initMenu(menu: Menu) {
        model.initMenu(menu)
        model.setLevelListToMenu()
    }

    fun onOptionsItemSelectedHandler(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_back ->{
                model.backDesktop();
            }
            R.id.menu_forward ->{
                model.forwardDesktop();
            }
            R.id.menu_refresh -> {
                model.restartLevel()
            }
            R.id.menu_level -> {
                return
            }
            else -> {
                model.setLevel(item.itemId)
            }
        }
    }


    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val valueX: Float = event2.x - event1.x
        val valueY: Float = event2.y - event1.y

        if (abs(valueX) > abs(valueY)) {
            if (abs(valueX) > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                if (valueX > 0) {
                    swipeRight()
                } else {
                    swipeLeft()
                }
            }
        } else if (abs(valueY) > SWIPE_MIN_DISTANCE && abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            if (valueY > 0) {
                swipeDown()
            } else {
                swipeUp()
            }
        }

        return super.onFling(event1, event2, velocityX, velocityY)
    }


    private fun swipeUp() {
        model.move(Direction.UP)
    }

    private fun swipeDown() {
        model.move(Direction.DOWN)
    }

    private fun swipeRight() {
        model.move(Direction.RIGHT)
    }

    private fun swipeLeft() {
        model.move(Direction.LEFT)
    }


}