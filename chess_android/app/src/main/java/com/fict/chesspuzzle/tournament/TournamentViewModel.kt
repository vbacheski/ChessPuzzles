package com.fict.chesspuzzle.tournament

import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.models.BoardModel
import com.fict.chesspuzzle.models.PlayedFenDto
import com.fict.chesspuzzle.models.SquareModel
import com.fict.chesspuzzle.models.dto.PuzzleModelDTO
import com.fict.chesspuzzle.retrofit.Api
import com.fict.chesspuzzle.retrofit.RetrofitClient
import com.fict.chesspuzzle.services.prefs.MySharedPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


data class UIItemsWrapper(val countDownTimer: String, val tournamentEnded: Boolean = false)

class TournamentViewModel : ViewModel() {

    private val _boardModel = MutableStateFlow(BoardModel())
    val boardModel = _boardModel.asStateFlow()

    fun updateBoard(boardModel: BoardModel) {
        _boardModel.update {
            it.copy(squares = boardModel.squares)
        }
    }

    private var _countDownTimerModel by mutableStateOf(UIItemsWrapper(""))
    val countDownTimerModel: UIItemsWrapper
        get() = _countDownTimerModel

    fun updateUIItemsWrapper(uIItemsWrapper: UIItemsWrapper) {
        _countDownTimerModel = uIItemsWrapper.copy()
    }


    private var fromSquare = String()
    private var toSquare = String()

    private fun getPersonNick(): String {
        val prefs = MySharedPrefs(App.getAppContext())
        return prefs.getNickName()
    }

    fun getTournamentId(): String {
        val prefs = MySharedPrefs(App.getAppContext())
        return prefs.getTournamentId()
    }

    private fun getSolution(): String {
        return "$fromSquare-$toSquare"
    }

    fun onSquareClicked(x: Int, y: Int, legalMoves: MutableList<String>, currentFen: String) {

        val tmpSquares = boardModel.value.squares.toMutableList()

        //check if item is already selected
        if (boardModel.value.isSomeFieldSelectedAsFrom()) {
            //item is already selected (red selection visible on the board)
            //but not green destination yet

            val squareFrom = boardModel.value.getSelectedFromSquare()

            if (squareFrom != null) { //it should always be available
                if (squareFrom.x == x && squareFrom.y == y) {
                    //if from is equal with to - unselect the chess figure
                    val unselectFigure = tmpSquares.get(y * 8 + x).copy()
                    unselectFigure.isSelectedFrom = false
                    tmpSquares.set(y * 8 + x, unselectFigure)
                    //hide valid moves
                    hideValidMoves(tmpSquares)
                } else {
                    //from and to fields are different squares
                    val squareTo = tmpSquares.get(y * 8 + x).copy()
                    //Only move on valid moves
                    if (squareTo.isValidMove) {
                        squareTo.isSelectedTo = true
                        tmpSquares.set(y * 8 + x, squareTo)

                        if (squareTo.isSelectedTo) {
                            toSquare = boardModel.value.get(x, y).getSquareCoordinate()
                        }

                        squareTo.figureType = squareFrom.figureType //move the chess figure
                        tmpSquares.set(y * 8 + x, squareTo)
                        tmpSquares.set(
                            squareFrom.y * 8 + squareFrom.x,
                            squareFrom.copy(figureType = ".")
                        )
                    } else {
                        squareFrom.isSelectedFrom = false
                    }
                    //hide valid moves
                    hideValidMoves(tmpSquares)

                }
            }
        } else {
            //nothing is selected

            //check if this square can be selected
            if (boardModel.value.hasItem(x, y)) { // || noLegalMovesCheck
                val tmpSquare = tmpSquares.get(y * 8 + x).copy()
                tmpSquare.isSelectedFrom = true
                tmpSquares.set(y * 8 + x, tmpSquare)

                //check if white/black color is seleted and if white/black is on turn
                val getSide = currentFen.split(" ").toTypedArray()
                val turn = getSide[1]
                if (turn == "w") {
                    tmpSquare.isSelectedFrom = tmpSquare.isWhite()
                }
                if (turn == "b") {
                    tmpSquare.isSelectedFrom = tmpSquare.isBlack()
                }

                //Gets the selected square coordinate and stores it into "get"

                if (tmpSquare.isSelectedFrom) {
                    fromSquare = boardModel.value.get(x, y).getSquareCoordinate()
                }

                println("POSITION $fromSquare")
                //If the selected square is a1, loops through the array and finds move that applies to a1 for example a2a4
                //From a2a4 it gets the valid square a4 and converts it to x = 0 ,y = 3
                //Checks if the valid move is true and draws it
                for (y in 0..7) {
                    for (x in 0..7) {
                        for (move in legalMoves) {
                            if (move.contains(fromSquare)) {
                                val s: String =
                                    StringBuilder().append(move[2]).append(move[3]).toString()

                                val currentX = convertX(s) // mu davash b3 ti vraka x=4
                                val currentY = convertY(s)
                                val reverseY = 7 - y
                                if (x == currentX && reverseY == currentY) {
                                    val validSquareMove = tmpSquares.get(y * 8 + x).copy()
                                    if (turn == "w") {
                                        validSquareMove.isValidMove = tmpSquare.isWhite()
                                    }
                                    if (turn == "b") {
                                        validSquareMove.isValidMove = tmpSquare.isBlack()
                                    }
                                    tmpSquares.set(y * 8 + x, validSquareMove)
                                }
                            }
                        }
                    }
                }
            } else {
                //just ignore, the user selected empty square
            }
        }

        _boardModel.update {
            it.copy(squares = tmpSquares)
        }
    }

