package com.robothaver.kandraw.composables.toolBar.composables

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ToolBarItem(
    icon: ImageVector,
    enabled: Boolean = true,
    size: Dp,
    padding: Dp,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size + 12.dp)
            .clip(CircleShape)
            .clickable(enabled = enabled) {
                onClick()
            }
    ) {
        Icon(
            icon,
            modifier = Modifier
                .size(size)
                .padding(padding),
            contentDescription = null,
            tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(
                alpha = 0.2f
            )
        )
    }
}