package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun CellSettings(
    title: String,
    cellSize: Float,
    cellStrokeWidth: Float,
    cellColor: Color,
    sizeRange: ClosedFloatingPointRange<Float>,
    strokeWidthRange: ClosedFloatingPointRange<Float> = 5f..50f,
    onColorChange: () -> Unit,
    onSizeChange: (size: Float) -> Unit,
    onStrokeWidthChange: (width: Float) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(12.dp)
        ) {
            Text(
                text = "Cell size: ${cellSize.roundToInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = cellSize,
                onValueChange = {
                    onSizeChange(it)
                },
                valueRange = sizeRange
            )

            Text(
                text = "Stroke width: ${cellStrokeWidth.roundToInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = cellStrokeWidth,
                onValueChange = {
                    onStrokeWidthChange(it)
                },
                valueRange = strokeWidthRange,
                steps = 8
            )

            ColorSelector(color = cellColor, title = "Cell color") {
                onColorChange()
            }
        }
    }
}