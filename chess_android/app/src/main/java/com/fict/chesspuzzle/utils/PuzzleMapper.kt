package com.fict.chesspuzzle.utils

interface PuzzleMapper <T, PuzzleModel>{

    fun mapToDomainModel(model: T): PuzzleModel

    fun mapFromDomainModel(domainModel: PuzzleModel): T


}

interface LeaderboardMapper <T, LeaderboardModel>{
    fun mapToDomainMapperLeaderboard(model: T): LeaderboardModel

    fun mapFromDomainMapperLeaderboard(domainModel: LeaderboardModel): T
}

