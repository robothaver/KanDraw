package com.robothaver.kandraw.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.robothaver.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.robothaver.kandraw.utils.windowInfo.WindowInfo
import com.robothaver.kandraw.viewModel.PenColor

@Composable
fun CustomColorPicker(
    initialColor: Color,
    penColor: PenColor,
    layout: WindowInfo.WindowType = WindowInfo.WindowType.Compact,
    onDismiss: () -> Unit,
    onBrightnessChanged: (brightness: Float) -> Unit,
    onColorPickerActivated: () -> Unit,
    onColorSelected: (selectedColor: Color) -> Unit
) {
    val colorPickerController = rememberColorPickerController()
    val color = remember { mutableStateOf(initialColor) }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        onDismiss()
                    }
            )
            Icon(
                painterResource(id = R.drawable.eye_dropper),
                null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .clickable {
                        onColorPickerActivated()
                    }
            )
        }
        if (layout == WindowInfo.WindowType.Compact) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp),
                    controller = colorPickerController,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        onColorSelected(colorEnvelope.color)
                    },
                    initialColor = color.value
                )

                Spacer(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f))

                ColorBrightnessSlider(
                    penColor = penColor
                ) {
                    onBrightnessChanged(it)
                }
            }
        } else {
            Row {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                )
                {
                    ColorBrightnessSlider(
                        penColor = penColor
                    ) {
                        onBrightnessChanged(it)
                    }
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    VerticalDivider(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(285.dp)
                            .padding(10.dp),
                        controller = colorPickerController,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            onColorSelected(colorEnvelope.color)
                        },
                        initialColor = color.value
                    )
                }
            }
        }
    }
}