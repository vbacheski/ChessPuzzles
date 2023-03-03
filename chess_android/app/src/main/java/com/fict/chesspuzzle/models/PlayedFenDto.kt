package com.fict.chesspuzzle.models

/**
 * Front end to beck-end. At the point when user make a move.
 */

data class PlayedFenDto(
    var username: String,
    var fenId: String,
    var tournamentId: String,
    var playedSolution: String,
    var actualPoints: Int,
)