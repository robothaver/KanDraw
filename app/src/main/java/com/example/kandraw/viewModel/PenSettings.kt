package com.example.kandraw.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke

data class PenSettings(
    val penColor: PenColor = PenColor(Color.Red, 1f, Color.Red),
    val cap: StrokeCap = StrokeCap.Round,
    val strokeWidth: Float = 20f,
    val style: DrawStyle = Stroke(
        width = strokeWidth,
        cap = cap,
        join = StrokeJoin.Round
    ),
    val alpha: Float = 1f
)
