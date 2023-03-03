package com.fict.chesspuzzle.tournament

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.R
import com.fict.chesspuzzle.models.BoardModel
import com.fict.chesspuzzle.models.SquareModel
import com.fict.chesspuzzle.services.prefs.MySharedPrefs
import com.fict.chesspuzzle.ui.theme.*
import com.fict.chesspuzzle.usecase.LibraryLogic
import com.fict.chesspuzzle.utils.MyUtils.getBackgroundSquareColor
import com.fict.chesspuzzle.utils.MyUtils.getSelectionSquareColor
import com.fict.chesspuzzle.widgets.SuccessDialog
import com.fict.chesspuzzle.widgets.drawPieces
import com.github.bhlangonijr.chesslib.*
import com.github.bhlangonijr.chesslib.move.Move

class TournamentComposeActivity : ComponentActivity() {

    private val viewModel by viewModels<TournamentViewModel>()
    private val libraryLogic = LibraryLogic()
    private var currentFenIndex = 0

    private var relativeTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = rememberWindowSizeClass()
            MyApplicationTheme(window) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    overridePendingTransition(0, 0) // No animation on createActivity

                    viewModel.updateBoard(getBoardModel(currentFenIndex))
                    BoardScreen(viewModel)
                    viewModel.startCountDownTimer()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = MySharedPrefs(App.getAppContext())
        val lastFenIndex = prefs.getLastFenIndex()

        if (prefs.hasKey("last_fen_idex")) {
            currentFenIndex = lastFenIndex // Uncomment to remember where you left off
        }


        if ((currentFenIndex + 1) == App.puzzleService.getPuzzles().size) {
            viewModel.stopContDown()
            viewModel.updateUIItemsWrapper(
                UIItemsWrapper(
                    countDownTimer = "",
                    tournamentEnded = true
                )
            )
        }

        val tournamentId = MySharedPrefs(this).getTournamentId()
        if (tournamentId != "") {
            val t = MySharedPrefs(this).getTournamentStartedTime(tournamentId)
            if (t < 1) {
                relativeTime = System.currentTimeMillis()
                MySharedPrefs(this).setTournamentStartedTime(
                    relativeTime,
                    tournamentId
                )
            } else {
                relativeTime = t
            }
        }
        viewModel.setRelativeTime(relativeTime)

