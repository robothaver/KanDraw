package com.example.kandraw.composables.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

data class PathData (
    val points: MutableList<Offset>,
    val color: Color = Color.Red,
    val cap: StrokeCap = StrokeCap.Round,
    val strokeWidth: Float = 20f,
    val style: DrawStyle = Stroke(
        width = strokeWidth,
        cap = cap,
//        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f), 2f)
//        pathEffect = PathEffect.cornerPathEffect(100f)
    ),
    val alpha: Float = 1f,
    val wasErased: Boolean = false,
    val index: Int = 0
)