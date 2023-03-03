package com.fict.chesspuzzle.models

data class CreateChessPuzzleModel (
    var fen: String,
    var solutionMove: String,
    var description: String,
    var puzzlePoints: Int
        )