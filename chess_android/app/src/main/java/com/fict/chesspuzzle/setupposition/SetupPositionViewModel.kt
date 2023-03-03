package com.fict.chesspuzzle.setupposition

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.models.BoardModel
import com.fict.chesspuzzle.models.CreateChessPuzzleModel
import com.fict.chesspuzzle.retrofit.Api
import com.fict.chesspuzzle.retrofit.RetrofitClient
import com.github.bhlangonijr.chesslib.Square
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class SetupPositionModel(
    val solutionFromSquare: Square,
    val solutionToSquare: Square,
    val description: String = "",
    val points: Int = 100
)

private var from = String()
private var to = String()

class SetupPositionViewModel : ViewModel() {
    private var _myModel by mutableStateOf(
        SetupPositionModel(
            Square.NONE,
            Square.NONE
        )
    )

    val myModel: SetupPositionModel
        get() = _myModel

    fun setDescription(description: String) {
        _myModel = SetupPositionModel(
            myModel.solutionFromSquare,
            myModel.solutionToSquare,
            points = myModel.points,
            description = description
        )
    }

    fun setPoints(points: Int) {
        _myModel = SetupPositionModel(
            myModel.solutionFromSquare,
            myModel.solutionToSquare,
            description = myModel.description,
            points = points
        )
    }

    private var _boardModel by mutableStateOf(BoardModel())

    val boardModel: BoardModel
        get() = _boardModel

    fun updateBoard(boardModel: BoardModel) {
        _boardModel = boardModel.copy()
    }

    fun onPointsChange(x: Int) {
        setPoints(x)
    }

    fun onDescriptionChange(x: String) {
        setDescription(x)
    }

    fun onSquareClicked(xArg: Int, yArg: Int, figureType: String) {
        when (figureType) {
            "f" -> {
                val tmpSquares = _boardModel.squares.toMutableList()
                val editingSquare = boardModel.get(yArg * 8 + xArg).copy()
                editingSquare.isSelectedFrom = !editingSquare.isSelectedFrom
                tmpSquares.set(yArg * 8 + xArg, editingSquare)
                _boardModel = BoardModel(tmpSquares)
                from = boardModel.get(xArg, yArg).getSquareCoordinate()
            }
            "t" -> {
                val tmpSquares = _boardModel.squares.toMutableList()
                val editingSquare = boardModel.get(yArg * 8 + xArg).copy()
                editingSquare.isSelectedTo = !editingSquare.isSelectedTo
                tmpSquares.set(yArg * 8 + xArg, editingSquare)
                _boardModel = BoardModel(tmpSquares)
                to = boardModel.get(xArg, yArg).getSquareCoordinate()
            }
            "d" -> {
                val tmpSquares = _boardModel.squares.toMutableList()
                val editingSquare =
                    boardModel.get(yArg * 8 + xArg)
                        .copy(isSelectedTo = false, isSelectedFrom = false, figureType = ".")
                tmpSquares.set(yArg * 8 + xArg, editingSquare)
                _boardModel = BoardModel(tmpSquares)
            }
            else -> {
                val tmpSquares = _boardModel.squares.toMutableList()
                val editingSquare = boardModel.get(yArg * 8 + xArg).copy(figureType = figureType)
                tmpSquares.set(yArg * 8 + xArg, editingSquare)
                _boardModel = BoardModel(tmpSquares)
            }
        }

    }

    fun createChessPuzzle(setUpBoard: BoardModel, description: String, points: Int, side: String) {

        val createFen = boardModel.getFenFromBoard(setUpBoard) + side
        val createValidMove = from + to
        val createdPuzzles = CreateChessPuzzleModel(createFen, createValidMove, description, points)

        viewModelScope.launch {
            //ovde celoto, pri uspesno kreiranje treba da se zatvori ekranot
            //pri neuspesno da se kazi deka imalo greska
            val apiService: Api = RetrofitClient().getRetrofitClient()
            val call: Call<Void?>? = apiService.createChessPuzzle(createdPuzzles)
            call?.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    Log.d("response", response.toString())
                    if (response.isSuccessful) {
                        println("Successful!")
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    Toast.makeText(
                        App.getAppContext(),
                        "Error: " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}

