package com.robothaver.kandraw.composables.toolBar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.composables.toolBar.composables.OptionButtons
import com.robothaver.kandraw.composables.toolBar.composables.Tools
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.ToolbarSettings
import com.robothaver.kandraw.viewModel.data.ToolbarSize
import com.robothaver.kandraw.viewModel.data.Tools
import kotlin.math.roundToInt


@Composable
fun ToolBar(
    canvasController: CanvasController,
    toolbarSettings: MutableState<ToolbarSettings>,
    activeTool: MutableState<Tools>,
    selectedDialog: MutableState<Dialogs>,
    parentSize: MutableState<IntSize>
) {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    val size = remember { mutableStateOf(IntSize(0, 0)) }
    val hasBeenPositioned = remember { mutableStateOf(false) }
    val settings = toolbarSettings.value

    LaunchedEffect(toolbarSettings.value.isHorizontal) {
        size.value = IntSize(0, 0)
        hasBeenPositioned.value = false
    }

    val modifier = Modifier
        .onGloballyPositioned {
            if (!hasBeenPositioned.value) {
                if (settings.isHorizontal) {
                    offsetX.floatValue = (parentSize.value.width / 2f) - (it.size.width / 2)
                    offsetY.floatValue = parentSize.value.height - it.size.height.toFloat() - 25f
                } else {
                    offsetX.floatValue = 25f
                    offsetY.floatValue = (parentSize.value.height / 2f) - (it.size.height / 2)
                }
                size.value = IntSize(it.size.width, it.size.height)
                hasBeenPositioned.value = true
            }
        }
        .offset { IntOffset(offsetX.floatValue.roundToInt(), offsetY.floatValue.roundToInt()) }
        .clip(RoundedCornerShape(50.dp))
        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
        .padding(if (settings.size != ToolbarSize.Small) 6.dp else 3.dp)
        .pointerInput(true) {
            detectDragGestures { _, offset ->
                if (!toolbarSettings.value.isPinned) {
                    offsetX.floatValue = (offsetX.floatValue + offset.x).coerceIn(
                        0f,
                        parentSize.value.width - size.value.width.toFloat()
                    )
                    offsetY.floatValue = (offsetY.floatValue + offset.y).coerceIn(
                        0f,
                        parentSize.value.height - size.value.height.toFloat()
                    )
                }
            }
        }

    if (isVisible(
            settings.isVisible,
            settings.hideOnDraw,
            canvasController.isTouchEventActive.value
        )
    ) {
        if (settings.isHorizontal) {
            Row(
                modifier = modifier
            ) {
                ToolbarItems(activeTool, canvasController, selectedDialog, settings.size)
            }
        } else {
            Column(
                modifier = modifier,
            ) {
                ToolbarItems(activeTool, canvasController, selectedDialog, settings.size)
            }
        }
    }
}

@Composable
private fun ToolbarItems(
    activeTool: MutableState<Tools>,
    canvasController: CanvasController,
    selectedDialog: MutableState<Dialogs>,
    toolbarSize: ToolbarSize
) {
    val size = when (toolbarSize) {
        ToolbarSize.Small -> 32.dp
        ToolbarSize.Medium -> 46.dp
        ToolbarSize.Large -> 52.dp
    }
    val padding = when (toolbarSize) {
        ToolbarSize.Small -> 3.dp
        ToolbarSize.Medium -> 6.dp
        ToolbarSize.Large -> 6.dp
    }
    Tools(activeTool, canvasController, selectedDialog, size, padding)
    OptionButtons(canvasController, selectedDialog, size, padding)
}

private fun isVisible(
    isVisible: Boolean,
    hideOnDraw: Boolean,
    isTouchEventActive: Boolean
): Boolean {
    return when {
        isVisible && hideOnDraw && isTouchEventActive -> false
        else -> isVisible
    }
}
