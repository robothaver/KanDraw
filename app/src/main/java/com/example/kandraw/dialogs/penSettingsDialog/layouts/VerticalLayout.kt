package com.example.kandraw.dialogs.penSettingsDialog.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.kandraw.composables.alphaSlider.AlphaSlider
import com.example.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.example.kandraw.dialogs.penSettingsDialog.composables.ColorOptions
import com.example.kandraw.dialogs.penSettingsDialog.composables.PenCapSettings
import com.example.kandraw.dialogs.penSettingsDialog.composables.PenSizeSlider
import com.example.kandraw.dialogs.penSettingsDialog.composables.Title
import com.example.kandraw.dialogs.penSettingsDialog.composables.penEffect.PenEffectOptions
import com.example.kandraw.utils.changeColorBrightness.changeColorBrightness
import com.example.kandraw.viewModel.PenEffectSettings
import com.example.kandraw.viewModel.PenSettings
import kotlin.math.roundToInt

@Composable
fun VerticalLayout(
    penSettings: MutableState<PenSettings>,
    isColorPickingPage: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.55f)
            .fillMaxWidth(0.8f)
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

fun updateEffect(penSettings: MutableState<PenSettings>, updateSettings: PenEffectSettings) {
    penSettings.value = penSettings.value.copy(penEffectSettings = updateSettings)
}

fun updateColor(
    penSettings: MutableState<PenSettings>,
    newHue: Color = penSettings.value.penColor.hue,
    brightness: Float = penSettings.value.penColor.brightness
) {
    penSettings.value = penSettings.value.copy(
        penColor = penSettings.value.penColor.copy(
            color = changeColorBrightness(newHue, brightness),
            hue = newHue,
            brightness = brightness
        )
    )
}

