package com.fict.chesspuzzle.models

import com.github.bhlangonijr.chesslib.move.Move


/**
 * Pure business logic of our model.
 */
data class BoardModel(
    var squares: List<SquareModel> = listOf()
) {

    private fun translateBoardToFEN(board: Array<Array<String>>): String? {
        val rankSeparator = "/"
        var fen: String? = ""
        for (rank in board.indices) {
            // count empty fields
            var empty = 0
            // empty string for each rank
            var rankFen = ""
            for (file in 0 until board[rank].size) {
                if (board[rank][file].contains(".")) {
                    empty++
                } else {
                    // add the number to the fen if not zero.
                    if (empty != 0) rankFen += empty
                    // add the letter to the fen
                    rankFen += board[rank][file]
                    // reset the empty
                    empty = 0
                }
            }
            // add the number to the fen if not zero.
            if (empty != 0) rankFen += empty
            // add the rank to the fen
            fen += rankFen
            // add rank separator. If last then add a space
            fen += if (rank != board.size - 1) {
                rankSeparator
            } else {
                " "
            }
        }
        return fen
    }


    fun getFenFromBoard(boardModel: BoardModel): String {
        val boardFen = arrayListOf<String>()

        for (pos in boardModel.squares.iterator()){
            boardFen.add(pos.figureType)
        }

        val boardFenRow = boardFen.toTypedArray()

        val fenArray = arrayOf(
            boardFenRow.sliceArray(0..7),
            boardFenRow.sliceArray(8..15),
            boardFenRow.sliceArray(16..23),
            boardFenRow.sliceArray(24..31),
            boardFenRow.sliceArray(32..39),
            boardFenRow.sliceArray(40..47),
            boardFenRow.sliceArray(48..55),
            boardFenRow.sliceArray(56..63)
        )

        val fen = translateBoardToFEN(fenArray)
        println(fen)
        return fen!!
    }

    // get the coordinates of the selected square
    fun get(x: Int, y: Int): SquareModel {
        //if (checkForIndexOutOfBounds(y * 8 + x)) {
        return squares[y * 8 + x]
        //}
        //return getInitSquareModel()
    }

    // get the index of the selected square
    fun get(index: Int): SquareModel {
        //if (checkForIndexOutOfBounds(index)) {
        return squares[index]
        //}
        //return getInitSquareModel()
    }

    fun hasItem(x: Int, y: Int): Boolean {
        if (squares.get(y * 8 + x).figureType == ".") {
            return false
        }
        return true
    }

    // checks if the square we select is a square that contains a figure that we want to move
    fun isSomeFieldSelectedAsFrom(): Boolean {
        for (i in 0..(squares.size - 1)) {
            if (squares.get(i).isSelectedFrom) {
                return true
            }
        }
        return false
    }

    fun isSomeFieldSelectedAsTo(): Boolean {
        for (i in 0..(squares.size - 1)) {
            if (squares.get(i).isSelectedTo) {
                return true
            }
        }
        return false
    }


    fun getSelectedFromSquare(): SquareModel? {
        for (i in 0..(squares.size - 1)) {
            if (squares.get(i).isSelectedFrom) {
                return squares.get(i)
            }
        }
        return null
    }

    fun getSelectedFromSquareIndex(): Int? {
        for (i in 0..(squares.size - 1)) {
            if (squares.get(i).isSelectedFrom) {
                return i
            }
        }
        return null
    }

    fun getSelectedFrom(): String {
        val file = arrayListOf("a", "b", "c", "d", "e", "f", "g", "h")
        val rank = arrayListOf("8", "7", "6", "5", "4", "3", "2", "1")
        var getFrom = String()
        for (x in 0..7) {
            for (y in 0..7) {
                if (squares[y * 8 + x].isSelectedFrom) {
                    getFrom = file[x] + rank[y]
                }
            }
        }
        return getFrom
    }

    fun getSelectedTo(): String {
        val file = arrayListOf("a", "b", "c", "d", "e", "f", "g", "h")
        val rank = arrayListOf("8", "7", "6", "5", "4", "3", "2", "1")
        for (x in 0..7) {
            for (y in 0..7) {
                if (squares[y * 8 + x].isSelectedTo) {
                    return file[x] + rank[y]
                }
            }
        }
        return getSelectedFrom()
    }

    fun getLegalMoves(legalMoves: List<Move>): MutableList<String> {
        //List of valid moves
        val moves = mutableListOf<String>()
        //Gets and loops valid moves from BoardComposeActivity -> onSquareClicked(x: Int, y: Int,legalMoves: List<Move>)
        //and adds the valid moves to moves MutableList
        for (legalMove in legalMoves) {
            moves.add(legalMove.toString())
        }
        return moves
    }

}