package com.fict.chesspuzzle.leaderboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fict.chesspuzzle.R
import com.fict.chesspuzzle.models.LeaderboardModel
import com.fict.chesspuzzle.services.prefs.MySharedPrefs
import com.fict.chesspuzzle.tournament.JoinTournamentActivity
import com.fict.chesspuzzle.ui.theme.*
import com.fict.chesspuzzle.ui.theme.creamy
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class LeaderboardComposeActivity : ComponentActivity() {

    private val viewModel by viewModels<LeaderboardComposeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = rememberWindowSizeClass()
            MyApplicationTheme(window) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    overridePendingTransition(0, 0) // No animation on createActivity

                    //back-end
                    val leaderboardModel by viewModel.leaderboardModel.collectAsState()
                    val sortedPlayers = leaderboardModel.list.sortedByDescending { it.points }
                    val tournamentId = MySharedPrefs(this).getTournamentId()

                    viewModel.setPlayers(sortedPlayers)
                    viewModel.startPullingLeaderBoardData(tournamentId)

                    DefaultPreview(viewModel, sortedPlayers)
                    Confetti()
                }
            }
        }
    }


    override fun onBackPressed() {
        startActivity(Intent(this, JoinTournamentActivity::class.java))
        overridePendingTransition(0, 0)
    }
}

@Composable
fun DefaultPreview(viewModel: LeaderboardComposeViewModel, sortedPlayers: List<LeaderboardModel>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .paint(
                    painterResource(id = R.drawable.leaderboard_background),
                    contentScale = ContentScale.FillBounds
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(70.dp)
                        .width(70.dp)
                        .paint(painterResource(id = R.drawable.ic_white_transparent_circle)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "2", fontSize = 30.sp, color = creamy)
                }
                Row(
                    Modifier
                        .width(100.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.secondPlace,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        fontFamily = tommyFontBold,
                        color = creamy
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(100.dp)
                        .paint(painterResource(id = R.drawable.ic_white_transparent_circle))
                ) {
                    Box(
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_winner_trophy),
                            contentDescription = null
                        )
                    }
                }
                Row(
                    Modifier
                        .width(100.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.firstPlace,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        fontFamily = tommyFontBold,
                        color = creamy
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .height(70.dp)
                        .width(70.dp)
                        .paint(painterResource(id = R.drawable.ic_white_transparent_circle)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "3", fontSize = 30.sp, color = creamy)
                }
                Row(
                    Modifier
                        .width(100.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = viewModel.thirdPlace,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        color = creamy,
                        fontFamily = tommyFontBold
                    )
                }
            }
        }
        LazyColumn(contentPadding = PaddingValues(horizontal = 14.dp)) {

            item {
                if (sortedPlayers.isNotEmpty()) {
                    FirstPlaceItem(player = sortedPlayers.maxBy { it.points })
                }
            }
        }
        LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp)) {
            itemsIndexed(items = sortedPlayers.drop(1)) { index, player ->
                LeaderboardRowItem(
                    player = player,
                    index = index + 2,
                    isItMe = viewModel.isItMe(player.playerName)
                )
            }
        }
    }
}

@Composable
fun FirstPlaceItem(player: LeaderboardModel) {
    Row(
        modifier = Modifier
            .background(
                color = Color(0xFFFFE245),
                shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)
            )
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_winner_trophy),
            contentDescription = null,
            Modifier.width(22.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Text(
            text = player.playerName,
            Modifier.fillMaxWidth(0.6f),
            maxLines = 1,
            textAlign = TextAlign.Left,
            color = Color.White,
            fontFamily = tommyFontMedium
        )
        Text(
            text = "${player.points}",
            maxLines = 1,
            textAlign = TextAlign.Right,
            color = Color.White,
            fontFamily = tommyFontMedium
        )
    }
}

@Composable
fun LeaderboardRowItem(index: Int, player: LeaderboardModel, isItMe: Boolean) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStartPercent = 30,
                    topEndPercent = 30,
                    bottomEndPercent = 30,
                    bottomStartPercent = 30
                )
            )
            .background(if (isItMe) Color.LightGray else Color.White)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$index",
            fontFamily = tommyFont,
            maxLines = 1,
            fontWeight = if (isItMe) androidx.compose.ui.text.font.FontWeight.Companion.Bold else androidx.compose.ui.text.font.FontWeight.Companion.Normal
        )
        Text(
            text = player.playerName,
            Modifier.fillMaxWidth(0.6f),
            maxLines = 1,
            textAlign = TextAlign.Left,
            fontFamily = tommyFont,
            fontWeight = if (isItMe) androidx.compose.ui.text.font.FontWeight.Companion.Bold else androidx.compose.ui.text.font.FontWeight.Companion.Normal
        )
        Text(
            text = "${player.points}",
            maxLines = 1,
            textAlign = TextAlign.Right,
            fontFamily = tommyFont,
            fontWeight = if (isItMe) androidx.compose.ui.text.font.FontWeight.Companion.Bold else androidx.compose.ui.text.font.FontWeight.Companion.Normal
        )
    }
}

@Composable
fun Confetti() {
    val party = listOf(
        Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
    )
    KonfettiView(parties = party)
}
