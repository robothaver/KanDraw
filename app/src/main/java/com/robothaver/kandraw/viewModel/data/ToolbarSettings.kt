package com.robothaver.kandraw.viewModel.data

data class ToolbarSettings(
    val isVisible: Boolean = true,
    val hideOnDraw: Boolean = false,
    val isHorizontal: Boolean = true,
    val isPinned: Boolean = false,
    val size: ToolbarSize = ToolbarSize.Medium
)

enum class ToolbarSize {
    Small,
    Medium,
    Large
}
