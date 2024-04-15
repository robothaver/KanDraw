package com.robothaver.kandraw.utils.windowInfo

import androidx.compose.ui.unit.Dp

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
}

enum class WindowType {
    Compact,
    Medium,
    Expanded
}