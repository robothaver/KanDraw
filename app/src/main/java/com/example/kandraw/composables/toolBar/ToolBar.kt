package com.example.kandraw.composables.toolBar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.kandraw.composables.toolBar.composables.ToolBarItem
import com.example.kandraw.composables.toolBar.composables.ToolbarActionItems
import com.example.kandraw.domain.canvasController.CanvasController
import com.example.kandraw.viewModel.Tools
import kotlin.math.roundToInt


@Composable
fun ToolBar(
    canvasController: CanvasController,
    activeTool: MutableState<Tools>,
    isDialogVisible: MutableState<Boolean>,
    parentSize: IntSize,
    undoPaths: Boolean,
    redoPaths: Boolean
) {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize(0, 0)) }


    Row(
        modifier = Modifier
            .onSizeChanged {
                offsetX.floatValue = (parentSize.width / 2f) - (it.width / 2)
                offsetY.floatValue = parentSize.height - it.height.toFloat() - 25f
                size = IntSize(it.width, it.height)
            }
            .offset { IntOffset(offsetX.floatValue.roundToInt(), offsetY.floatValue.roundToInt()) }
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
            .padding(6.dp)
            .pointerInput(true) {
                detectDragGestures { _, offset ->
                    offsetX.floatValue = (offsetX.floatValue + offset.x).coerceIn(
                        0f,
                        parentSize.width - size.width.toFloat()
                    )
                    offsetY.floatValue = (offsetY.floatValue + offset.y).coerceIn(
                        0f,
                        parentSize.height - size.height.toFloat()
                    )
                }
            },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ToolbarActionItems(activeTool = activeTool, isDialogVisible, canvasController)

        val undo = remember { { canvasController.undo() } }
        val redo = remember { { canvasController.redo() } }
        ToolBarItem(icon = Icons.Filled.ArrowBack, undoPaths, undo)
        ToolBarItem(icon = Icons.Filled.ArrowForward, redoPaths, redo)
        ToolBarItem(icon = Icons.Filled.Menu) {}
    }
}
