package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.composables.customColorPicker.CustomColorPicker
import com.robothaver.kandraw.utils.changeColorBrightness.changeColorBrightness
import com.robothaver.kandraw.viewModel.CanvasViewModel

@Composable
fun CustomColorSelector(
    viewModel: CanvasViewModel,
    selectedData: ColorPickerData,
    onExit: () -> Unit
) {
    val customColorSelectorHandler =
        CustomColorSelectorHandler(viewModel = viewModel, selectedData = selectedData)
    Column(modifier = Modifier.fillMaxSize()) {
        CustomColorPicker(
            modifier = Modifier.size(500.dp),
            initialColor = selectedData.color.hue,
            penColor = selectedData.color,
            onDismiss = {
                onExit()
            },
            onBrightnessChanged = { brightness ->
                customColorSelectorHandler.updateBrightness(brightness)
            },
            onColorPickerActivated = {

            }
        ) { newColor ->
            val brightness = viewModel.backgroundColor.value.brightness
            val updatedColor = changeColorBrightness(newColor, brightness)
            customColorSelectorHandler.updateColor(updatedColor)
        }

    }
}


class CustomColorSelectorHandler(
    private val viewModel: CanvasViewModel,
    private val selectedData: ColorPickerData
) {
    private val gridSettings = viewModel.gridSettings.value
    private val backgroundColor = viewModel.backgroundColor.value
    fun updateBrightness(brightness: Float) {
        when (selectedData.id) {
            ColorPickerIds.SmallGridColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        smallCellColor = gridSettings.smallCellColor.copy(
                            color = changeColorBrightness(
                                gridSettings.smallCellColor.color,
                                brightness
                            ),
                            brightness = brightness
                        )
                    )
            }

            ColorPickerIds.LargeGridColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        largeCellColor = gridSettings.largeCellColor.copy(
                            color = changeColorBrightness(
                                gridSettings.largeCellColor.color,
                                brightness
                            ),
                            brightness = brightness
                        )
                    )
            }

            ColorPickerIds.BackgroundColor -> {
                viewModel.backgroundColor.value =
                    backgroundColor.copy(
                        color = changeColorBrightness(backgroundColor.color, brightness),
                        brightness = brightness
                    )
            }
        }
    }

    fun updateColor(updatedColor: Color) {
        when (selectedData.id) {
            ColorPickerIds.SmallGridColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        smallCellColor = gridSettings.smallCellColor.copy(
                            hue = updatedColor,
                            color = updatedColor
                        )
                    )
            }

            ColorPickerIds.LargeGridColor -> {
                viewModel.gridSettings.value =
                    gridSettings.copy(
                        largeCellColor = gridSettings.largeCellColor.copy(
                            hue = updatedColor,
                            color = updatedColor
                        )
                    )
            }

            ColorPickerIds.BackgroundColor -> {
                viewModel.backgroundColor.value =
                    backgroundColor.copy(
                        hue = updatedColor,
                        color = updatedColor
                    )
            }
        }
    }
}
