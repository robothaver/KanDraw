package com.robothaver.kandraw.composables.canvas.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun Eraser(selectedPosition: MutableState<Offset>, eraserWidth: MutableFloatState) {
    Box(modifier = Modifier
        .fillMaxSize()
        .drawBehind {
            drawCircle(
                color = Color.Gray,
                radius = eraserWidth.floatValue,
                center = Offset(
                    (selectedPosition.value.x),
                    (selectedPosition.value.y)
                ),
                style = Stroke(
                    width = if (eraserWidth.floatValue > 40) 5f else 2f
                )
            )
        }
    )
}