package com.robothaver.kandraw.composables.canvas.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Eraser(selectedPosition: MutableState<Offset>) {
    Box(modifier = Modifier
        .offset {
            IntOffset(
                (selectedPosition.value.x).roundToInt(),
                (selectedPosition.value.y).roundToInt()
            )
        }
        .clip(CircleShape)
        .size(32.dp)
        .border(6.dp, shape = CircleShape, color = Color.Gray))
}