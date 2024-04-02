package com.robothaver.kandraw.composables.canvas.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Eraser(selectedPosition: MutableState<Offset>, eraserWidth: MutableFloatState) {
    val size = remember {
        mutableStateOf(IntSize(0, 0))
    }
    Box(modifier = Modifier
        .onSizeChanged {
            size.value = it
        }
        .offset {
            IntOffset(
                (selectedPosition.value.x - size.value.width / 2).roundToInt(),
                (selectedPosition.value.y - size.value.width / 2).roundToInt()
            )
        }
        .clip(CircleShape)
        .size(eraserWidth.floatValue.dp)
        .border(if (eraserWidth.floatValue > 40) 5.dp else 2.dp, shape = CircleShape, color = Color.Gray))
}