    private fun convertX(move: String): Int {
        return move[0] - 'a'
    }

    private fun convertY(move: String): Int {
        return move[1] - '1'
    }

    //  hide valid moves from certain figure
    private fun hideValidMoves(tmpSquares: MutableList<SquareModel>) {
        for (i in 0 until boardModel.value.squares.size) {
            val deleteValidSquareMove = tmpSquares.get(i).copy()
            deleteValidSquareMove.isValidMove = false
            tmpSquares.set(i, deleteValidSquareMove)
        }
    }

    // --------------------------TIMERS AND POINTS----------------------------

    private var _maxPoints by mutableStateOf(0)

    val maxPoints: Int
        get() = _maxPoints

    fun setMaxPoints(points: Int) {
        _maxPoints = points
    }

    private fun decreaseMaxPointsOnTick() {
        if (maxPoints < 101) {
            _maxPoints = 100
        } else {
            _maxPoints = maxPoints - 1
        }
    }

    private val myTimer = object : CountDownTimer(60 * 1000 * 60 * 60 * 24, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            onTick()
        }

        override fun onFinish() {
// TODO:  
        }
    }

    fun startCountDownTimer() {
        stopContDown()
        myTimer.start()
    }

    fun stopContDown() {
        myTimer.cancel()
    }

    fun onTick() {
        Log.d("TAG", "tick")
        //update the points per move
        decreaseMaxPointsOnTick()

        //check for ending
        val now = System.currentTimeMillis()
        val p = MySharedPrefs(App.getAppContext())
        val durationOfTournament = 1000 * 60 * p.getLastTournamentDuration()
        val tournamentEnd = (relativeTime + durationOfTournament) - now //* 60
        if (now > relativeTime + durationOfTournament) {
            //tournament should end
            updateUIItemsWrapper(
                UIItemsWrapper(
                    countDownTimer = getCountDownText(tournamentEnd),
                    tournamentEnded = true
                )
            )
        } else {
            //update the tournament count down timer
            updateUIItemsWrapper(
                UIItemsWrapper(
                    countDownTimer = getCountDownText(tournamentEnd),
                    tournamentEnded = false
                )
            )
        }
    }

    private fun getCountDownText(tournamentEnd: Long): String {
        if (tournamentEnd < 0) {
            return "00:00"
        }
        val minutes = (tournamentEnd / 1000) / 60
        val seconds = (tournamentEnd / 1000) % 60
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }


    private var relativeTime = 0L
    fun setRelativeTime(relativeTimeArg: Long) {
        relativeTime = relativeTimeArg
    }

    // --------------------------SENDING DATA TO BACKEND----------------------------

    fun sendDataToBackEnd(fenId: String, puzzlePoints: Int) {
        viewModelScope.launch {
            val apiService: Api = RetrofitClient().getRetrofitClient()

            val newMove = PlayedFenDto(
                username = getPersonNick(),
                fenId = fenId,
                tournamentId = getTournamentId(),
                playedSolution = getSolution(),
                actualPoints = puzzlePoints
            )
            val call: Call<Void?>? = apiService.createNewMove(newMove)

            call?.enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    Log.d("response", response.toString())
                    if (response.isSuccessful) {
                        println("Successful!")
                    } else {
                        Toast.makeText(
                            App.getAppContext(),
                            "Error when sending data to back-end" ,
                            Toast.LENGTH_LONG
                        ).show()
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

    fun declinePuzzle(fenId: String) {
        viewModelScope.launch {
            val apiService: Api = RetrofitClient().getRetrofitClient()

            val call: Call<PuzzleModelDTO> = apiService.updateSolutionDecline(fenId)

            call.enqueue(object : Callback<PuzzleModelDTO> {
                override fun onResponse(
                    call: Call<PuzzleModelDTO>,
                    response: Response<PuzzleModelDTO>
                ) {
                    Log.d("response", response.toString())
                    if (response.isSuccessful) {
                        println("Successful!")
                    }
                }

                override fun onFailure(call: Call<PuzzleModelDTO>, t: Throwable) {
                    Toast.makeText(
                        App.getAppContext(),
                        "Error: " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

    }

    fun approvePuzzle(fenId: String) {
        viewModelScope.launch {
            val apiService: Api = RetrofitClient().getRetrofitClient()

            val call: Call<PuzzleModelDTO> = apiService.updateSolutionApprove(fenId)

            call.enqueue(object : Callback<PuzzleModelDTO> {
                override fun onResponse(
                    call: Call<PuzzleModelDTO>,
                    response: Response<PuzzleModelDTO>
                ) {
                    Log.d("response", response.toString())
                    if (response.isSuccessful) {
                        println("Successful!")
                    }
                }

                override fun onFailure(call: Call<PuzzleModelDTO>, t: Throwable) {
                    Toast.makeText(
                        App.getAppContext(),
                        "Error: " + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

    }

    fun updateSolutionToBackEnd(fenId: String) {
        viewModelScope.launch {
            val apiService: Api = RetrofitClient().getRetrofitClient()

            val call: Call<PuzzleModelDTO> = apiService.updateSolution(fenId, getSolution())

            call.enqueue(object : Callback<PuzzleModelDTO> {
                override fun onResponse(
                    call: Call<PuzzleModelDTO>,
                    response: Response<PuzzleModelDTO>
                ) {
                    Log.d("response", response.toString())
                    if (response.isSuccessful) {
                        Log.d("response", response.toString())
                    } else {
                        Log.d("response", "Error updating")
                        Toast.makeText(
                            App.getAppContext(),
                            "Error Updating: ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PuzzleModelDTO>, t: Throwable) {
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