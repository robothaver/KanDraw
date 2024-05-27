package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.viewModel.data.Tools

@Composable
fun ToolBarActionItem(
    activeTool: Tools,
    toolName: Tools,
    icon: Painter,
    size: Dp,
    padding: Dp,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size + 12.dp)
            .clip(CircleShape)
            .background(
                if (activeTool == toolName) {
                    MaterialTheme.colorScheme.primary
                } else Color.Transparent
            )
            .clickable {
                onClick()
            }
    ) {
        Icon(
            painter = icon,
            modifier = Modifier.size(size).padding(padding),
            contentDescription = null,
            tint = if (activeTool == toolName) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.primary
        )
    }
}