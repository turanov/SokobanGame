package kg.turanov.sokobangame.levels

import android.widget.Toast
import kg.turanov.sokobangame.ConnectToServer
import kg.turanov.sokobangame.data.DESKTOP_SPACE
import kg.turanov.sokobangame.Viewer

public class LevelServer {
    private var viewer: Viewer

    constructor(viewer: Viewer) {
        this.viewer = viewer
    }

    fun level(level: Int): Array<IntArray> {
        val connectToServer =
            ConnectToServer(level.toString())
        connectToServer.go()
        val letter = connectToServer.getLetter()
        if (letter.isEmpty()) {
            Toast.makeText(viewer,"Error connecting to the server", Toast.LENGTH_SHORT).show()
            return LevelAdapter.getLevel(
                LevelAdapter.firstLevel,
                viewer
            )
        }
        return letterToArray(letter)
    }

    private fun letterToArray(letter: String): Array<IntArray> {
        var row = 0;
        var column = 0
        var columnCntMax=0
        for (symbol in letter) {
            if (symbol=='\n'){
                row++;
                if (columnCntMax>column)column=columnCntMax
                columnCntMax=0
            }else{
                columnCntMax++;
            }
        }
        if (columnCntMax>column)column=columnCntMax
        val array = Array(row) { IntArray(column) }
        row = 0
        column = 0
        for (symbol in letter) {
            if (symbol == '\n') {
                while (column<array[row].size) {
                     array[row][column++] =
                         DESKTOP_SPACE
                }
                row++
                column=0
            } else {
                array[row][column] = Character.getNumericValue(symbol)
                column++
            }
        }

        return array
    }


}