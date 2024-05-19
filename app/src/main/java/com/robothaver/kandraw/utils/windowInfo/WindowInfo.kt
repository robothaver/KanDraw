package com.robothaver.kandraw.utils.windowInfo

import androidx.compose.ui.unit.IntSize

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenSize: IntSize
) {
}

enum class WindowType {
    Compact,
    Medium,
    Expanded
}