package kg.turanov.sokobangame.levels

import kg.turanov.sokobangame.Viewer

public class LevelAdapter {
    companion object {
        val firstLevel = 1
        var lastLevel = 9
        var currentLevel = 1
        fun getLevel(level: Int, viewer: Viewer): Array<IntArray> {
            currentLevel = level
            return when (level) {
                in 1..3 -> LevelByteCode(viewer)
                    .level(level)
                in 4..6 -> LevelFile(
                    viewer
                ).level(level)
                in 7..9 -> LevelServer(
                    viewer
                ).level(level)
                else -> getLevel(
                    firstLevel,
                    viewer
                )
            }
        }
    }
}