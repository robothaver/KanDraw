package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.viewModel.data.Tools

@Composable
fun ToolBarActionItem(
    activeTool: MutableState<Tools>,
    toolName: Tools,
    icon: Painter,
    onClick: (toolName: Tools) -> Unit
) {

    Button(
        onClick = { onClick(toolName) },
        modifier = Modifier.clip(CircleShape),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            if (activeTool.value == toolName) MaterialTheme.colorScheme.primary
            else Color.Transparent
        )
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(32.dp),
            contentDescription = null,
            tint = if (activeTool.value == toolName) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.primary
        )
    }
}