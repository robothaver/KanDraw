package com.robothaver.kandraw.composables.canvas.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.domain.canvasController.CanvasController
import kotlin.math.roundToInt


@Composable
fun ColorPickerTool(canvasController: CanvasController, offset: Offset) {
    val size = remember { mutableStateOf(IntSize(0, 0)) }
    Box(modifier = Modifier
        .onGloballyPositioned { size.value = it.size }
        .offset {
            IntOffset(
                (offset.x - size.value.width / 2).roundToInt(),
                (offset.y - size.value.height).roundToInt()
            )
        }) {
        Icon(
            painter = painterResource(id = R.drawable.color_indicator_bg),
            tint = canvasController.penSettings.value.penColor.color,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.color_indicator_outline),
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
    }
}