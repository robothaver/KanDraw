package com.robothaver.kandraw.dialogs.penSettingsDialog.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.composables.alphaSlider.AlphaSlider
import com.robothaver.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.ColorOptions
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.PenCapSettings
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.PenSizeSlider
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.Title
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.penEffect.PenEffectOptions
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.updateColor
import com.robothaver.kandraw.viewModel.data.PenSettings
import kotlin.math.roundToInt

@Composable
fun VerticalLayout(
    penSettings: MutableState<PenSettings>,
    isColorPickingPage: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Title("Color")
        ColorOptions(penSettings = penSettings, isColorPickingPage = isColorPickingPage)
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        AlphaSlider(
            alpha = penSettings.value.alpha,
            color = penSettings.value.penColor.hue
        ) {
            penSettings.value = penSettings.value.copy(alpha = it)
        }

        ColorBrightnessSlider(
            penColor = penSettings.value.penColor
        ) {
            updateColor(penSettings, brightness = it)
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Title("Width: ${penSettings.value.strokeWidth.roundToInt()}")
        PenSizeSlider(penSettings = penSettings)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

        Title("Pen cap")
        PenCapSettings(penSettings = penSettings)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        PenEffectOptions(penSettings = penSettings)
    }
}
