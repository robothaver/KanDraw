package com.example.kandraw.dialogs.penSettingsDialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kandraw.viewModel.Tools
import com.example.kandraw.viewModel.PenSettings
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun PenSettingsDialog(
    isVisible: MutableState<Boolean>,
    penSettings: MutableState<PenSettings>,
    activeTool: MutableState<Tools>
) {
    if (!isVisible.value) return
    Dialog(onDismissRequest = { isVisible.value = false }) {
        val colorPickerController = rememberColorPickerController()
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                .padding(16.dp)
        ) {
            VerticalSettingsLayout(colorPickerController, penSettings, activeTool, isVisible)
        }
    }
}
