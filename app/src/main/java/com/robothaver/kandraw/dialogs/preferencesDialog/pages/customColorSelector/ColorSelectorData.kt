package com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector

import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.viewModel.data.PenColor

data class ColorSelectorData(
    val color: PenColor,
    val id: ColorSelectorIds,
    val defaultColors: List<Color>? = null
)


enum class ColorSelectorIds {
    SmallCellColor,
    LargeCellColor,
    BackgroundColor
}
