package com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector

import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.composables.customColorPicker.utils.changeColorBrightness
import com.robothaver.kandraw.viewModel.CanvasViewModel

class CustomColorSelectorHandler(
    private val selectedData: ColorSelectorData
) {
    fun updateBrightness(brightness: Float, viewModel: CanvasViewModel) {
        val gridSettings = viewModel.gridSettings.value
        val backgroundColor = viewModel.backgroundColor.value
        when (selectedData.id) {
            ColorSelectorIds.SmallCellColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        smallCellColor = gridSettings.smallCellColor.copy(
                            color = changeColorBrightness(
                                gridSettings.smallCellColor.hue,
                                brightness
                            ),
                            brightness = brightness
                        )
                    )
            }

            ColorSelectorIds.LargeCellColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        largeCellColor = gridSettings.largeCellColor.copy(
                            color = changeColorBrightness(
                                gridSettings.largeCellColor.hue,
                                brightness
                            ),
                            brightness = brightness
                        )
                    )
            }

            ColorSelectorIds.BackgroundColor -> {
                viewModel.backgroundColor.value =
                    backgroundColor.copy(
                        color = changeColorBrightness(backgroundColor.hue, brightness),
                        brightness = brightness
                    )
            }
        }
    }

    fun updateColor(hue: Color, viewModel: CanvasViewModel) {
        val gridSettings = viewModel.gridSettings.value
        val backgroundColor = viewModel.backgroundColor.value
        when (selectedData.id) {
            ColorSelectorIds.SmallCellColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        smallCellColor = gridSettings.smallCellColor.copy(
                            hue = hue,
                            color = changeColorBrightness(
                                hue,
                                gridSettings.smallCellColor.brightness
                            )
                        )
                    )
            }

            ColorSelectorIds.LargeCellColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        largeCellColor = gridSettings.largeCellColor.copy(
                            hue = hue,
                            color = changeColorBrightness(
                                hue,
                                gridSettings.largeCellColor.brightness
                            )
                        )
                    )
            }

            ColorSelectorIds.BackgroundColor -> {
                viewModel.backgroundColor.value =
                    backgroundColor.copy(
                        hue = hue,
                        color = changeColorBrightness(
                            hue,
                            backgroundColor.brightness
                        )
                    )
            }
        }
    }
}