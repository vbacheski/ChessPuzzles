package com.fict.chesspuzzle.tournament

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.models.BoardModel
import com.fict.chesspuzzle.models.PuzzleModel
import com.fict.chesspuzzle.models.dto.PuzzleModelDTOMapper
import com.fict.chesspuzzle.models.dto.TournamentModelDetailsDTO
import com.fict.chesspuzzle.retrofit.Api
import com.fict.chesspuzzle.retrofit.RetrofitClient
import com.fict.chesspuzzle.services.prefs.MySharedPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


data class JoinTournamentModel(
    var nickname: String = "",
    var tournamentName: String = "",
    var tournamentId: String = ""
)

class JoinTournamentViewModel : ViewModel() {

    private var _myModel = MutableStateFlow(JoinTournamentModel())
    val myModel = _myModel.asStateFlow()


    val puzzleModelDTOMapper = PuzzleModelDTOMapper()

    fun isTournamentCompleted(): Boolean {
        val prefs = MySharedPrefs(App.getAppContext())
        return prefs.getTournamentCompleted(myModel.value.tournamentId)
    }

    fun updateNickName(newNick: String) {
        _myModel.update { it.copy(nickname = newNick) }
        val prefs = MySharedPrefs(App.getAppContext())
        prefs.saveNickName(newNick)
    }

    fun updateTournamentName(tournamentName: String) {
        _myModel.update { it.copy(tournamentName = tournamentName) }
        val prefs = MySharedPrefs(App.getAppContext())
        prefs.saveTournamentName(tournamentName)
    }
    // Get puzzles from backend
    fun startPullingForTournamentStatus() {
        Log.v("tag", "start pinging server...")
        viewModelScope.launch {
            val numberOfRetries = 1000
            var stopPolling = false
            try {

                retry(times = numberOfRetries) {
                    Log.v("tag", "ping server")

                    //when tournament is ready -> go to tournament screen
                    if (stopPolling) {
                        return@retry
                    }

                    val tournamentName = MySharedPrefs(App.getAppContext()).getTournamentName()
                    val apiService: Api = RetrofitClient().getRetrofitClient()
                    val call: Call<TournamentModelDetailsDTO> =
                        apiService.getPuzzlesOfTournament(tournamentName)
                    call.enqueue(object : Callback<TournamentModelDetailsDTO> {
                        override fun onResponse(
                            call: Call<TournamentModelDetailsDTO>,
                            response: Response<TournamentModelDetailsDTO>
                        ) {
                            if (response.body() != null) {

                                val tournamentModelDetails =
                                    response.body() as TournamentModelDetailsDTO

                                if (tournamentModelDetails.puzzleList == null) {

                                } else {

                                    val p = MySharedPrefs(App.getAppContext())
                                    p.saveTournamentId(tournamentModelDetails.id ?: "")
                                    p.setLastTournamentDuration(tournamentModelDetails.duration ?: 0)
                                    val puzzleList =
                                        puzzleModelDTOMapper.toDomainList(tournamentModelDetails.puzzleList)
                                    Log.d("TAG", "Response = $puzzleList")
                                    if (puzzleList?.isNotEmpty() == true) {
                                        stopPolling = true
                                        p.setIsFirstTimeLogged(isLogged = true)
                                        App.setPuzzles(puzzleList as ArrayList<PuzzleModel>) //Return puzzleList to get puzzles from backend
                                        App.startTournamentScreen()
                                    } else {
                                        //keep pooling - or error todo
                                    }
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<TournamentModelDetailsDTO>,
                            t: Throwable
                        ) {
                            Log.d("TAG", "Response = $t")
                        }
                    })
                    if (!stopPolling) {
                        throw Exception()
                    }
                }
            } catch (e: Exception) {
                Log.v("tag", "ping server error")
            }
        }
    }
}

private suspend fun <T> retry(
    times: Int,
    initialDelayMillis: Long = 5000,
    maxDelayMillis: Long = 10000,
    factor: Double = 2.0,
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMillis
    repeat(times) {
        try {
            return block()
        } catch (exception: Exception) {

        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
    }
    delay(currentDelay)
    return block() // last attempt
}

//fun fenPuzzlesMockedData(): ArrayList<PuzzleModel> {
//    return arrayListOf(
//        PuzzleModel("1", "4r1rk/5p2/3p4/R7/6R1/7P/7b/2BK4 w KQkq - 0 1", "Opis 1", 500),
//        PuzzleModel("2", "8/8/8/8/8/1K2N3/3N3p/k7 w - - 0 1", "Opis 2", 500),
//        PuzzleModel("3", "k1K1N3/p7/8/8/8/8/8/8 w - - 0 1", "Opis 3", 500),
//        PuzzleModel("4", "6rk/6pr/3N4/8/8/8/7P/7K w - - 0 1", "Opis 4", 500),
//        PuzzleModel("5", "6k1/5ppp/8/8/7q/8/5PPP/4Q1K1 w - - 0 1", "Opis 5", 500),
//        PuzzleModel("6", "1kr5/6Qp/pP4p1/P4p2/2q5/7P/5PP1/4R1K1 w - - 0 1", "Opis 6", 500),
//        PuzzleModel("7", "r3kb1r/1p3ppp/8/3np1B1/1p6/8/PP3PPP/R3KB1R w KQkq - 0 1", "Opis 7", 500),
//        PuzzleModel("8", "8/8/8/2N5/8/8/p1K5/k7 w - - 0 1", "Opis 8", 500),
//        PuzzleModel("9", "5qk1/3R4/6pP/6K1/8/8/1B6/8 w - - 0 1", "Opis 9", 500),
//        PuzzleModel("10", "k7/1R6/K7/8/2N5/6p1/8/8 w - - 0 1", "Opis 10", 500)
//    )
//}
