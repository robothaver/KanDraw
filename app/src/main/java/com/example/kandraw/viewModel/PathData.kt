package com.example.kandraw.viewModel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

data class PathData(
    val path: Path = Path(),
    val points: MutableList<Offset>,
    val color: Color = Color.Red,
    val cap: StrokeCap = StrokeCap.Round,
    val strokeWidth: Float = 20f,
    val style: DrawStyle = Stroke(
        width = strokeWidth,
        cap = cap,
        join = StrokeJoin.Round
    ),
    val alpha: Float = 1f,
    val wasErased: Boolean = false,
    val index: Int = 0
)