package com.fict.chesspuzzle.services


import com.fict.chesspuzzle.models.PuzzleModel

class PuzzleService {

    private var puzzleList = ArrayList<PuzzleModel>()

    fun setPuzzle(list: ArrayList<PuzzleModel>) {
        puzzleList = list
    }

    fun getPuzzles() : ArrayList<PuzzleModel> {
        return puzzleList
    }
}