package com.robothaver.kandraw.dialogs.penSettingsDialog.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.composables.ColorItem
import com.robothaver.kandraw.dialogs.penSettingsDialog.layouts.updateColor
import com.robothaver.kandraw.viewModel.PenSettings
import com.robothaver.kandraw.viewModel.defaultColors

@Composable
fun ColorOptions(
    penSettings: MutableState<PenSettings>,
    isColorPickingPage: MutableState<Boolean>,
    size: Dp = 42.dp
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0..4) {
            ColorItem(color = defaultColors[i], penSettings.value.penColor.hue, size) {
                updateColor(penSettings, newHue = defaultColors[i])
            }
        }
    }
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 5..8) {
            ColorItem(color = defaultColors[i], penSettings.value.penColor.hue, size) {
                updateColor(penSettings, newHue = defaultColors[i])
            }
        }
        CustomColorButton(penSettings, size) {
            isColorPickingPage.value = !isColorPickingPage.value
        }

    }
}