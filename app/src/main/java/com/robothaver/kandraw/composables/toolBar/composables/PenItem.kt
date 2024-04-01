package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.Tools

@Composable
fun PenItem(activeTool: MutableState<Tools>, selectedDialog: MutableState<Dialogs>, canvasController: CanvasController) {
    Button(
        onClick = {
            if (activeTool.value == Tools.Pen || activeTool.value == Tools.ColorPicker) {
                selectedDialog.value = Dialogs.PenSettings
                activeTool.value = Tools.Pen
            } else {
                activeTool.value = Tools.Pen
            }
        },
        modifier = Modifier.clip(CircleShape),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            if (activeTool.value == Tools.Pen || activeTool.value == Tools.ColorPicker) {
                MaterialTheme.colorScheme.primary
            } else Color.Transparent
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.pen_bg),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = canvasController.penSettings.value.penColor.color
            )
            Icon(
                painter = painterResource(id = R.drawable.pen_outline),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (activeTool.value == Tools.Pen || activeTool.value == Tools.ColorPicker) {
                    MaterialTheme.colorScheme.onPrimary
                } else Color.Black
            )
        }
    }
}