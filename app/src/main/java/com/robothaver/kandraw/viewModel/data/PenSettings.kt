package com.robothaver.kandraw.viewModel.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.Effects
import com.robothaver.kandraw.domain.canvasController.penEffect.Shapes
import com.robothaver.kandraw.domain.canvasController.penEffect.getShape
import com.robothaver.kandraw.utils.penDefaultColors

data class PenSettings(
    val penColor: PenColor = PenColor(color = penDefaultColors[0]),
    val cap: StrokeCap = StrokeCap.Round,
    val strokeWidth: Float = 10f,
    val alpha: Float = 1f,
    val penEffectSettings: PenEffectSettings = PenEffectSettings(),
    val customColor: Color = Color(0xFFF4538A)
)

data class PenEffectSettings(
    val spaceBetween: Float = 50f,
    val lineLength: Float = 70f,
    val stampStyle: StampedPathEffectStyle = StampedPathEffectStyle.Rotate,
    val selectedShape: Shapes = Shapes.Triangle,
    val shape: Path = getShape(selectedShape, 10f),
    val effect: Effects = Effects.Default
)

