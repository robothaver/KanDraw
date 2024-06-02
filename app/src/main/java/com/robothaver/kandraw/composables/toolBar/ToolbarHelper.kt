package com.robothaver.kandraw.composables.toolBar

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

fun isVisible(
    isVisible: Boolean,
    hideOnDraw: Boolean,
    isTouchEventActive: Boolean
): Boolean {
    return when {
        isVisible && hideOnDraw && isTouchEventActive -> false
        else -> isVisible
    }
}

fun setPositionToDefault(
    isHorizontal: Boolean,
    position: MutableState<Offset>,
    parentSize: IntSize,
    toolbarSize: IntSize
) {
    position.value = if (isHorizontal) {
        Offset(
            (parentSize.width / 2f) - (toolbarSize.width / 2),
            parentSize.height - toolbarSize.height.toFloat() - 25f
        )
    } else {
        Offset(
            25f,
            (parentSize.height / 2f) - (toolbarSize.height / 2)
        )
    }
}

fun moveToolbar(
    position: MutableState<Offset>,
    offset: Offset,
    parentSize: IntSize,
    toolbarSize: IntSize
) {
    position.value = Offset(
        (position.value.x + offset.x).coerceIn(
            0f,
            parentSize.width - toolbarSize.width.toFloat()
        ),
        (position.value.y + offset.y).coerceIn(
            0f,
            parentSize.height - toolbarSize.height.toFloat()
        )
    )
}