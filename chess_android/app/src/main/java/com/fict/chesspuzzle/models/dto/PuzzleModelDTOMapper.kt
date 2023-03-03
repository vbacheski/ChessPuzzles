package com.fict.chesspuzzle.models.dto

import com.fict.chesspuzzle.models.PuzzleModel
import com.fict.chesspuzzle.utils.PuzzleMapper

class PuzzleModelDTOMapper: PuzzleMapper<PuzzleModelDTO, PuzzleModel> {
    override fun mapToDomainModel(model: PuzzleModelDTO): PuzzleModel {
        return PuzzleModel(
            id = model.id ?: "",
            fen = model.fen ?: "",
            description = model.description ?: "",
            maxPoints = model.maxPoints ?: 500,
        )
    }

    override fun mapFromDomainModel(domainModel: PuzzleModel): PuzzleModelDTO {
        return PuzzleModelDTO(
            id = domainModel.id,
            fen = domainModel.fen,
            description = domainModel.description,
            maxPoints = domainModel.maxPoints
        )
    }

    fun toDomainList(initial: List<PuzzleModelDTO>?): List<PuzzleModel>? {
        return initial?.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<PuzzleModel>): List<PuzzleModelDTO>{
        return initial.map { mapFromDomainModel(it) }
    }
}