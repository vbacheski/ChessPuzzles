package com.fict.chesspuzzle.models.dto

data class TournamentModelDetailsDTO(
    var puzzleList: List<PuzzleModelDTO>?,
    val duration: Int?,
    val id: String?
    ){
}