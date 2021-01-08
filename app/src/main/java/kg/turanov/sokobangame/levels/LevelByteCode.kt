package kg.turanov.sokobangame.levels

import kg.turanov.sokobangame.Viewer

public class LevelByteCode {
    private var viewer: Viewer
    constructor(viewer: Viewer){
        this.viewer = viewer
    }
    fun level(level: Int): Array<IntArray> {
        return when (level) {
            1 -> getFirstLevel()
            2 -> getSecondLevel()
            3 -> getThirdLevel()
            else -> LevelAdapter.getLevel(
                LevelAdapter.firstLevel,
                viewer
            )
        }
    }

    private fun getFirstLevel(): Array<IntArray> {
        return arrayOf(
            intArrayOf(2, 2, 2, 2, 2, 2, 2),
            intArrayOf(2, 1, 0, 3, 0, 4, 2),
            intArrayOf(2, 2, 2, 2, 2, 2, 2)
        )
    }

    private fun getSecondLevel(): Array<IntArray> {
        return arrayOf(
            intArrayOf(2, 2, 2, 2, 2, 2),
            intArrayOf(2, 0, 0, 0, 0, 2),
            intArrayOf(2, 0, 2, 1, 0, 2),
            intArrayOf(2, 0, 3, 5, 0, 2),
            intArrayOf(2, 0, 4, 5, 0, 2),
            intArrayOf(2, 0, 0, 0, 0, 2),
            intArrayOf(2, 2, 2, 2, 2, 2)
        )
    }

    private fun getThirdLevel(): Array<IntArray> {
        return arrayOf(
            intArrayOf(2, 2, 2, 2, 2, 2),
            intArrayOf(2, 1, 0, 0, 0, 2, 2),
            intArrayOf(2, 0, 3, 3, 0, 0, 2),
            intArrayOf(2, 0, 2, 4, 0, 4, 2),
            intArrayOf(2, 0, 0, 0, 0, 0, 2),
            intArrayOf(2, 2, 2, 2, 2, 2, 2)
        )
    }

}