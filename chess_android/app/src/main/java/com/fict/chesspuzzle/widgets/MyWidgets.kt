package com.fict.chesspuzzle.widgets

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build.VERSION.SDK_INT
import android.view.animation.Transformation
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.R
import com.fict.chesspuzzle.leaderboard.LeaderboardComposeActivity
import com.fict.chesspuzzle.models.BoardModel
import com.fict.chesspuzzle.ui.theme.AppTheme
import com.fict.chesspuzzle.ui.theme.creamy
import com.fict.chesspuzzle.ui.theme.green
import com.fict.chesspuzzle.ui.theme.white

class MyWidgets {
}

@Composable
// draw pieces symbols on the board
fun drawPieces(board: BoardModel, x: Int, y: Int) {
    if (board.get(x, y).isWhiteRook()) {
        Image(
            painter = painterResource(id = R.drawable.white_rook),
            contentDescription = null
        )
    }
    if (board.get(x, y).isBlackRook()) {
        Image(
            painter = painterResource(id = R.drawable.black_rook),
            contentDescription = null
        )
    }
    if (board.get(x, y).isWhiteKnight()) {
        Image(
            painter = painterResource(id = R.drawable.white_knight),
            contentDescription = null
        )
    }
    if (board.get(x, y).isBlackKnight()) {
        Image(
            painter = painterResource(id = R.drawable.black_knight),
            contentDescription = null
        )
    }
    if (board.get(x, y).isWhiteBishop()) {
        Image(
            painter = painterResource(id = R.drawable.white_bishop),
            contentDescription = null
        )
    }
    if (board.get(x, y).isBlackBishop()) {
        Image(
            painter = painterResource(id = R.drawable.black_bishop),
            contentDescription = null
        )
    }
    if (board.get(x, y).isWhiteQueen()) {
        Image(
            painter = painterResource(id = R.drawable.white_queen),
            contentDescription = null
        )
    }
    if (board.get(x, y).isBlackQueen()) {
        Image(
            painter = painterResource(id = R.drawable.black_queen),
            contentDescription = null
        )
    }
    if (board.get(x, y).isWhiteKing()) {
        Image(
            painter = painterResource(id = R.drawable.white_king),
            contentDescription = null
        )
    }
    if (board.get(x, y).isBlackKing()) {
        Image(
            painter = painterResource(id = R.drawable.black_king),
            contentDescription = null
        )
    }
    if (board.get(x, y).isWhitePawn()) {
        Image(
            painter = painterResource(id = R.drawable.white_pawn),
            contentDescription = null
        )
    }
    if (board.get(x, y).isBlackPawn()) {
        Image(
            painter = painterResource(id = R.drawable.black_pawn),
            contentDescription = null
        )
    }
}

@Composable
fun PiecesComponent(
    onItemSelected: (String) -> Unit
) {

    val modifier = Modifier.size(48.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        backgroundColor = creamy

    ) {
        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.white_pawn),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("P")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.white_rook),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("R")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.white_knight),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("N")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.white_bishop),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("B")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.white_queen),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("Q")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.white_king),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("K")
                            }
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.black_pawn),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("p")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.black_rook),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("r")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.black_knight),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("n")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.black_bishop),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("b")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.black_queen),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("q")
                            }
                        )
                        Image(
                            painter = painterResource(id = R.drawable.black_king),
                            contentDescription = null,
                            modifier = modifier.clickable {
                                onItemSelected("k")
                            }
                        )
                    }
                }
                Image(
                    painter = painterResource(id = R.drawable.delete_piece),
                    contentDescription = null,
                    modifier = modifier.clickable {
                        onItemSelected("d")
                    }
                )
            }

        }
    }
}

