package com.robothaver.kandraw.composables.customColorPicker.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.robothaver.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.robothaver.kandraw.composables.customColorPicker.DefaultColors
import com.robothaver.kandraw.composables.customColorPicker.utils.getColorBrightness
import com.robothaver.kandraw.composables.customColorPicker.utils.getPeakColorHue
import com.robothaver.kandraw.viewModel.data.PenColor

@Composable
fun VerticalColorPicker(
    colorPickerController: ColorPickerController,
    penColor: PenColor,
    initialColor: Color,
    defaultColors: List<Color>?,
    onColorSelected: (newColor: Color) -> Unit,
    onBrightnessChanged: (newBrightness: Float) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .weight(1f),
            controller = colorPickerController,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                println(colorPickerController.selectedColor.value)
                if (colorEnvelope.fromUser) {
                    onColorSelected(colorEnvelope.color)
                }
            },
            initialColor = initialColor
        )
        ColorBrightnessSlider(
            penColor = penColor
        ) {
            onBrightnessChanged(it)
        }

        if (defaultColors != null) {
            DefaultColors(colors = defaultColors, currentColor = penColor.color) { color ->
                colorPickerController.selectByColor(color, true)
                onColorSelected(getPeakColorHue(color))
                onBrightnessChanged(getColorBrightness(color))
            }
        }
    }
}