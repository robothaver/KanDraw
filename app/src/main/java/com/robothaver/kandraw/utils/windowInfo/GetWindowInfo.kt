package com.robothaver.kandraw.utils.windowInfo

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun getWindowInfo(): WindowInfo {
    val configuration = LocalConfiguration.current
    return WindowInfo(
        screenWidthInfo = when {
            configuration.screenWidthDp < 600 -> WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowType.Medium
            else -> WindowType.Expanded
        },
        screenHeightInfo = when {
            configuration.screenHeightDp < 480 -> WindowType.Compact
            configuration.screenHeightDp < 900 -> WindowType.Medium
            else -> WindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}