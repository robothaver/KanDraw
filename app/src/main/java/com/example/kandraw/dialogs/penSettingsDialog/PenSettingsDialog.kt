package com.example.kandraw.dialogs.penSettingsDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kandraw.viewModel.PenSettings
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun PenSettingsDialog(
    isVisible: MutableState<Boolean>,
    penSettings: MutableState<PenSettings>,
) {
    if (!isVisible.value) return
    val colorPickerController = rememberColorPickerController()
    Dialog(onDismissRequest = { isVisible.value = false }) {
        val color = remember {
            mutableStateOf(Color(penSettings.value.color.toArgb()))
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = colorPickerController,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    penSettings.value = penSettings.value.copy(
                        alpha = colorEnvelope.color.alpha,
                        color = colorEnvelope.color
                    )
                },
                initialColor = color.value
            )
            AlphaSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = colorPickerController,
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = colorPickerController,
            )
        }
    }
}
