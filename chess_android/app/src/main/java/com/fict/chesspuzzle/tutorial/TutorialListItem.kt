package com.fict.chesspuzzle.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Card
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.fict.chesspuzzle.models.dto.TutorialModel



@Composable
fun TutorialListItem(tutorial: TutorialModel, navigationToDetails: (TutorialModel) -> Unit) {

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White
    ) {
        Row (
            modifier = Modifier
                .clickable { navigationToDetails(tutorial) }
                ){
            TutorialImage(tutorial = tutorial)
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = tutorial.title, style = typography.h4)
                Text(text = tutorial.description, style = typography.caption)

            }

        }
    }
}

@Composable
fun TutorialImage(tutorial: TutorialModel) {
    Image(
        modifier = Modifier
            .padding(8.dp)
            .size(86.dp),
        painter = painterResource(id = tutorial.imageId),
        contentDescription = null,
        contentScale = ContentScale.Crop


    )
}