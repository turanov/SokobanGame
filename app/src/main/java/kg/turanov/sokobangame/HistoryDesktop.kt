import android.os.Environment
import kg.turanov.sokobangame.data.Direction
import java.io.*

public class HistoryDesktop {
    private lateinit var desktop: Array<IntArray>
    private var dir: String
    private var firstFileName: String
    private var secondFileName: String
    private var top:Int
    private var queueWriteForFile: Boolean
    private var lastValueOfTop: Int
    private var isNext: Boolean
    private var isPrevious: Boolean
    private var isManDirectionRow: Boolean
    private var manDirection: Direction

    constructor() {
        manDirection = Direction.DOWN
        isManDirectionRow = false
        isNext = false
        isPrevious = false
        top = 0
        lastValueOfTop = top
        queueWriteForFile = false
        this.dir =
            Environment.getExternalStorageDirectory().path + "/Download";
        firstFileName = "history1.txt"
        secondFileName = "history2.txt"
    }

    fun previousValueOfDesktop(): Array<IntArray> {
        isNext = false
        isPrevious = true
        queueWriteForFile = true
        var desk = getCurrentDesktopValue()
        if (top > 1) {
            desk = getFromFileDesktop()
            top--
        } else if (top == 1) {
            desk = getFromFileDesktop()
        }
        return desk
    }

    fun nextValueOfDesktop(): Array<IntArray> {
        isNext = true
        isPrevious = false
        queueWriteForFile = true
        return when {
            top < lastValueOfTop -> {
                top++
                getFromFileDesktop()
            }
            top == lastValueOfTop -> {
                getFromFileDesktop()
            }
            else -> getCurrentDesktopValue()
        }
    }

    private fun setManDirection(direction: String) {
        manDirection = when (direction) {
            Direction.UP.toString() -> Direction.UP
            Direction.RIGHT.toString() -> Direction.RIGHT
            Direction.LEFT.toString() -> Direction.LEFT
            else -> Direction.DOWN
        }
    }

    fun newValueToDesktop(desktop: Array<IntArray>,manDirection: Direction) {
        isNext = false
        if (queueWriteForFile) {
            var temp = firstFileName
            firstFileName = secondFileName
            secondFileName = temp
            queueWriteForFile = true
        }
        this.desktop = desktop
        top++
        lastValueOfTop = top
        this.manDirection = manDirection
        writeValuesFromDesktopToFile(desktop, firstFileName, secondFileName)
    }

    fun getCurrentDesktopValue(): Array<IntArray> {
        return desktop
    }

    fun getDirectionMan(): Direction {
        return manDirection
    }

    private fun getFromFileDesktop(): Array<IntArray> {
        var inputStream: InputStream? = null
        var fileWriter: FileWriter? = null
        var rowIndex = 0

        try {
            inputStream = File("$dir/$firstFileName").inputStream()
            fileWriter = FileWriter("$dir/$secondFileName")
            var isDesktop = false
            var writeToNewFile = true
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach {

                    var columnIndex = 0
                    if (isNext || isPrevious) {
                        if ("!_${top + 1}" == it) {
                            isDesktop = false
                            writeToNewFile = false
                        }
                    } else {
                        if ("!_${top}" == it && !isPrevious) {
                            isDesktop = false
                            writeToNewFile = false
                        }
                    }
                    if (isManDirectionRow) {
                        setManDirection(it)
                        isManDirectionRow = false
                    } else if (isDesktop) {
                        for (symbol in it) {
                            if (symbol in '0'..'9' && rowIndex < desktop.size) {
                                desktop[rowIndex][columnIndex] = symbol.toInt() - 48
                                columnIndex++
                            }
                        }
                        if (columnIndex != 0 && rowIndex < desktop.size) {
                            while (columnIndex < desktop[rowIndex].size) {
                                desktop[rowIndex][columnIndex] = 5
                                columnIndex++
                            }
                            rowIndex++
                        }
                    }
                    if (isNext || (top == 1 && isPrevious)) {
                        if ("!_${top}" == it) {
                            isDesktop = true
                            isManDirectionRow = true
                        }
                    } else {
                        if ("!_${top - 1}" == it) {
                            isDesktop = true
                            isManDirectionRow = true
                        }
                    }
                    if (writeToNewFile) {
                        fileWriter.write(it + "\n")
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
            if (fileWriter != null)
                try {
                    fileWriter.flush()
                    fileWriter.close()
                } catch (e: Exception) {
                    println(e)
                }
        }
        return desktop
    }

    private fun writeValuesFromDesktopToFile(
        desktop: Array<IntArray>,
        firstFile: String,
        secondFile: String
    ) {
        clearFile(secondFile)
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("$dir/$firstFile", true)
            fileWriter.write("!_$top\n")
            fileWriter.write(manDirection.toString() + "\n")
            for (i in desktop) {
                for (j in i) {
                    fileWriter.write(j.toString())
                }
                fileWriter.write("\n")
            }

        } catch (e: FileNotFoundException) {
            println(e)
        } catch (e: IOException) {
            println(e)
        } finally {

            if (fileWriter != null)
                try {
                    fileWriter.close()
                } catch (e: Exception) {
                    println(e)
                }
        }
    }

    fun clearHistories() {
        clearFile(firstFileName)
        clearFile(secondFileName)
        firstFileName = "history1.txt"
        secondFileName = "history2.txt"
        top = 0
        queueWriteForFile = false
    }

    fun clearFile(fileName: String) {
        queueWriteForFile = false
        var fileWriter: FileWriter? = null
        try {
            fileWriter = FileWriter("$dir/$fileName", false)
            fileWriter.write("")

        } catch (e: FileNotFoundException) {
            println(e)
        } catch (e: IOException) {
            println(e)
        } finally {
            if (fileWriter != null)
                try {
                    fileWriter.close()
                } catch (e: Exception) {
                    println(e)
                }
        }

    }

}