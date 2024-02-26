package com.example.kandraw.composables.toolBar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.example.kandraw.composables.canvas.CanvasController
import kotlin.math.roundToInt


@Composable
fun ToolBar(
    canvasController: CanvasController,
    activeTool: MutableState<String>,
    isDialogVisible: MutableState<Boolean>,
    parentSize: IntSize
) {
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize(0, 0)) }
    val color = MaterialTheme.colorScheme.onPrimaryContainer

    val toolBarItems = listOf(
        ToolBarItem(
            Icons.Filled.ArrowBack,
            onClick = { canvasController.undo() },
            tint = color,
            enabled = canvasController.undoPaths.isNotEmpty()
        ),
        ToolBarItem(
            Icons.Filled.ArrowForward,
            onClick = { canvasController.redo() },
            tint = color,
            enabled = canvasController.redoPaths.isNotEmpty()
        ),
        ToolBarItem(
            Icons.Filled.Menu,
            onClick = { },
            tint = color,
            enabled = true
        )
    )
    Row(
        modifier = Modifier
            .onSizeChanged {
                offsetX.floatValue = (parentSize.width / 2f) - (it.width / 2)
                offsetY.floatValue = parentSize.height - it.height.toFloat() - 25f
                size = IntSize(it.width, it.height)
            }
            .offset { IntOffset(offsetX.floatValue.roundToInt(), offsetY.floatValue.roundToInt()) }
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f))
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
        Button(
            onClick = {
                if (activeTool.value == "pen") {
                    isDialogVisible.value = true
                } else {
                    activeTool.value = "pen"
                }
            },
            modifier = Modifier.clip(CircleShape),
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                if (activeTool.value == "pen") {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else Color.Transparent
            )
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.penbg),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = canvasController.penSettings.value.penColor.color
                )
                Icon(
                    painter = painterResource(id = R.drawable.penoutline),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (activeTool.value == "pen") {
                        MaterialTheme.colorScheme.primaryContainer
                    } else Color.Black
                )
            }
        }

        ToolBarActionItem(
            activeTool = activeTool,
            toolName = "eraser",
            painterResource(id = R.drawable.eraser_solid)
        )

        ToolBarActionItem(
            activeTool = activeTool,
            toolName = "mover",
            painterResource(id = R.drawable.mover)
        )

        for (toolBarItem in toolBarItems) {
            Button(
                onClick = { toolBarItem.onClick() },
                modifier = Modifier.clip(CircleShape),
                contentPadding = PaddingValues(12.dp),
                colors = ButtonDefaults.buttonColors(
                    Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f),
                    contentColor = toolBarItem.tint
                ),
                enabled = toolBarItem.enabled
            ) {
                Icon(
                    toolBarItem.icon,
                    modifier = Modifier.size(32.dp),
                    contentDescription = toolBarItem.icon.toString(),
                )
            }
        }
    }
}