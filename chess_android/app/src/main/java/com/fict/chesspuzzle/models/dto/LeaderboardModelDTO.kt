package com.fict.chesspuzzle.models.dto

data class LeaderboardModelDTO(
    val id: String? = null, //todo, this was int and it needs to be String
    //also this is id, not an rank, many changes need to follow - ask krste
    val nickname: String? = null,
    val points: Int? = null,
    val tournamentId: String? = null,
    val numberOfIncorrectPlayedPuzzles: Int? = null,
    val numberOfPlayedPuzzles: Int? = null,
    val numberOfCorrectPlayedPuzzles: Int? = null
)
