package com.robothaver.kandraw.dialogs

import androidx.compose.ui.geometry.Size
import com.robothaver.kandraw.utils.windowInfo.WindowType

fun getDialogSize(screenWidthInfo: WindowType, screenHeightInfo: WindowType): Size {
    println("${screenWidthInfo.name} ${screenHeightInfo.name}")
    return when {
        // Mobile medium vertical layout
        screenWidthInfo == WindowType.Compact && screenHeightInfo == WindowType.Medium ->
            Size(0.8f, 0.55f)
        // Mobile expanded vertical layout
        screenWidthInfo == WindowType.Compact && screenHeightInfo == WindowType.Expanded ->
            Size(0.8f, 0.55f)
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