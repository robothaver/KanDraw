package com.example.kandraw.utils.penEffect

import androidx.compose.ui.graphics.PathEffect
import com.example.kandraw.viewModel.Effects
import com.example.kandraw.viewModel.PenSettings

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