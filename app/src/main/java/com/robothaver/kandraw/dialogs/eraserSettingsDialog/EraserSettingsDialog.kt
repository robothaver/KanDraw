package com.robothaver.kandraw.dialogs.eraserSettingsDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.Title
import com.robothaver.kandraw.utils.windowInfo.WindowInfo
import kotlin.math.roundToInt

@Composable
fun EraserSettingsDialog(eraserWidth: MutableState<Float>, windowInfo: WindowInfo) {
    Column(modifier = Modifier.fillMaxWidth(if (windowInfo.screenWidthInfo == WindowInfo.WindowType.Compact) 0.8f else 0.6f)) {
        Title(text = "Eraser width: ${eraserWidth.value.roundToInt()}")
        Slider(
            value = eraserWidth.value,
            onValueChange = {eraserWidth.value = it},
            valueRange = 1f..300f,
        )
        Button(onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Delete, null)
                Text(text = "Clear canvas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}