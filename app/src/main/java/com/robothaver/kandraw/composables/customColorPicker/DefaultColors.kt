package com.robothaver.kandraw.composables.customColorPicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.composables.ColorItem

@Composable
fun DefaultColors(
    colors: List<Color>,
    currentColor: Color,
    size: Dp = 42.dp,
    onColorSelected: (color: Color) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        colors.forEach { color ->
            ColorItem(color = color, currentColor = currentColor, size = size) {
                onColorSelected(color)
            }
        }
    }
}