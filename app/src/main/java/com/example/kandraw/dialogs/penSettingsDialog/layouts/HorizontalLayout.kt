package com.example.kandraw.dialogs.penSettingsDialog.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kandraw.composables.alphaSlider.AlphaSlider
import com.example.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.example.kandraw.dialogs.penSettingsDialog.composables.ColorOptions
import com.example.kandraw.dialogs.penSettingsDialog.composables.PenCapSettings
import com.example.kandraw.dialogs.penSettingsDialog.composables.penEffect.PenEffectOptions
import com.example.kandraw.utils.penEffect.getShape
import com.example.kandraw.viewModel.Effects
import com.example.kandraw.viewModel.PenSettings
import kotlin.math.roundToInt

@Composable
fun HorizontalLayout(
    penSettings: MutableState<PenSettings>,
    isColorPickingPage: MutableState<Boolean>
) {
    val leftScrollState = rememberScrollState()
    val rightScrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(0.85f)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(leftScrollState)
                .fillMaxWidth()
                .weight(1f)

        ) {
            PenCapSettings(penSettings = penSettings)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            PenEffectOptions(penSettings = penSettings)
        }
        VerticalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Column(
            modifier = Modifier
                .verticalScroll(rightScrollState)
                .fillMaxWidth()
                .weight(1f)
        ) {
            ColorOptions(
                penSettings = penSettings,
                isColorPickingPage = isColorPickingPage,
                32.dp,
                18.sp
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            AlphaSlider(
                alpha = penSettings.value.alpha,
                color = penSettings.value.penColor.hue,
                height = 32.dp,
                selectorSize = 32.dp
            ) {
                penSettings.value = penSettings.value.copy(alpha = it)
            }

            ColorBrightnessSlider(
                penColor = penSettings.value.penColor,
                height = 32.dp,
                selectorSize = 32.dp
            ) {
                updateColor(penSettings, brightness = it)
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 6.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )

            Title(
                "Width: ${penSettings.value.strokeWidth.roundToInt()}",
                fontSize = 18.sp,
                padding = 0.dp
            )
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
        }
    }
}