package com.fict.chesspuzzle.register_login

import android.content.Intent
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fict.chesspuzzle.R
import com.fict.chesspuzzle.ui.theme.*
import com.fict.chesspuzzle.widgets.MyInputField
import com.fict.chesspuzzle.widgets.NicknameAndEmailInputField
import com.fict.chesspuzzle.widgets.PasswordInputField
import kotlinx.coroutines.launch


class RegisterFormActivity : ComponentActivity() {

    private val viewModel by viewModels<RegisterFormViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = rememberWindowSizeClass()
            MyApplicationTheme(window) {

                RegisterForm(viewModel)

            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@RegisterFormActivity, LoginFormActivity::class.java))
//        overridePendingTransition(0, 0)
        finish()
    }
}


@SuppressLint("PrivateResource")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterForm(viewModel: RegisterFormViewModel) {

    val state = viewModel.myRegisterModel.collectAsState()

    var email = ""
    var password = ""

    var passwordVisibility by remember { mutableStateOf(false) }
    val visibilityIcon = if (passwordVisibility)
        painterResource(id = com.google.android.material.R.drawable.design_ic_visibility)
    else
        painterResource(id = com.google.android.material.R.drawable.design_ic_visibility_off)

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
                shape = RoundedCornerShape(10)
            ) {
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

                        Text(
                            text = "Register",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp,
                                color = greenText
                            ),
                            textAlign = TextAlign.Center

                        )
                        Spacer(modifier = Modifier.padding(10.dp))

                        NicknameAndEmailInputField(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            name = "Nickname",
                            value = state.value.nickname,
                            imeAction = ImeAction.Next,
                            singleLine = true,
                            leadingIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "Person icon"
                                    )
                                }
                            },
                            trailingIcon = {}
                        ) {
                            viewModel.updateNickname(it)
                        }
                        Spacer(modifier = Modifier.padding(10.dp))

                        NicknameAndEmailInputField(
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            name = "E-mail",
                            value = state.value.email,
                            imeAction = ImeAction.Next,
                            singleLine = true,
                            leadingIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Email,
                                        contentDescription = "Email icon"
                                    )

                                }
                            },
                            trailingIcon = {}
                        ) {
                            viewModel.updateEmail(it)
                            email = it
                        }

                        Spacer(modifier = Modifier.padding(10.dp))

                        PasswordInputField(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .onFocusEvent { event ->
                                    if (event.isFocused) {
                                        coroutineScope.launch {
                                            bringIntoViewRequester.bringIntoView()
                                        }
                                    }
                                },
                            name = "Password",
                            value = state.value.password,
                            imeAction = ImeAction.Done,
                            singleLine = true,
                            leadingIcon = {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Lock,
                                        contentDescription = "Lock icon"
                                    )
                                }
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                    Icon(
                                        painter = visibilityIcon,
                                        contentDescription = "Visibility Icon"
                                    )

                                }
                            },
                            keyboardType = KeyboardType.Password,
                            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()

                        ) {
                            viewModel.updatePassword(it)
                            password = it
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
                                    viewModel.onRegisterClick(email, password)
                                }) {
                                Text(text = "Register", color = white)
                            }
                            Spacer(modifier = Modifier.padding(20.dp))
                        }
                    }
                }
            }
        }
    }
}