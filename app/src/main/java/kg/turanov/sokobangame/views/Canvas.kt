package kg.turanov.sokobangame.views

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import kg.turanov.sokobangame.*
import kg.turanov.sokobangame.data.*
import kg.turanov.sokobangame.data.Direction
import kg.turanov.sokobangame.models.Model

public class Canvas : View {
    private val viewer: Viewer
    private val model: Model
    private val paint: Paint
    private val goal: Drawable
    private var man: Drawable
    private val empty: Drawable
    private val wall: Drawable
    private val box: Drawable
    private val boxGoal: Drawable
    private var desktop: Array<IntArray>
    private var x: Int
    private var y: Int

    constructor(viewer: Viewer, model: Model) : super(viewer) {
        this.viewer = viewer
        this.model = model
        this.paint = Paint()
        setBackgroundColor(R.drawable.background)
        box = this.viewer.getDrawable(R.drawable.box)!!
        wall = this.viewer.getDrawable(R.drawable.wall)!!
        goal = this.viewer.getDrawable(R.drawable.goal)!!
        empty = this.viewer.getDrawable(R.drawable.empty)!!
        boxGoal = this.viewer.getDrawable(R.drawable.box_goal)!!
        man = this.viewer.getDrawable(R.drawable.man_down)!!
        desktop = this.model.getDeskTop()
        x = 0
        y = 0
    }
    override fun onDraw(canvas: Canvas) {
        desktop = model.getDeskTop()
        var maxWidth = 0
        var maxHeight = desktop.size
        for (row in desktop)
            if (row.size>maxWidth) maxWidth = row.size
        var lenMaxDesktop = if (maxWidth > maxHeight) maxWidth else maxHeight
        var sizeBox = (width - width / lenMaxDesktop) / lenMaxDesktop
        var xStart = width/2-(maxWidth*sizeBox)/2
        var yStart = height/2-(maxHeight*sizeBox)/2
        y = yStart
        for (row in desktop) {
            x = xStart
            for (index in row) {
                when (index) {
                    DESKTOP_GAMER -> {
                        man.setBounds(x, y, (x + sizeBox).toInt(), y + sizeBox)
                        man.draw(canvas)
                    }
                    DESKTOP_BOX -> {
                        box.setBounds(x, y, x + sizeBox, y + sizeBox)
                        box.draw(canvas)
                    }
                    DESKTOP_GOAL -> {
                        goal.setBounds(x, y, x + sizeBox, y + sizeBox)
                        goal.draw(canvas)
                    }
                    DESKTOP_EMPTY -> {
                        empty.setBounds(x, y, x + sizeBox, y + sizeBox)
                        empty.draw(canvas)
                    }
                    DESKTOP_WALL -> {
                        wall.setBounds(x, y, x + sizeBox, y + sizeBox)
                        wall.draw(canvas)
                    }
                    DESKTOP_BOX_GOAL -> {
                        boxGoal.setBounds(x, y, x + sizeBox, y + sizeBox)
                        boxGoal.draw(canvas)
                    }
                }
                x+= sizeBox
            }
            y += sizeBox
        }
    }

    fun updateMan(direction: Direction) {
        man = when (direction) {
            Direction.LEFT -> viewer.getDrawable(
                R.drawable.man_left
            )!!
            Direction.RIGHT -> viewer.getDrawable(
                R.drawable.man_right
            )!!
            Direction.UP -> viewer.getDrawable(
                R.drawable.man_up
            )!!
            Direction.DOWN -> viewer.getDrawable(
                R.drawable.man_down
            )!!
        }
    }

    fun update() {
        invalidate()
    }


}