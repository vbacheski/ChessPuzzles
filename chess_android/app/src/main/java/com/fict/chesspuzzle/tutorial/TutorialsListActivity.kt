package com.fict.chesspuzzle.tutorial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.fict.chesspuzzle.models.dto.TutorialModel
import com.fict.chesspuzzle.services.prefs.MySharedPrefs
import com.fict.chesspuzzle.tournament.MyApp
import com.fict.chesspuzzle.ui.theme.MyApplicationTheme
import com.fict.chesspuzzle.ui.theme.rememberWindowSizeClass

class TutorialsListActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        
            val window = rememberWindowSizeClass()
            MyApplicationTheme(window) {
              TutorialList{
                  startActivity(Intent(this, TutorialDetailActivity::class.java))
              }
                
            }
        }
    }
}

@Composable
fun TutorialList(navigateToDetails: (TutorialModel) -> Unit) {
    val tutorials = remember { Data.tutorialList}
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(
            items = tutorials,
            itemContent = {
                TutorialListItem(tutorial = it, navigateToDetails)
            }

        )
    }
}