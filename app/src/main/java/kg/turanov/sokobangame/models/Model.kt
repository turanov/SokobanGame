package kg.turanov.sokobangame.models

import HistoryDesktop
import android.view.Menu
import kg.turanov.sokobangame.*
import kg.turanov.sokobangame.data.*
import kg.turanov.sokobangame.Viewer
import kg.turanov.sokobangame.levels.LevelAdapter

public class Model {
    private val viewer: Viewer
    private var desktop: Array<IntArray>
    private var curRowInxOfMan: Int
    private var curColInxOfMan: Int
    private var goalIndexes: MutableList<MutableList<Int>>
    private lateinit var menu: Menu
    private val historyDesktop: HistoryDesktop
    private var isNotInitHistory: Boolean
    private var isInitGoalIndexes:Boolean
    private var directionMan: Direction

    constructor(viewer: Viewer) {
        directionMan = Direction.DOWN
        this.isInitGoalIndexes = true
        this.historyDesktop = HistoryDesktop()
        this.viewer = viewer
        this.desktop = LevelAdapter.getLevel(
            LevelAdapter.firstLevel, viewer
        )
        isNotInitHistory = true
        curRowInxOfMan = 0
        curColInxOfMan = 0
        goalIndexes = mutableListOf<MutableList<Int>>()
        setPositionOfManAndInitGoalIndexes()
    }

    fun getDeskTop(): Array<IntArray> {
        return desktop
    }

    private fun setPositionOfManAndInitGoalIndexes(): Unit {
        if (isInitGoalIndexes)
            goalIndexes = mutableListOf<MutableList<Int>>()

        for (i in desktop.indices) {
            for (j in desktop[i].indices) {
                if (desktop[i][j] == DESKTOP_GAMER) {
                    curRowInxOfMan = i
                    curColInxOfMan = j
                }
                if ((desktop[i][j] == DESKTOP_GOAL || desktop[i][j] == DESKTOP_BOX_GOAL) && isInitGoalIndexes) {
                    goalIndexes.add(arrayListOf(i, j))
                }
            }
        }
        isInitGoalIndexes = false
        if (isNotInitHistory) {
            isNotInitHistory = false
            historyDesktop.clearHistories()
            historyDesktop.newValueToDesktop(desktop,directionMan)
        }
    }

    fun initMenu(menu: Menu) {
        this.menu = menu
    }

    fun backDesktop() {
        desktop = historyDesktop.previousValueOfDesktop()
        directionMan = historyDesktop.getDirectionMan()
        desktopUpdate()
    }

    fun forwardDesktop() {
        desktop = historyDesktop.nextValueOfDesktop()
        directionMan = historyDesktop.getDirectionMan()
        desktopUpdate()
    }

    private fun desktopUpdate() {
        viewer.updateMan(directionMan)
        setPositionOfManAndInitGoalIndexes()
        viewer.update()
    }

    fun restartLevel() {
        desktop = LevelAdapter.getLevel(
            LevelAdapter.currentLevel, viewer
        )
        isNotInitHistory = true
        historyDesktop.clearHistories()
        desktopUpdate()
    }

    fun setLevelListToMenu() {
        val subLevelMenu = menu.findItem(R.id.menu_level).subMenu
        subLevelMenu.clear()
        viewer.setTitle("Level ${LevelAdapter.currentLevel}")
        for (i in 1..LevelAdapter.lastLevel) {
            subLevelMenu.add(0, i, Menu.NONE, "level $i")
        }
        subLevelMenu.setGroupCheckable(0, true, true)
    }

    fun setLevel(level: Int) {
        isInitGoalIndexes = true
        desktop = LevelAdapter.getLevel(level, viewer)
        viewer.setTitle("Level ${LevelAdapter.currentLevel}")
        historyDesktop.clearHistories()
        isNotInitHistory = true
        directionMan = Direction.DOWN
        desktopUpdate()
    }

    fun nextLevel() {
        setLevel(LevelAdapter.currentLevel + 1)
    }

    fun previousLevel(): Boolean {
        if (LevelAdapter.currentLevel == 1)
            return false
        setLevel(LevelAdapter.currentLevel - 1)
        return true
    }

    fun restartGame() {
        setLevel(LevelAdapter.firstLevel)
    }

    fun dismissDialog() {
        viewer.dismissDialog()
    }

    fun move(direction: Direction) {
        directionMan = direction
        if (isNextEmpty(direction, curRowInxOfMan, curColInxOfMan)) {
            when (direction) {
                Direction.LEFT -> moveLeft()
                Direction.RIGHT -> moveRight()
                Direction.UP -> moveUp()
                Direction.DOWN -> moveDown()
            }
        } else if (isNextBox(direction)) {
            when (direction) {
                Direction.LEFT -> {
                    if (isNextEmpty(direction, curRowInxOfMan, curColInxOfMan - 1)) {
                        moveBoxLeft(curRowInxOfMan, curColInxOfMan - 1)
                        moveLeft()
                    }
                }
                Direction.RIGHT -> {
                    if (isNextEmpty(direction, curRowInxOfMan, curColInxOfMan + 1)) {
                        moveBoxRight(curRowInxOfMan, curColInxOfMan + 1)
                        moveRight()
                    }
                }
                Direction.UP -> {
                    if (isNextEmpty(direction, curRowInxOfMan - 1, curColInxOfMan)) {
                        moveBoxUp(curRowInxOfMan - 1, curColInxOfMan)
                        moveUp()
                    }
                }
                Direction.DOWN -> {
                    if (isNextEmpty(direction, curRowInxOfMan + 1, curColInxOfMan)) {
                        moveBoxDown(curRowInxOfMan + 1, curColInxOfMan)
                        moveDown()
                    }
                }
            }

        } else {
            desktopUpdate()
            return
        }
        if (isWin()) {
            if (LevelAdapter.currentLevel < LevelAdapter.lastLevel) {
                viewer.dialogNextLevel()
                setPositionOfManAndInitGoalIndexes()
            } else viewer.dialogLastLevel()
        }
        historyDesktop.newValueToDesktop(desktop,direction)
        desktopUpdate()
    }

