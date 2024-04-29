package com.robothaver.kandraw.viewModel.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap

data class PathData(
    val path: Path = Path(),
    val points: List<Offset>,
    val color: Color = Color.Red,
    val cap: StrokeCap = StrokeCap.Round,
    val strokeWidth: Float = 20f,
    val style: PathEffect? = null,
    val alpha: Float = 1f,
    val action: Actions = Actions.Draw,
    val index: Int = 0
)

enum class Actions {
    Draw,
    Erase,
    Clear
}
