package com.fict.chesspuzzle.models.dto

import com.fict.chesspuzzle.models.LeaderboardModel
import com.fict.chesspuzzle.utils.LeaderboardMapper


class LeaderboardModelDTOMapper: LeaderboardMapper<LeaderboardModelDTO, LeaderboardModel> {
    override fun mapToDomainMapperLeaderboard(model: LeaderboardModelDTO): LeaderboardModel {
        return LeaderboardModel(
            id = model.id ?: "",
            playerName = model.nickname ?: "",
            points = model.points ?: 0
        )
    }

    override fun mapFromDomainMapperLeaderboard(domainModel: LeaderboardModel): LeaderboardModelDTO {
        return LeaderboardModelDTO(
            id = domainModel.id,
            nickname = domainModel.playerName,
            points = domainModel.points,

        )
    }

    fun toDomainList(initial: List<LeaderboardModelDTO>): List<LeaderboardModel>{
        return initial.map { mapToDomainMapperLeaderboard(it) }
    }

    fun fromDomainList(initial: List<LeaderboardModel>): List<LeaderboardModelDTO>{
        return initial.map { mapFromDomainMapperLeaderboard(it) }
    }
}