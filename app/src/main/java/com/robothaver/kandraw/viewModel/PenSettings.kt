package com.robothaver.kandraw.viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.StrokeCap
import com.robothaver.kandraw.utils.penEffect.getShape

data class PenSettings(
    val penColor: PenColor = PenColor(Color.Red, 1f, Color.Red),
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

val secondaryDefaultColors = listOf(
    Color(0xFFFF204E),
    Color(0xFFA0153E),
    Color(0xFF5D0E41),
    Color(0xFF00224D),
    Color(0xFF711DB0),
    Color(0xFFC21292),
    Color(0xFFEF4040),
    Color(0xFFFFA732),
    Color(0xFFE4F9F5),
    Color(0xFF30E3CA),
    Color(0xFF11999E),
    Color(0xFF40514E),

)
