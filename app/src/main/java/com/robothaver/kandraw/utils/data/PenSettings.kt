package com.robothaver.kandraw.utils.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.Effects
import com.robothaver.kandraw.domain.canvasController.penEffect.Shapes
import com.robothaver.kandraw.domain.canvasController.penEffect.getShape

data class PenSettings(
    val penColor: PenColor = PenColor(defaultColors[0], 1f, defaultColors[0]),
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

val defaultColors = listOf(
    Color(0xFFE80101),
    Color(0xFFFE2738),
    Color(0xFFFE8101),
    Color(0xFFFEBE01),
    Color(0xFF3C4EBD),
    Color(0xFF019DEC),
    Color(0xFF9B01B0),
    Color(0xFF00BB72),
    Color(0xFF00823A),
)
