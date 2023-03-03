package com.fict.chesspuzzle.tutorial

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.fict.chesspuzzle.ui.theme.MyApplicationTheme
import com.fict.chesspuzzle.ui.theme.rememberWindowSizeClass

class TutorialDetailActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val window = rememberWindowSizeClass()
            MyApplicationTheme(window) {
                Text(text = "Hello World")

            }
        }
    }
}


