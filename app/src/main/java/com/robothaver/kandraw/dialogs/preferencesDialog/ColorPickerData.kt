package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.viewModel.data.PenColor

data class ColorPickerData(
    val color: PenColor,
    val id: ColorPickerIds,
    val defaultColors: List<Color>? = null
)


enum class ColorPickerIds {
    SmallGridColor,
    LargeGridColor,
    BackgroundColor
}
