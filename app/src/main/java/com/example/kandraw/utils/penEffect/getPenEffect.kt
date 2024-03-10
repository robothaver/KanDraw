package com.example.kandraw.utils.penEffect

import androidx.compose.ui.graphics.PathEffect
import com.example.kandraw.viewModel.PenSettings
import com.example.kandraw.viewModel.Effects

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
            phase = 0f,
            style = penSettings.penEffectSettings.stampStyle
        )
    }
}