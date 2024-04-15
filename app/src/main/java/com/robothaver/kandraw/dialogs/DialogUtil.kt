package com.robothaver.kandraw.dialogs

import androidx.compose.ui.geometry.Size
import com.robothaver.kandraw.utils.windowInfo.WindowType

fun getDialogSize(screenWidthInfo: WindowType, screenHeightInfo: WindowType): Size {
    return when {
        // Tablet horizontal
        screenWidthInfo == WindowType.Expanded && screenHeightInfo == WindowType.Medium ->
            Size(0.85f, 0.6f)
        // Tablet vertical
        screenWidthInfo == WindowType.Medium && screenHeightInfo == WindowType.Expanded ->
            Size(0.85f, 0.45f)
        else ->
            Size(0.85f, 0.9f)
    }
}