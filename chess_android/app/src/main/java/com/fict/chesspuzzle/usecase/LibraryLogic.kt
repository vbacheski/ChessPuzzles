package com.fict.chesspuzzle.usecase


import com.fict.chesspuzzle.models.BoardModel
import com.github.bhlangonijr.chesslib.*

class LibraryLogic {
    val boardLib = Board()

    fun doLibraryMove(selectedTo: Boolean,board: BoardModel) {
        if (selectedTo) {
            val from = board.getSelectedFrom()
            val to = board.getSelectedTo()
            boardLib.doMove(from+to)
        }
    }
}