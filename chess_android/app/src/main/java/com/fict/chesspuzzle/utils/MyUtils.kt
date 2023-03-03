package com.fict.chesspuzzle.utils

import androidx.compose.ui.graphics.Color
import com.fict.chesspuzzle.models.BoardModel
import com.fict.chesspuzzle.models.SquareModel
import com.fict.chesspuzzle.ui.theme.lightGreen
import com.fict.chesspuzzle.ui.theme.lightRed
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.File
import com.github.bhlangonijr.chesslib.Rank
import com.github.bhlangonijr.chesslib.Square

object MyUtils {
    // marks the selected square , with red color selectedFrom square, with green color selectedTo square
    fun getSelectionSquareColor(selectedFrom: Boolean, selectedTo: Boolean): Color {
        if (selectedFrom) {
            return lightRed
        } else if (selectedTo) {
            return lightGreen
        }
        return Color.Transparent
    }

    // get the background color of light and dark squares
    fun getBackgroundSquareColor(isLightSquare: Boolean): Color {
        val darkSquare = Color(0xFF779556)
        val lightSquare = Color(0xFFEBECD0)
        return if (isLightSquare) lightSquare else darkSquare
    }

    fun getInitEmptyBoardModel(): BoardModel {
        //get FEN from intent
        val boardLib = Board()
        boardLib.loadFromFen("8/8/8/8/8/8/8/8 w KQkq - 0 0")
        //use the lib to create BoardModel

        val squares: MutableList<SquareModel> = mutableListOf()
        //for y 0 - 7
        //for x 0-7
        for (y in 0..7) {
            for (x in 0..7) {
                val isLightSquare = x % 2 == y % 2
                val s = SquareModel(
                    x, y, getFenSymbol(x,y,boardLib),
                    isSelectedFrom = false,
                    isSelectedTo = false,
                    isValidMove = false,
                    isLightSquare = isLightSquare
                )
                squares.add(s)
            }
        }
        //get proper peace and add it to the init model
        return BoardModel(squares)
    }

    private fun getFenSymbol(x: Int, y: Int, boardLib: Board): String {
        val col = File.allFiles[x] //file is column, example A-File, H-File
        val row = Rank.allRanks[7 - y] //rank is 1st to 8th rank
        val sq = Square.encode(row, col)
        val piece = boardLib.getPiece(sq)
        return piece.fenSymbol
    }
}


// TODO: add conversion from PGN(Nxh3) to Coordinates(A1-B2)
//35. Ra7 g6 36. Ra6+ Kc5 37. Ke1 Nf4 38. g3 Nxh3 39. Kd2 Kb5 40. Rd6 Kc5 41. Ra6
//Ra6 - ova znaci deka treba da go zemish topot i da go mrdnish na A6
//  H8-A6

