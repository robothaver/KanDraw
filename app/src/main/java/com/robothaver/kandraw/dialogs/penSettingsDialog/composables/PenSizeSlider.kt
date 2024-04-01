package com.robothaver.kandraw.dialogs.penSettingsDialog.composables

import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.robothaver.kandraw.dialogs.penSettingsDialog.layouts.updateEffect
import com.robothaver.kandraw.utils.penEffect.getShape
import com.robothaver.kandraw.viewModel.Effects
import com.robothaver.kandraw.viewModel.PenSettings

@Composable
fun PenSizeSlider(penSettings: MutableState<PenSettings>) {
    Slider(
        value = penSettings.value.strokeWidth,
        onValueChange = {
            penSettings.value = penSettings.value.copy(strokeWidth = it)
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
        },
        valueRange = 1f..300f,
    )
}