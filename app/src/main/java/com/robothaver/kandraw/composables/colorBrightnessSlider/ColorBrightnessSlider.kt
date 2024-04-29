package com.robothaver.kandraw.composables.colorBrightnessSlider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.viewModel.data.PenColor
import kotlin.math.roundToInt

/**
 * @param penColor the object that contains the hue and the brightness.
 * @param padding the padding to be applied to the widget.
 * @param selectorColor the color of the selector.
 * @param selectorSize the size of the selector.
 * @param widthFraction sets the widgets size to the fraction of the max width.
 * @param height the height of the widget.
 * @param borderRadius the radius of the borders.
 * @param borderWidth the width of the borders.
 * @param onValueChanged callback in which value should be updated.
 */
@Composable
fun ColorBrightnessSlider(
    penColor: PenColor,
    padding: PaddingValues = PaddingValues(vertical = 6.dp),
    selectorColor: Color = Color.White,
    selectorSize: Dp = 40.dp,
    widthFraction: Float = 1f,
    height: Dp = 50.dp,
    borderRadius: Dp = 15.dp,
    borderWidth: Dp = 2.dp,
    onValueChanged: (Float) -> Unit,
) {
    val selectorPosition = remember { mutableFloatStateOf(0f) }
    var selectorWidth by remember { mutableStateOf(0.dp) }
    val progress = remember { mutableFloatStateOf(penColor.brightness) }
    BoxWithConstraints(modifier = Modifier
        .padding(padding)
        .fillMaxWidth(widthFraction)
        .clip(RoundedCornerShape(borderRadius))
        .border(borderWidth, Color.White, shape = RoundedCornerShape(borderRadius))
        .padding(borderWidth)
        .height(height)
        .onSizeChanged {
            selectorPosition.floatValue = (it.width - selectorWidth.value) * penColor.brightness
        }
        .background(
            brush = Brush.horizontalGradient(colors = listOf(Color.Black, penColor.hue))
        )
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                selectorPosition.floatValue =
                    (change.position.x - dragAmount.x).coerceIn(
                        0f,
                        size.width - selectorWidth.value
                    )
                progress.floatValue =
                    getProgress(selectorPosition.floatValue, size.width, selectorWidth)
                println(progress.floatValue)
                onValueChanged(progress.floatValue)
            }
        }
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                selectorPosition.floatValue = (offset.x - selectorWidth.value / 2).coerceIn(
                    0f,
                    size.width - selectorWidth.value
                )
                progress.floatValue =
                    getProgress(selectorPosition.floatValue, size.width, selectorWidth)
                onValueChanged(progress.floatValue)
            }
        }, contentAlignment = Alignment.CenterStart
    )
    {
        Box(
            modifier = Modifier
                .offset { IntOffset(selectorPosition.floatValue.roundToInt(), 0) }
                .onSizeChanged { selectorWidth = it.width.dp }
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        selectorPosition.floatValue =
                            (selectorPosition.floatValue + dragAmount.x).coerceIn(
                                0f,
                                constraints.maxWidth - size.width.toFloat()
                            )
                        progress.floatValue = getProgress(
                            selectorPosition.floatValue,
                            constraints.maxWidth,
                            selectorWidth
                        )
                        onValueChanged(progress.floatValue)
                    }
                }
                .size(selectorSize)
                .clip(CircleShape)
                .background(selectorColor)
        )
    }
}

fun getProgress(selectorPosition: Float, maxWidth: Int, selectorSize: Dp): Float {
    return selectorPosition / (maxWidth - selectorSize.value)
}
