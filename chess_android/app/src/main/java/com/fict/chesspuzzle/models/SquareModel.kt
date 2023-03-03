package com.fict.chesspuzzle.models

/**
 *   x - this is x position on the board 0 on left 7 on right
 *   y - this is y position on the board 0 on top 7 on bottom
 *   figureType - this square contains figure of type: p r n b q k, P R N B Q K, or "" for empty
 *   isSelected - this square is selected
 *   isValidMove - this square is valid move
 */
data class SquareModel(
    var x: Int, //this is x position on the board 0 on left 7 on right
    var y: Int, //this is y position on the board 0 on top 7 on bottom
    var figureType: String, //this square contains figure of type: p r n b q k, P R N B Q K, or "."  for empty
    //small letters black, capital letters white
    var isSelectedFrom: Boolean, //this square is selected as 'from'. It is indication that we will move the figure
    // from here
    var isSelectedTo: Boolean,//this square is selected as 'to'. It is indication for the destination of the figure
    var isValidMove: Boolean, //this square is valid move
    var isLightSquare: Boolean //white/black background of the square
) {

    fun getSquareCoordinate(): String {
        val file = arrayListOf("a", "b", "c", "d", "e", "f", "g", "h")
        val rank = arrayListOf("1", "2", "3", "4", "5", "6", "7", "8")
        for (y in rank[7 - y]) {
            for (x in file[x]) {
                return x + y.toString()
            }
        }
        return ""
    }

    // checks if the selected field is empty
    fun emptyField() = figureType != "."

    // checks if the selected figure is white rook
    fun isWhiteRook() = figureType == "R"

    // checks if the selected figure is black rook
    fun isBlackRook() = figureType == "r"

    // checks if the selected figure is white king
    fun isWhiteKing() = figureType == "K"

    // checks if the selected figure is black king
    fun isBlackKing() = figureType == "k"

    // checks if the selected figure is white pawn
    fun isWhitePawn() = figureType == "P"

    // checks if the selected figure is black pawn
    fun isBlackPawn() = figureType == "p"

    // checks if the selected figure is white queen
    fun isWhiteQueen() = figureType == "Q"

    // checks if the selected figure is black queeen
    fun isBlackQueen() = figureType == "q"

    // checks if the selected figure is white knight
    fun isWhiteKnight() = figureType == "N"

    // checks if the selected figure is black knight
    fun isBlackKnight() = figureType == "n"

    // checks if the selected figure is white bishop
    fun isWhiteBishop() = figureType == "B"

    // checks if the selected figure is black bishop
    fun isBlackBishop() = figureType == "b"

    fun isWhite(): Boolean {
        val whiteFigures = arrayListOf("R", "N", "B", "Q", "K", "P")
        for (whiteFigure in whiteFigures) {
            if (figureType == whiteFigure) {
                return true
            }
        }
        return false
    }

    fun isBlack(): Boolean {
        val blackFigures = arrayListOf("r", "n", "b", "q", "k", "p")
        for (blackFigure in blackFigures) {
            if (figureType == blackFigure) {
                return true
            }
        }
        return false
    }

}

