package com.robothaver.kandraw.dialogs.penSettingsDialog.composables.penEffect

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.updateEffect
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.Effects
import com.robothaver.kandraw.utils.data.PenSettings

@Composable
fun SelectedPenEffectSettings(penSettings: MutableState<PenSettings>) {
    AnimatedContent(
        targetState = penSettings.value.penEffectSettings.effect,
        transitionSpec = {
            fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right
            )
        }, label = "effect settings"
    ) { effect ->
        when (effect) {
            Effects.Default -> Unit
            Effects.Dashed -> {
                Column {
                    Text(text = "Space between")
                    Slider(
                        value = penSettings.value.penEffectSettings.spaceBetween,
                        onValueChange = {
                            updateEffect(
                                penSettings,
                                penSettings.value.penEffectSettings.copy(
                                    spaceBetween = it
                                )
                            )
                        },
                        valueRange = 1f..200f,
                    )
                    Text(text = "Line length")
                    Slider(
                        value = penSettings.value.penEffectSettings.lineLength,
                        onValueChange = {
                            updateEffect(
                                penSettings,
                                penSettings.value.penEffectSettings.copy(lineLength = it)
                            )
                        },
                        valueRange = 1f..200f,
                    )
                }
            }

            Effects.Stamped -> {
                Column {
                    Text(text = "Line length")
                    Slider(
                        value = penSettings.value.penEffectSettings.spaceBetween,
                        onValueChange = {
                            updateEffect(
                                penSettings,
                                penSettings.value.penEffectSettings.copy(
                                    spaceBetween = it
                                )
                            )
                        },
                        valueRange = 1f..200f,
                    )
                    PenStampShapeChanger(penSettings = penSettings)
                    PenStampStyleChanger(penSettings = penSettings)
                }
            }
        }
    }
}