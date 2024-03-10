package com.example.kandraw.dialogs.penSettingsDialog.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kandraw.composables.alphaSlider.AlphaSlider
import com.example.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.example.kandraw.dialogs.penSettingsDialog.composables.ColorOptions
import com.example.kandraw.dialogs.penSettingsDialog.composables.PenCapSettings
import com.example.kandraw.dialogs.penSettingsDialog.composables.penEffect.PenEffectOptions
import com.example.kandraw.utils.changeColorBrightness.changeColorBrightness
import com.example.kandraw.utils.penEffect.getShape
import com.example.kandraw.viewModel.Effects
import com.example.kandraw.viewModel.PenEffectSettings
import com.example.kandraw.viewModel.PenSettings
import kotlin.math.roundToInt

@Composable
fun VerticalSettingsLayout(
    penSettings: MutableState<PenSettings>,
    isColorPickingPage: MutableState<Boolean>
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxHeight(0.55f)
            .fillMaxWidth(0.8f)
            .verticalScroll(scrollState)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
            .padding(16.dp)
    ) {
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
        Slider(
            value = penSettings.value.strokeWidth,
            onValueChange = {
                if (penSettings.value.penEffectSettings.effect == Effects.Stamped) {
                    updateEffect(
                        penSettings, penSettings.value.penEffectSettings.copy(
                            shape = getShape(
                                penSettings.value.penEffectSettings.selectedShape,
                                penSettings.value.strokeWidth
                            )
                        )
                    )
                }
                penSettings.value = penSettings.value.copy(strokeWidth = it)
            },
            valueRange = 1f..300f,
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )

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

@Composable
fun Title(text: String, fontSize: TextUnit = 22.sp, fontWeight: FontWeight = FontWeight.Bold, padding: Dp = 3.dp) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = padding)
    )
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

