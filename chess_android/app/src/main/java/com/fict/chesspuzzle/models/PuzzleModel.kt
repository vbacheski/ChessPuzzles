package com.fict.chesspuzzle.models

/**
 * Server keeps this data.
 */
data class PuzzleModel(
    var id: String,
    var fen: String,
    var description: String,
    var maxPoints: Int
)