package com.robothaver.kandraw.dialogs.eraserSettingsDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.Title
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.utils.windowInfo.WindowInfo
import com.robothaver.kandraw.utils.windowInfo.WindowType
import kotlin.math.roundToInt

@Composable
fun EraserSettingsDialog(
    eraserWidth: MutableState<Float>,
    windowInfo: WindowInfo,
    canvasController: CanvasController
) {
    Column(modifier = Modifier.fillMaxWidth(if (windowInfo.screenWidthInfo == WindowType.Compact) 0.8f else 0.6f)) {
        Title(text = "Eraser width: ${eraserWidth.value.roundToInt()}")
        Slider(
            value = eraserWidth.value,
            onValueChange = {eraserWidth.value = it},
            valueRange = 10f..200f,
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        )
        Button(onClick = { canvasController.clearCanvas() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Delete, null)
                Text(text = "Clear canvas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}