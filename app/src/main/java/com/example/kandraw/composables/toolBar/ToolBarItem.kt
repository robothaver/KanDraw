package com.example.kandraw.composables.toolBar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ToolBarItem(
    val icon: ImageVector,
    val onClick: () -> Unit,
    val tint: Color,
    val enabled: Boolean
)