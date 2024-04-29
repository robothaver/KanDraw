package com.robothaver.kandraw.viewModel.data

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class GridSettings(
    val isGridEnabled: Boolean = true,
    val isLargeGridEnabled: Boolean = true,
    val gridOffset: Offset = Offset(0f, 0f),
    val smallCellSize: Float = 80f,
    val smallCellColor: Color = Color.Gray,
    val smallCellStrokeWidth: Float = 5f,
    val largeCellSize: Int = 5,
    val largeCellColor: Color = Color.White,
    val largeCellStrokeWidth: Float = 6f
)
