package com.fict.chesspuzzle.services

import com.fict.chesspuzzle.models.LeaderboardModel

class LeaderboardService {

    private var leaderboardList = ArrayList<LeaderboardModel>()

    fun setLeaderboardData(list: ArrayList<LeaderboardModel>) {
        leaderboardList = list
    }

    fun getLeaderboardData() : ArrayList<LeaderboardModel> {
        return leaderboardList
    }
}
