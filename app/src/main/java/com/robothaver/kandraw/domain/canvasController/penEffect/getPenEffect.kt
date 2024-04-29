package com.robothaver.kandraw.domain.canvasController.penEffect

import androidx.compose.ui.graphics.PathEffect
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.Effects
import com.robothaver.kandraw.viewModel.data.PenSettings

fun getPenEffect(penSettings: PenSettings): PathEffect? {
    return when (penSettings.penEffectSettings.effect) {
        Effects.Default -> null
        Effects.Dashed -> PathEffect.dashPathEffect(
            floatArrayOf(
                penSettings.penEffectSettings.lineLength,
                penSettings.penEffectSettings.spaceBetween
            )
        )

        Effects.Stamped -> PathEffect.stampedPathEffect(
            shape = penSettings.penEffectSettings.shape,
            advance = penSettings.penEffectSettings.spaceBetween,
            phase = 0.01f,
            style = penSettings.penEffectSettings.stampStyle
        )
    }
}