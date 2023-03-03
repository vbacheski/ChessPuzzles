package com.fict.chesspuzzle.setupposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.text.input.ImeAction
import com.fict.chesspuzzle.R
import com.fict.chesspuzzle.models.BoardModel
import com.fict.chesspuzzle.ui.theme.*
import com.fict.chesspuzzle.utils.MyUtils
import com.fict.chesspuzzle.widgets.MyInputField
import com.fict.chesspuzzle.widgets.PiecesComponent
import com.fict.chesspuzzle.widgets.SelectedPiece
import com.fict.chesspuzzle.widgets.drawPieces


/**
 * Should be able to create valid puzzle.
 */
class SetupPositionActivity : ComponentActivity() {

    private val viewModel by viewModels<SetupPositionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = rememberWindowSizeClass()
            MyApplicationTheme(window) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    viewModel.updateBoard(MyUtils.getInitEmptyBoardModel())
                    SetupScreen(viewModel)
                }
            }
        }
    }

    @Composable
    fun SetupScreen(viewModel: SetupPositionViewModel) {
        SetupComponent(
            board = viewModel.boardModel,
            onSquareClicked = viewModel::onSquareClicked,
            onPointsChange = viewModel::onPointsChange,
            onDescriptionChange = viewModel::onDescriptionChange
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SetupComponent(
        board: BoardModel,
        onSquareClicked: (Int, Int, String) -> Unit,
        onPointsChange: (Int) -> Unit,
        onDescriptionChange: (String) -> Unit
    ) {
        var setSide by remember { mutableStateOf("") }
        val selectedFigure = remember {
            mutableStateOf("")
        }
        val description = remember {
            mutableStateOf("")
        }

        val pointsSlider = remember {
            mutableStateOf(0f)
            mutableStateOf(0f)
        }

        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(), color = Color.LightGray
        ) {
            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState(), enabled = true),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

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
                            painter = painterResource(id = R.drawable.greenwood_background),
                            contentDescription = null
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
                                                        color = MyUtils.getSelectionSquareColor(
                                                            board.get(x, y).isSelectedFrom,
                                                            board.get(x, y).isSelectedTo
                                                        )
                                                    )
                                                )
                                                .background(
                                                    MyUtils.getBackgroundSquareColor(
                                                        board.get(x, y).isLightSquare
                                                    )
                                                )
                                                .clickable(
                                                    interactionSource = MutableInteractionSource(),
                                                    indication = null
                                                ) {
                                                    onSquareClicked(x, y, selectedFigure.value)
                                                }) {


                                            if (y == 7) {
                                                Text(
                                                    text = "${'a' + x}",
                                                    color = if (x % 2 == y % 2) green else creamy,
                                                    modifier = Modifier
                                                        .align(Alignment.BottomEnd)
                                                        .padding(0.dp, 0.dp, 0.dp, 0.dp)
                                                )
                                            }

                                            if (x == 0) {
                                                Text(
                                                    text = "${1 + y}",
                                                    color = if (x % 2 == y % 2) green else creamy,
                                                )
                                            }
                                            drawPieces(board = board, x = x, y = y)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } // box

                PiecesComponent() {
                    selectedFigure.value = it
                }
                SelectedPiece(figureType = selectedFigure.value)


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                    backgroundColor = creamy
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.padding(start = 8.dp))
                            Text(text = "Set solution", color = green)
                            Spacer(modifier = Modifier.padding(start = 40.dp))

                            Button(
                                onClick = { selectedFigure.value = "f" },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(green)
                            ) {
                                Text(
                                    "From",
                                    style = MaterialTheme.typography.button,
                                    color = creamy
                                )
                            }

                            Spacer(modifier = Modifier.padding(start = 8.dp))
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "")
                            Spacer(modifier = Modifier.padding(start = 8.dp))
                            Button(
                                onClick = { selectedFigure.value = "t" },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(green)
                            ) {
                                Text(
                                    "To",
                                    style = MaterialTheme.typography.button,
                                    color = creamy
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                    backgroundColor = creamy
                ) {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)

                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(text = "White", color = green)
                            Checkbox(
                                checked = setSide == "w",
                                onCheckedChange = { setSide = "w" },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = green,

                                    )
                            )

                            Text(text = "Black", color = green)
                            Checkbox(
                                checked = setSide == "b",
                                onCheckedChange = { setSide = "b" },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = green
                                )
                            )
                        }

                        MyInputField(
                            modifier = Modifier.fillMaxWidth(),
                            name = "Description",
                            description.value,
                            imeAction = ImeAction.Next,
                            singleLine = false
                        ) {
                            description.value = it
                            onDescriptionChange(it)
                        }
                        Slider(
                            modifier = Modifier,
                            value = pointsSlider.value,
                            onValueChange = { newVal ->
                                pointsSlider.value = newVal
                                onPointsChange(getPoints(pointsSlider.value))
                            },
                            steps = 5,
                            onValueChangeFinished = {

                            },
                            colors = SliderDefaults.colors(
                                activeTrackColor = green,
                                inactiveTrackColor = white
                            ),
                            thumb = {
                                Image(
                                    painterResource(id = R.drawable.black_knight),
                                    "",
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                        )
                        Text(text = "Points: ${getPoints(pointsSlider.value)}", color = green)
                    }
                }
                val points = getPoints(pointsSlider.value)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            viewModel.createChessPuzzle(board, description.value, points, setSide)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(green)
                    ) {
                        Text(
                            "Create Puzzle",
                            style = MaterialTheme.typography.button,
                            color = creamy
                        )
                    }
                    Button(
                        onClick = { viewModel.updateBoard(MyUtils.getInitEmptyBoardModel()) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(green)
                    ) {
                        Text(
                            "Reset Board",
                            style = MaterialTheme.typography.button,
                            color = creamy
                        )
                    }
                }
            }
        }
    }

    fun getPoints(myPoints: Float): Int {
        return 100 + (myPoints * 400).toInt()
    }
}