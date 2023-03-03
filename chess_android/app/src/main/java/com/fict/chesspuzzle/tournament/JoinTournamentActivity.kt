package com.fict.chesspuzzle.tournament

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.fict.chesspuzzle.App
import com.fict.chesspuzzle.R
import com.fict.chesspuzzle.services.prefs.MySharedPrefs
import com.fict.chesspuzzle.ui.theme.*
import com.fict.chesspuzzle.widgets.LoadingView
import com.fict.chesspuzzle.widgets.MyInputField
import kotlinx.coroutines.launch


/**
 * User will need to enter tournament id and nick name.
 * In the future when we add proper login screens this will change.
 * At this moment for the MVP (minimal valuable product) it will be simplified.
 */
class JoinTournamentActivity : ComponentActivity() {

    private val viewModel by viewModels<JoinTournamentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = rememberWindowSizeClass()
            MyApplicationTheme(window) {
                //load saved prefs
                val prefs = MySharedPrefs(this)
                viewModel.updateTournamentName(prefs.getTournamentName())
                viewModel.updateNickName(prefs.getNickName())

                MyApp(viewModel)

            }
        }
    }

    override fun onBackPressed() {
        val exitApp = Intent(Intent.ACTION_MAIN)
        exitApp.addCategory(Intent.CATEGORY_HOME)
        exitApp.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(exitApp)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyApp(viewModel: JoinTournamentViewModel) {
    val state = viewModel.myModel.collectAsState()
    val buttonClickedState = remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = BringIntoViewRequester()

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        color = Color.White
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column {
                Row(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth()
                        .background(color = Color(0xFFC5E1A5))
                        .shadow(200.dp)
                        .paint(
                            painterResource(id = R.drawable.chess_background_pieces),
                            contentScale = ContentScale.FillBounds
                        )
                ) {
                }
                Row(
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxWidth()
                        .background(color = Color(0xFFFFFFFF))
                        .shadow(200.dp)
                        .paint(
                            painterResource(id = R.drawable.chess_background_pieces),
                            contentScale = ContentScale.FillBounds
                        )
                ) {
                }
            }
            Surface(
                elevation = 12.dp,
                shape = RoundedCornerShape(10)) {
                Box(
                    modifier = Modifier
                        .height(AppTheme.dimens.large)
                        .width(AppTheme.dimens.medium)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_chess_icon_logo),
                            contentDescription = "profile image",
                            modifier = Modifier.size(135.dp)
                        )
                        if (buttonClickedState.value) {
                            LoadingView()
                        } else {

                            val sharedPrefs = MySharedPrefs(App.getAppContext())
                            val isNicknameEnabled = !sharedPrefs.getIsFirstTimeLogged()

                            MyInputField(
                                modifier = Modifier.fillMaxWidth(0.8f),
                                name = "Nickname",
                                value = state.value.nickname ,
                                enabled = isNicknameEnabled,
                                imeAction = ImeAction.Next,
                                singleLine = true
                            ) {
                                viewModel.updateNickName(it)
                            }
                            Spacer(modifier = Modifier.padding(10.dp))

                            MyInputField(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .onFocusEvent { event ->
                                        if (event.isFocused) {
                                            coroutineScope.launch {
                                                bringIntoViewRequester.bringIntoView()
                                            }
                                        }
                                    },
                                name = "Tournament Name",
                                value = state.value.tournamentName,
                                imeAction = ImeAction.Done,
                                singleLine = true
                            ) {
                                viewModel.updateTournamentName(it)
                            }
                            Spacer(modifier = Modifier.padding(10.dp))

                            Box(
                                modifier = Modifier
                                    .padding(bottom = 15.dp)
                                    .bringIntoViewRequester(bringIntoViewRequester)
                            ) {
                                Button(modifier = Modifier
                                    .padding(8.dp)
                                    .height(50.dp),
                                    shape = RoundedCornerShape(30),
                                      colors = ButtonDefaults.buttonColors(green),
                                    onClick = {
                                        buttonClickedState.value = true
                                        viewModel.startPullingForTournamentStatus()
                                    }) {
                                    Text(text = "Join tournament", color = white)
                                }
                                Spacer(modifier = Modifier.padding(20.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}