        viewModel.updateBoard(getBoardModel(currentFenIndex))
    }


    //todo zboraj so krste zosto ova vo odredeni slicaj ima problemi
    override fun onStop() {
        super.onStop()
        val prefs = MySharedPrefs(App.getAppContext())
        prefs.setLastFenIndex(currentFenIndex)
    }

    override fun onBackPressed() {
        //do nothing
    }

    private fun getFenSymbol(x: Int, y: Int): String {
        val col = File.allFiles[x] //file is column, example A-File, H-File
        val row = Rank.allRanks[7 - y] //rank is 1st to 8th rank
        val sq = Square.encode(row, col)
        val piece = libraryLogic.boardLib.getPiece(sq)
        return piece.fenSymbol
    }

    private fun getBoardModel(i: Int): BoardModel {
        viewModel.setMaxPoints(App.puzzleService.getPuzzles().get(i).maxPoints)

        try {
            libraryLogic.boardLib.loadFromFen(App.puzzleService.getPuzzles().get(i).fen)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        val squares: MutableList<SquareModel> = mutableListOf()

        for (y in 0..7) {
            for (x in 0..7) {
                val isLightSquare = x % 2 == y % 2
                val s = SquareModel(
                    x, y, getFenSymbol(x, y),
                    isSelectedFrom = false,
                    isSelectedTo = false,
                    isValidMove = false,
                    isLightSquare = isLightSquare
                )
                squares.add(s)
            }
        }
        return BoardModel(squares)
    }

    @Composable
    fun BoardScreen(viewModel: TournamentViewModel) {
        val boardModel by viewModel.boardModel.collectAsState()
        BoardComponent(
            board = boardModel,
            onSquareClicked = viewModel::onSquareClicked
        )
    }

    @Composable
    fun BoardComponent(
        board: BoardModel,
        onSquareClicked: (Int, Int, MutableList<String>, String) -> Unit
    ) {
        val currentFen = libraryLogic.boardLib.fen
        var tmpLegalMoves: MutableList<String>
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//        val moves: List<Move> = libraryLogic.boardLib.legalMoves()
        val moves: List<Move> = libraryLogic.boardLib.pseudoLegalMoves()
        tmpLegalMoves = board.getLegalMoves(moves)

//        }

        val prefs = MySharedPrefs(this@TournamentComposeActivity)
        if (viewModel.countDownTimerModel.tournamentEnded
            || prefs.getTournamentCompleted(viewModel.getTournamentId())
        ) {
            viewModel.stopContDown()
            viewModel.updateUIItemsWrapper(
                UIItemsWrapper(
                    countDownTimer = "00:00",
                    tournamentEnded = true
                )
            )
            SuccessDialog(onDismiss = {})
        }

        val legalMoves = tmpLegalMoves

        Column(
            modifier = Modifier.paint(
                painterResource(id = R.drawable.chess_background2),
                contentScale = ContentScale.FillWidth
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .border(
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(10.dp),
                    )
                    .align(Alignment.End)
            ) {
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .width(100.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Time:",
                        color = greenText,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = viewModel.countDownTimerModel.countDownTimer
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    elevation = 4.dp,
                    modifier = Modifier
                        .padding(10.dp)
                        .border(BorderStroke(width = 1.dp, color = Color(0xFF4E5112)))
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.greenwood_background),
                        contentDescription = "chessboard frame",
                        contentScale = ContentScale.FillBounds
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .border(BorderStroke(width = 1.5.dp, color = Color(0xFF4E5112)))
                            .padding(2.dp)
                    ) {
                        for (y in 0..7) {
                            Row {
                                for (x in 0..7) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .border(
                                                BorderStroke(
                                                    width = 2.dp,
                                                    color = getSelectionSquareColor(
                                                        board.get(x, y).isSelectedFrom,
                                                        board.get(x, y).isSelectedTo
                                                    )
                                                )
                                            )
                                            .background(
                                                getBackgroundSquareColor(
                                                    board.get(x, y).isLightSquare
                                                )
                                            )
                                            .clickable {
                                                onSquareClicked(x, y, legalMoves, currentFen)
                                            }
                                    ) {

                                        if (board.get(x, y).isValidMove) {
                                            Image(
                                                painter = painterResource(id = R.drawable.valid_move6),
                                                contentDescription = null,
                                                modifier = Modifier.align(
                                                    Alignment.Center
                                                )
                                            )
                                        }

                                        if (y == 7) {
                                            Text(
                                                text = "${'a' + x}",
                                                fontSize = 10.sp,
                                                color = if (x % 2 == y % 2) green else creamy,
                                                modifier = Modifier
                                                    .align(Alignment.BottomEnd)
                                                    .padding(1.dp)
                                            )
                                        }

                                        if (x == 0) {
                                            Text(
                                                text = "${8 - y}",
                                                fontSize = 10.sp,
                                                color = if (x % 2 == y % 2) green else creamy,
                                                modifier = Modifier
                                                    .padding(1.dp)
                                            )
                                        }

                                        drawPieces(board = board, x = x, y = y)

                                        if (board.get(x, y).isSelectedTo) {

                                            //POST data to back-end - board.get(x, y).isSelectedTo
                                            val fenId =
                                                App.puzzleService.getPuzzles()[currentFenIndex].id

                                            //only to update the solutions !!!
                                            //viewModel.updateSolutionToBackEnd(fenId)
                                            //move to view model
                                            val fen =
                                                App.puzzleService.getPuzzles()[currentFenIndex]

                                            viewModel.sendDataToBackEnd(
                                                fen.id,
                                                viewModel.maxPoints
                                            )

                                            //if at the end of puzzles, show TournamentEnd screen
                                            if ((currentFenIndex + 1) < App.puzzleService.getPuzzles().size) {
                                                currentFenIndex++
                                                viewModel.updateBoard(getBoardModel(currentFenIndex))
                                            } else if ((currentFenIndex + 1) == App.puzzleService.getPuzzles().size) {
                                                viewModel.stopContDown()
                                                viewModel.updateUIItemsWrapper(
                                                    UIItemsWrapper(
                                                        countDownTimer = "00:00",
                                                        tournamentEnded = true
                                                    )
                                                )
                                                prefs.setTournamentCompleted(
                                                    true,
                                                    viewModel.getTournamentId()
                                                )
                                            }
                                        }
                                    }//box
                                }//for (x in 0..7) {
                            }//row
                        }//for (y in 0..7) {
                    }//column
                }//card
            } // box

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .border(
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(10.dp),
                    )
                    .align(CenterHorizontally)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                ) {
//for the first phase, we won't show description
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(
//                            text = "Description", color = greenText, fontWeight = FontWeight.Bold
//                        )
//                        Text(
//                            text = App.puzzleService.getPuzzles()[currentFenIndex].description
//                        )
//                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Solved: ", color = greenText, fontWeight = FontWeight.Bold)
                        Text(text = "${currentFenIndex + 1}/${App.puzzleService.getPuzzles().size}")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Puzzle max points: ",
                            color = greenText,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "${viewModel.maxPoints}")
                    }

//                    Button(
//                        onClick = {
//                            val fen =
//                            App.puzzleService.getPuzzles()[currentFenIndex]
//                            viewModel.declinePuzzle(fen.id)
//
////                            if ((currentFenIndex + 1) < App.puzzleService.getPuzzles().size) {
////                                currentFenIndex++
////                                viewModel.updateBoard(getBoardModel(currentFenIndex))
////                            }
//
//                        },
//                        colors = ButtonDefaults.buttonColors(green)
//                    ) {
//                        Text(
//                            "Decline Puzzle",
//                            color = white
//                        )
//                    }
//
//                    Button(
//                        onClick = {
//                            val fen =
//                                App.puzzleService.getPuzzles()[currentFenIndex]
//                            viewModel.approvePuzzle(fen.id)
//
////                            if ((currentFenIndex + 1) < App.puzzleService.getPuzzles().size) {
////                                currentFenIndex++
////                                viewModel.updateBoard(getBoardModel(currentFenIndex))
////                            }
//
//                        },
//                        colors = ButtonDefaults.buttonColors(green)
//                    ) {
//                        Text(
//                            "Approve Puzzle",
//                            color = white
//                        )
//                    }

//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(text = "Correct: ", color = greenText, fontWeight = FontWeight.Bold)
//                        Text(text = "0")
//                    }
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        Text(text = "Incorrect: ", color = greenText, fontWeight = FontWeight.Bold)
//                        Text(text = "0")
//                    }
                }
            }
        }
    }
}
