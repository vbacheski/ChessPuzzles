package com.fict.chesspuzzle.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val small:Dp,
    val medium:Dp,
    val large:Dp
)

val smallDimensions = Dimensions(
    small = 280.dp,
    medium = 320.dp,
    large = 470.dp
)

val compactDimensions = Dimensions(
    small = 300.dp,
    medium = 350.dp,
    large = 500.dp
)

val mediumDimensions = Dimensions(
    small = 400.dp,
    medium = 400.dp,
    large = 550.dp
)

val largeDimensions = Dimensions(
    small = 500.dp,
    medium = 450.dp,
    large = 600.dp
)

val boxWidthDimensions = Dimensions(
    small = 300.dp,
    medium = 350.dp,
    large = 500.dp
)