package com.example.kandraw.composables.toolBar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun ToolBarActionItem(
    activeTool: MutableState<String> = mutableStateOf("asd"),
    toolName: String,
    icon: Painter
) {
    Button(
        onClick = { activeTool.value = toolName },
        modifier = Modifier.clip(CircleShape),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            if (activeTool.value == toolName) MaterialTheme.colorScheme.onPrimaryContainer
            else Color.Transparent
        )
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(32.dp),
            contentDescription = toolName,
            tint = if (activeTool.value == toolName) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}