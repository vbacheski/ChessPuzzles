package com.fict.chesspuzzle.models.dto


data class PuzzleModelDTO(
    val id: String? = null,
    val fen: String? = null,
    val description: String? = null,
    val status: String? = null,
    val maxPoints: Int? = null,
    val solution: String? = null,
)