@Composable
fun SelectedPiece(
    figureType: String
) {
    val modifier = Modifier.size(48.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        backgroundColor = creamy
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (figureType != "") {
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(text = "Selected piece: ", color = green)
                when (figureType) {
                    "P" -> Image(
                        modifier = modifier,
                        painter = painterResource(id = R.drawable.white_pawn),
                        contentDescription = null
                    )
                    "R" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.white_rook),
                            contentDescription = null
                        )
                    "N" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.white_knight),
                            contentDescription = null
                        )
                    "B" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.white_bishop),
                            contentDescription = null
                        )
                    "Q" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.white_queen),
                            contentDescription = null
                        )
                    "K" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.white_king),
                            contentDescription = null
                        )
                    "p" -> Image(
                        modifier = modifier,
                        painter = painterResource(id = R.drawable.black_pawn),
                        contentDescription = null
                    )
                    "r" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.black_rook),
                            contentDescription = null
                        )
                    "n" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.black_knight),
                            contentDescription = null
                        )
                    "b" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.black_bishop),
                            contentDescription = null
                        )
                    "q" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.black_queen),
                            contentDescription = null
                        )
                    "k" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.black_king),
                            contentDescription = null
                        )
                    "d" ->
                        Image(
                            modifier = modifier,
                            painter = painterResource(id = R.drawable.delete_piece),
                            contentDescription = null
                        )
                    "f" ->
                        Text("selecting from")
                    "t" ->
                        Text("selecting to")
                    else -> {}
                }
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyInputField(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    enabled: Boolean = true,
    singleLine: Boolean,
    imeAction: ImeAction,
    onValueChange: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        enabled = enabled,
        singleLine = singleLine,
        onValueChange = onValueChange,
        label = { Text(name, style = TextStyle(color = green)) },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = green,
            unfocusedBorderColor = green,
            focusedLabelColor = green,
            unfocusedLabelColor = green
        )
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    enabled: Boolean = true,
    singleLine: Boolean,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (()-> Unit)?,
    visualTransformation: VisualTransformation,
    onValueChange: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        enabled = enabled,
        singleLine = singleLine,
        onValueChange = onValueChange,
        label = { Text(name, style = TextStyle(color = green)) },
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = green,
            unfocusedBorderColor = green,
            focusedLabelColor = green,
            unfocusedLabelColor = green
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NicknameAndEmailInputField(
    modifier: Modifier = Modifier,
    name: String,
    value: String,
    enabled: Boolean = true,
    singleLine: Boolean,
    imeAction: ImeAction,
    leadingIcon: @Composable (()-> Unit)?,
    trailingIcon: @Composable (()-> Unit)?,
    onValueChange: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier,
        value = value,
        enabled = enabled,
        singleLine = singleLine,
        onValueChange = onValueChange,
        label = { Text(name, style = TextStyle(color = green)) },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),

        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = green,
            unfocusedBorderColor = green,
            focusedLabelColor = green,
            unfocusedLabelColor = green
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
    )
}

@Composable
fun OneTimeClickableText(text : String, onClick : () -> Unit){
    Text(
        modifier = Modifier,
        text = text

    )
}



@Composable
fun LoadingView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(20.dp)) {
        Text(
            text = "The tournament is about to start, please wait...",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(100.dp))
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(elevation = 12.dp, shape = RoundedCornerShape(10)) {
            Box(

                modifier = Modifier
                    .height(AppTheme.dimens.large)
                    .width(AppTheme.dimens.medium)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    GifImage()
                    Text(
                        modifier = Modifier,
                        text = "Congratulation!",
                        color = Color(0xff4f455e),
                        style = MaterialTheme.typography.h1,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = "You have completed the tournament, click the button below to see who is the winner.",
                        color = Color(0xff4f455e),
                        style = MaterialTheme.typography.subtitle1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(AppTheme.dimens.small)
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                    Button(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(30),
                        colors = ButtonDefaults.buttonColors(green),
                        onClick = {
                            val intent = Intent(
                                App.getAppContext(),
                                LeaderboardComposeActivity::class.java
                            )
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            App.getAppContext().startActivity(intent)
                            ActivityCompat.finishAffinity(Activity())
                        }) {
                        Text(
                            text = "Continue",
                            color = white
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.success_trophy).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}