package com.robothaver.kandraw.composables.customColorPicker.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.robothaver.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.robothaver.kandraw.viewModel.data.PenColor

@Composable
fun HorizontalColorPicker(
    colorPickerController: ColorPickerController,
    penColor: PenColor,
    initialColor: Color,
    onColorSelected: (newColor: Color) -> Unit,
    onBrightnessChanged: (newBrightness: Float) -> Unit
) {
    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ColorBrightnessSlider(
                penColor = penColor
            ) {
                onBrightnessChanged(it)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VerticalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .padding(10.dp),
                controller = colorPickerController,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    if (colorEnvelope.fromUser) {
                        onColorSelected(colorEnvelope.color)
                    }
                },
                initialColor = initialColor
            )
        }
    }
}