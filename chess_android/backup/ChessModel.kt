package old

import android.util.Log
import java.lang.Math.abs

object ChessModel {
    var piecesBox = mutableSetOf<ChessPiece>()

    init {
//        reset()


        Log.d(TAG, toString())
        Log.d(TAG, " ${piecesBox.size}")
    }

    fun canMove(from: Position, to: Position): Boolean {
        if (from.col == to.col && from.row == to.row) {
            return false
        }
        val movingPiece = pieceAt(from) ?: return false
        return when (movingPiece.figure) {
            ChessFigure.KNIGHT -> canKnightMove(from, to)
            ChessFigure.ROOK -> canRookMove(from, to)
            ChessFigure.BISHOP -> canBishopMove(from, to)
            ChessFigure.QUEEN -> canQueenMove(from, to)
            ChessFigure.KING -> canKingMove(from, to)
            ChessFigure.PAWN -> canPawnMove(from, to)
        }
    }

    fun moveFigure(from: Position, to: Position) {
        if (canMove(from, to)) {
            moveFigure(from.col, from.row, to.col, to.row)
        }
    }


    private fun moveFigure(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingFigure = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingFigure.player) {
                return
            }
            piecesBox.remove(it)
        }

        piecesBox.remove(movingFigure)
        piecesBox.add(
            ChessPiece(
                toCol,
                toRow,
                movingFigure.player,
                movingFigure.figure,
                movingFigure.resourceID
            )
        )

    }

    fun pieceAt(square: Position): ChessPiece? {
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col: Int, row: Int): ChessPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }


    private fun canKnightMove(from: Position, to: Position): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    private fun canBishopMove(from: Position, to: Position): Boolean {
        if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    private fun canRookMove(from: Position, to: Position): Boolean {
        if (from.col == to.col && isClearVerticallyBetween(from, to) ||
            from.row == to.row && isClearHorizontallyBetween(from, to)
        ) {
            return true
        }
        return false
    }

    private fun canQueenMove(from: Position, to: Position): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    private fun canKingMove(from: Position, to: Position): Boolean {
        if (canQueenMove(from, to)) {
            val deltaCol = abs(from.col - to.col)
            val deltaRow = abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        return false
    }

    private fun canPawnMove(from: Position, to: Position): Boolean {
        if (from.col == to.col) {
            if (from.row == 1) {
                return to.row == 2 || to.row == 3
            } else if (from.row == 6) {
                return to.row == 5 || to.row == 4
            }
        }
        if (from.col == to.col && isClearVerticallyBetween(from, to)) {
            val deltaCol = abs(from.col - to.col)
            val deltaRow = abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
        }
        if (abs(from.col - to.col) == abs(from.row - to.row) && isClearDiagonally(from, to)) {
            val deltaCol = abs(from.col - to.col)
            val deltaRow = abs(from.row - to.row)
            return deltaCol == 1 && deltaRow == 1 && isClearDiagonally(from, to)
        }
        return false
    }

    private fun isClearVerticallyBetween(from: Position, to: Position): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(Position(from.col, nextRow)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearHorizontallyBetween(from: Position, to: Position): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(Position(nextCol, from.row)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonally(from: Position, to: Position): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(nextCol, nextRow) != null) {
                return false
            }
        }
        return true
    }

//    fun reset() {
//        piecesBox.removeAll(piecesBox)
//        for (i in 0..1) {
//            piecesBox.add(
//                ChessPiece(
//                    0 + i * 7,
//                    0,
//                    ChessPlayer.WHITE,
//                    ChessFigure.ROOK,
//                    R.drawable.white_rook
//                )
//            )
//            piecesBox.add(
//                ChessPiece(
//                    0 + i * 7,
//                    7,
//                    ChessPlayer.BLACK,
//                    ChessFigure.ROOK,
//                    R.drawable.black_rook
//                )
//            )
//
//            piecesBox.add(
//                ChessPiece(
//                    1 + i * 5,
//                    0,
//                    ChessPlayer.WHITE,
//                    ChessFigure.KNIGHT,
//                    R.drawable.white_knight
//                )
//            )
//            piecesBox.add(
//                ChessPiece(
//                    1 + i * 5,
//                    7,
//                    ChessPlayer.BLACK,
//                    ChessFigure.KNIGHT,
//                    R.drawable.black_knight
//                )
//            )
//
//            piecesBox.add(
//                ChessPiece(
//                    2 + i * 3,
//                    0,
//                    ChessPlayer.WHITE,
//                    ChessFigure.BISHOP,
//                    R.drawable.white_bishop
//                )
//            )
//            piecesBox.add(
//                ChessPiece(
//                    2 + i * 3,
//                    7,
//                    ChessPlayer.BLACK,
//                    ChessFigure.BISHOP,
//                    R.drawable.black_bishop
//                )
//            )
//
//        }
//
//        for (i in 0..7) {
//            piecesBox.add(
//                ChessPiece(
//                    i,
//                    1,
//                    ChessPlayer.WHITE,
//                    ChessFigure.PAWN,
//                    R.drawable.white_pawn
//                )
//            )
//            piecesBox.add(
//                ChessPiece(
//                    i,
//                    6,
//                    ChessPlayer.BLACK,
//                    ChessFigure.PAWN,
//                    R.drawable.black_pawn
//                )
//            )
//        }
//
//        piecesBox.add(
//            ChessPiece(
//                3,
//                0,
//                ChessPlayer.WHITE,
//                ChessFigure.QUEEN,
//                R.drawable.white_queen
//            )
//        )
//        piecesBox.add(
//            ChessPiece(
//                3,
//                7,
//                ChessPlayer.BLACK,
//                ChessFigure.QUEEN,
//                R.drawable.black_queen
//            )
//        )
//        piecesBox.add(ChessPiece(4, 0, ChessPlayer.WHITE, ChessFigure.KING, R.drawable.white_king))
//        piecesBox.add(ChessPiece(4, 7, ChessPlayer.BLACK, ChessFigure.KING, R.drawable.black_king))
//
//    }
}
