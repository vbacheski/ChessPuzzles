package com.fict.chesspuzzle.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.models.LeaderboardModel
import com.fict.chesspuzzle.models.dto.LeaderboardModelDTOMapper
import com.fict.chesspuzzle.models.dto.LeaderboardModelDTO
import com.fict.chesspuzzle.retrofit.Api
import com.fict.chesspuzzle.retrofit.RetrofitClient
import com.fict.chesspuzzle.services.prefs.MySharedPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


data class LeaderBoardUIModel(val list: List<LeaderboardModel>)

class LeaderboardComposeViewModel : ViewModel() {

    val leaderboardModelDTOMapper = LeaderboardModelDTOMapper()

    var firstPlace = String()
    var secondPlace = String()
    var thirdPlace = String()

    private val _leaderboardModel =
        MutableStateFlow(LeaderBoardUIModel(arrayListOf()))
    val leaderboardModel = _leaderboardModel.asStateFlow()

    fun updateLeaderboardModel(list: List<LeaderboardModel>) {
        _leaderboardModel.update {
            it.copy(list = list)
        }
    }

    fun setPlayers(list: List<LeaderboardModel>) {
        if (list.isNotEmpty() && list.size == 1) {
            firstPlace = list[0].playerName
        } else if (list.isNotEmpty() && list.size == 2) {
            firstPlace = list[0].playerName
            secondPlace = list[1].playerName
        } else if (list.isNotEmpty()) {
            firstPlace = list[0].playerName
            secondPlace = list[1].playerName
            thirdPlace = list[2].playerName

        }
    }

    fun isItMe(someUserName: String): Boolean {
        val prefs = MySharedPrefs(App.getAppContext())
        return someUserName.equals(prefs.getNickName())
    }

    fun startPullingLeaderBoardData(tournamenId: String) {
        Log.v("tag", "start pinging server...")
        viewModelScope.launch {
            val numberOfRetries = 1000
            val stopPolling = false
            try {
                retry(times = numberOfRetries) {
                    Log.v("tag", "ping server")
                    if (stopPolling) {
                        return@retry
                    }
                    var leaderboardListDTO: ArrayList<LeaderboardModelDTO>
                    val apiService: Api = RetrofitClient().getRetrofitClient()
                    val call: Call<List<LeaderboardModelDTO>> =
                        apiService.getLeaderboardPlayers(tournamenId)
                    call.enqueue(object : Callback<List<LeaderboardModelDTO>> {
                        override fun onResponse(
                            call: Call<List<LeaderboardModelDTO>>,
                            response: Response<List<LeaderboardModelDTO>>
                        ) {
                            if (response.body() != null) {
                                leaderboardListDTO =
                                    response.body() as java.util.ArrayList<LeaderboardModelDTO>
                                Log.d("TAG", "Response = $leaderboardListDTO")
                                val leaderboardList =
                                    leaderboardModelDTOMapper.toDomainList(leaderboardListDTO)
                                updateLeaderboardModel(leaderboardList)
                            }
                        }

                        override fun onFailure(
                            call: Call<List<LeaderboardModelDTO>>,
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

    private suspend fun <T> retry(
        times: Int,
        initialDelayMillis: Long = 5000,
        maxDelayMillis: Long = 30000,
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

    fun getMockPlayerData(): List<LeaderboardModel> {
        return listOf(
            LeaderboardModel(
                "0",
                "Nick",
                200
            ),
            LeaderboardModel(
                "0",
                "Petre",
                400
            ),
            LeaderboardModel(
                "0",
                "Janko",
                251
            ),
            LeaderboardModel(
                "0",
                "Trajko",
                50
            ),
            LeaderboardModel(
                "0",
                "Zoran",
                1000
            ),
            LeaderboardModel(
                "0",
                "Vanco Vanceski",
                200
            ),
            LeaderboardModel(
                "0",
                "Tome Tomeski",
                1400
            ),
            LeaderboardModel(
                "0",
                "Jane",
                251
            ),
            LeaderboardModel(
                "0",
                "Kirosix",
                300
            ),
            LeaderboardModel(
                "0",
                "Zarko",
                739
            )
        )
    }
}