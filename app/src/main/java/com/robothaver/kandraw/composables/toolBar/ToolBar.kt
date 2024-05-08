package com.robothaver.kandraw.composables.toolBar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.composables.toolBar.composables.PenItem
import com.robothaver.kandraw.composables.toolBar.composables.ToolBarActionItem
import com.robothaver.kandraw.composables.toolBar.composables.ToolBarItem
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.Tools
import kotlin.math.roundToInt


@Composable
fun ToolBar(
    canvasController: CanvasController,
    activeTool: MutableState<Tools>,
    selectedDialog: MutableState<Dialogs>,
    parentSize: MutableState<IntSize>,
    undoPaths: Boolean,
    redoPaths: Boolean
) {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize(0, 0)) }
    val hasBeenPositioned = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .onGloballyPositioned {
                if (!hasBeenPositioned.value) {
                    offsetX.floatValue = (parentSize.value.width / 2f) - (it.size.width / 2)
                    offsetY.floatValue = parentSize.value.height - it.size.height.toFloat() - 25f
                    size = IntSize(it.size.width, it.size.height)
                    hasBeenPositioned.value = true
                }
            }
            .offset { IntOffset(offsetX.floatValue.roundToInt(), offsetY.floatValue.roundToInt()) }
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
            .padding(6.dp)
            .pointerInput(true) {
                detectDragGestures { _, offset ->
                    offsetX.floatValue = (offsetX.floatValue + offset.x).coerceIn(
                        0f,
                        parentSize.value.width - size.width.toFloat()
                    )
                    offsetY.floatValue = (offsetY.floatValue + offset.y).coerceIn(
                        0f,
                        parentSize.value.height - size.height.toFloat()
                    )
                }
            },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        PenItem(
            activeTool = activeTool,
            selectedDialog = selectedDialog,
            canvasController = canvasController
        )
        ToolBarActionItem(
            activeTool = activeTool,
            toolName = Tools.Eraser,
            painterResource(id = R.drawable.eraser_solid)
        ) {
            if (selectedDialog.value == Dialogs.None) {
                if (activeTool.value == it) {
                    selectedDialog.value = Dialogs.EraserSettings
                }
                activeTool.value = it
            }
        }

        ToolBarActionItem(
            activeTool = activeTool,
            toolName = Tools.Mover,
            painterResource(id = R.drawable.mover)
        ) {
            if (selectedDialog.value == Dialogs.None) {
                activeTool.value = it
            }
        }

        val undo = remember { { canvasController.undo() } }
        val redo = remember { { canvasController.redo() } }
        ToolBarItem(icon = Icons.AutoMirrored.Filled.ArrowBack, undoPaths, undo)
        ToolBarItem(icon = Icons.AutoMirrored.Filled.ArrowForward, redoPaths, redo)
        ToolBarItem(icon = Icons.Filled.Menu) {
            selectedDialog.value = Dialogs.Preferences
        }
    }
}
