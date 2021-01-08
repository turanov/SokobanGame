package kg.turanov.sokobangame.levels

import kg.turanov.sokobangame.data.DESKTOP_SPACE
import kg.turanov.sokobangame.Viewer
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import kotlin.math.max

public class LevelFile {
    private val viewer: Viewer

    constructor(viewer: Viewer) {
        this.viewer = viewer
    }

    fun level(level: Int): Array<IntArray> {
        val fileName = getFileName(level)
        val sizeOfDesktop = getSizeOfDesktopFromFile(fileName).split(' ')
        val row = sizeOfDesktop[0].toInt()
        val column = sizeOfDesktop[1].toInt()
        return getValuesOfDesktopFromFile(fileName, row, column)

    }

    private fun getFileName(level: Int): String {
        return when (level) {
            4 -> "level4.txt"
            5 -> "level5.txt"
            else -> "level6.txt"
        }
    }

    private fun getValuesOfDesktopFromFile(
        fileName: String,
        row: Int,
        column: Int
    ): Array<IntArray> {
        var inputStream: InputStream? = null
        var roxIndex = 0
        val desktop = Array(row) { IntArray(column) }
        try {

            inputStream = viewer.assets.open("levels/$fileName")

            inputStream.bufferedReader().useLines { lines ->
                lines.forEach {
                    var columnIndex = 0
                    for (symbol in it) {
                        if (symbol in '0'..'9') {
                            desktop[roxIndex][columnIndex] = symbol.toInt() - 48
                            columnIndex++
                        }
                    }
                    if (columnIndex != 0) {
                        while (columnIndex < desktop[roxIndex].size) {
                            desktop[roxIndex][columnIndex] =
                                DESKTOP_SPACE
                            columnIndex++
                        }
                        roxIndex++
                    }

                }
            }
        } catch (e: FileNotFoundException) {
            println(e)
        } catch (e: IOException) {
            println(e)
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close()
                } catch (e: Exception) {
                    println(e)
                }
        }
        return desktop
    }

    private fun getSizeOfDesktopFromFile(fileName: String): String {
        var inputStream: InputStream? = null
        var counterRow = 0
        var maxColumnSize = 0
        try {
            inputStream = viewer.assets.open("levels/$fileName")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach {
                    var counterColumn = 0
                    for (symbol in it) {
                        if (symbol in '0'..'9')
                            counterColumn++
                    }
                    if (counterColumn != 0) {
                        counterRow++
                        maxColumnSize = max(counterColumn, maxColumnSize)
                    }
                }
            }

        } catch (e: FileNotFoundException) {
            println(e)
        } catch (e: IOException) {
            println(e)
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close()
                } catch (e: Exception) {
                    println(e)
                }
        }
        return "$counterRow $maxColumnSize"
    }


}
