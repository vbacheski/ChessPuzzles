package com.fict.chesspuzzle.retrofit

import com.fict.chesspuzzle.models.CreateChessPuzzleModel
import com.fict.chesspuzzle.models.PlayedFenDto
import com.fict.chesspuzzle.models.dto.LeaderboardModelDTO
import com.fict.chesspuzzle.models.dto.PuzzleModelDTO
import com.fict.chesspuzzle.models.dto.TournamentModelDetailsDTO
import retrofit2.Call
import retrofit2.http.*


interface Api {
    @GET("api/fens")
    fun getPuzzles(): Call<List<PuzzleModelDTO>>

    @GET("api/tournament/puzzles")
    fun getPuzzlesOfTournament(@Query("name") name: String): Call<TournamentModelDetailsDTO>

    @GET("api/leaderboard/list/{tournamentId}")
    fun getLeaderboardPlayers(@Path("tournamentId") tournamentId: String): Call<List<LeaderboardModelDTO>>

    @POST("/api/playedFen/makeAMove")
    fun createNewMove(@Body playedFenDto: PlayedFenDto): Call<Void?>?

    @PUT("/api/fens/addSolution/{id}")
    fun updateSolution(@Path("id") id: String, @Query("solution") solution: String): Call<PuzzleModelDTO>

    @POST("/chesspuzzle")
    fun createChessPuzzle(@Body createPuzzle: CreateChessPuzzleModel): Call<Void?>?

    @DELETE("/api/fens/delete/{id}")
    fun deletePuzzle(@Path("id") id: String): Call<PuzzleModelDTO>

    @PUT("/api/fens/approve/{id}")
    fun updateSolutionApprove(@Path("id") id: String): Call<PuzzleModelDTO>

    @PUT("/api/fens/decline/{id}")
    fun updateSolutionDecline(@Path("id") id: String): Call<PuzzleModelDTO>

}

// TODO: za Krste - da napravi approve option za FENs


