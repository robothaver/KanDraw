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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.composables.toolBar.composables.ToolbarItems
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
    val size = remember { mutableStateOf(IntSize(0, 0)) }
    val settings = toolbarSettings.value
    val position = remember { mutableStateOf(Offset(0f, 0f)) }

    LaunchedEffect(toolbarSettings.value.isHorizontal) {
        setPositionToDefault(settings.isHorizontal, position, parentSize.value, size.value)
    }

    LaunchedEffect(toolbarSettings.value.size) {
        if (toolbarSettings.value.isPinned) {
            setPositionToDefault(settings.isHorizontal, position, parentSize.value, size.value)
        }
    }

    val modifier = Modifier
        .onSizeChanged { size.value = it }
        .offset { IntOffset(position.value.x.roundToInt(), position.value.y.roundToInt()) }
        .clip(RoundedCornerShape(50.dp))
        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
        .padding(if (settings.size != ToolbarSize.Small) 6.dp else 3.dp)
        .pointerInput(true) {
            detectDragGestures { _, offset ->
                if (!toolbarSettings.value.isPinned) {
                    moveToolbar(position, offset, parentSize.value, size.value)
                }
            }
        }

    if (isVisible(settings.isVisible, settings.hideOnDraw, canvasController.touchActive.value)) {
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