    private fun isWin(): Boolean {
        for (i in goalIndexes.indices) {
            if (desktop[goalIndexes[i][0]][goalIndexes[i][1]] != DESKTOP_BOX_GOAL)
                return false
        }
        return true
    }

    private fun moveBoxLeft(i: Int, j: Int) {
        if (desktop[i][j - 1] == DESKTOP_GOAL) {
            desktop[i][j - 1] =
                DESKTOP_BOX_GOAL
        } else desktop[i][j - 1] =
            DESKTOP_BOX
    }

    private fun moveBoxRight(i: Int, j: Int) {
        if (desktop[i][j + 1] == DESKTOP_GOAL) {
            desktop[i][j + 1] =
                DESKTOP_BOX_GOAL
        } else
            desktop[i][j + 1] =
                DESKTOP_BOX
    }

    private fun moveBoxUp(i: Int, j: Int) {
        if (desktop[i - 1][j] == DESKTOP_GOAL) {
            desktop[i - 1][j] =
                DESKTOP_BOX_GOAL
        } else
            desktop[i - 1][j] =
                DESKTOP_BOX
    }

    private fun moveBoxDown(i: Int, j: Int) {
        if (desktop[i + 1][j] == DESKTOP_GOAL) {
            desktop[i + 1][j] =
                DESKTOP_BOX_GOAL
        } else
            desktop[i + 1][j] =
                DESKTOP_BOX
    }


    private fun isNextBox(direction: Direction): Boolean {
        when (direction) {
            Direction.LEFT -> {
                if (desktop[curRowInxOfMan][curColInxOfMan - 1] == DESKTOP_BOX
                    || desktop[curRowInxOfMan][curColInxOfMan - 1] == DESKTOP_BOX_GOAL
                ) return true
            }
            Direction.RIGHT -> {
                if (desktop[curRowInxOfMan][curColInxOfMan + 1] == DESKTOP_BOX
                    || desktop[curRowInxOfMan][curColInxOfMan + 1] == DESKTOP_BOX_GOAL
                ) return true
            }
            Direction.UP -> {
                if (desktop[curRowInxOfMan - 1][curColInxOfMan] == DESKTOP_BOX
                    || desktop[curRowInxOfMan - 1][curColInxOfMan] == DESKTOP_BOX_GOAL
                ) return true
            }
            Direction.DOWN -> {
                if (desktop[curRowInxOfMan + 1][curColInxOfMan] == DESKTOP_BOX
                    || desktop[curRowInxOfMan + 1][curColInxOfMan] == DESKTOP_BOX_GOAL
                ) return true
            }
        }
        return false
    }

    private fun isNextEmpty(direction: Direction, rowIndex: Int, colIndex: Int): Boolean {
        when (direction) {
            Direction.LEFT -> {
                if (desktop[rowIndex][colIndex - 1] == DESKTOP_EMPTY || desktop[rowIndex][colIndex - 1] == DESKTOP_GOAL) return true
            }
            Direction.RIGHT -> {
                if (desktop[rowIndex][colIndex + 1] == DESKTOP_EMPTY || desktop[rowIndex][colIndex + 1] == DESKTOP_GOAL) return true
            }
            Direction.UP -> {
                if (desktop[rowIndex - 1][colIndex] == DESKTOP_EMPTY || desktop[rowIndex - 1][colIndex] == DESKTOP_GOAL) return true
            }
            Direction.DOWN -> {
                if (desktop[rowIndex + 1][colIndex] == DESKTOP_EMPTY || desktop[rowIndex + 1][colIndex] == DESKTOP_GOAL) return true
            }
        }
        return false
    }


    private fun moveDown() {
        if (isNextNotGoal(curRowInxOfMan, curColInxOfMan))
            desktop[curRowInxOfMan][curColInxOfMan] =
                DESKTOP_EMPTY
        desktop[++curRowInxOfMan][curColInxOfMan] =
            DESKTOP_GAMER
    }


    private fun moveUp() {
        if (isNextNotGoal(curRowInxOfMan, curColInxOfMan))
            desktop[curRowInxOfMan][curColInxOfMan] =
                DESKTOP_EMPTY
        desktop[--curRowInxOfMan][curColInxOfMan] =
            DESKTOP_GAMER
    }

    private fun moveRight() {
        if (isNextNotGoal(curRowInxOfMan, curColInxOfMan))
            desktop[curRowInxOfMan][curColInxOfMan] =
                DESKTOP_EMPTY
        desktop[curRowInxOfMan][++curColInxOfMan] =
            DESKTOP_GAMER
    }

    private fun moveLeft() {
        if (isNextNotGoal(curRowInxOfMan, curColInxOfMan))
            desktop[curRowInxOfMan][curColInxOfMan] =
                DESKTOP_EMPTY
        desktop[curRowInxOfMan][--curColInxOfMan] =
            DESKTOP_GAMER
    }

    private fun isNextNotGoal(curRowInx: Int, curColInx: Int): Boolean {
        for (i in goalIndexes.indices) {
            if (curRowInx == goalIndexes[i][0] && curColInx == goalIndexes[i][1]) {
                desktop[curRowInx][curColInx] =
                    DESKTOP_GOAL
                return false
            }
        }
        return true
    }


}