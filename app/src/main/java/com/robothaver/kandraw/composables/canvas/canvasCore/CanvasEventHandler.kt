package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.composables.customColorPicker.utils.setColorToCustom
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.Tools

class CanvasEventHandler(
    private val selectedPosition: MutableState<Offset>,
    private val isTouchEventActive: MutableState<Boolean>,
    private val activeTool: MutableState<Tools>,
    private val canvasController: CanvasController,
    private val viewPortPosition: MutableState<Offset>
) {
    fun tap(offset: Offset) {
        when (activeTool.value) {
            Tools.Eraser -> {
                selectedPosition.value = offset
                canvasController.eraseSelectedPath(getOffset(offset))
            }

            Tools.ColorPicker -> {
                selectedPosition.value = offset
                val color = canvasController.getSelectedPathColor(getOffset(offset), offset)
                setSelectedColor(canvasController, color)
                activeTool.value = Tools.Pen
            }

            Tools.Pen -> {
                canvasController.addNewPath(getOffset(offset))
            }

            else -> Unit
        }
    }

    fun dragStart(offset: Offset) {
        isTouchEventActive.value = true
        if (activeTool.value == Tools.Pen) {
            canvasController.addNewPath(
                getOffset(offset)
            )
        }
    }

    fun drag(change: Offset, offset: Offset) = when (activeTool.value) {
        Tools.Eraser -> {
            selectedPosition.value = change
            canvasController.eraseSelectedPath(getOffset(change))
        }

        Tools.ColorPicker -> {
            selectedPosition.value = change
            val color = canvasController.getSelectedPathColor(getOffset(change), change)
            setSelectedColor(canvasController, color)
        }

        Tools.Mover -> {
            viewPortPosition.value += offset
        }

        else -> {
            canvasController.expandPath(getOffset(change))
        }
    }

    fun dragEnd() {
        isTouchEventActive.value = false
        if (activeTool.value == Tools.ColorPicker) {
            activeTool.value = Tools.Pen
        }
    }

    private fun setSelectedColor(canvasController: CanvasController, selectedColor: Color) {
        canvasController.penSettings.value = canvasController.penSettings.value.copy(
            customColor = selectedColor,
        )
        setColorToCustom(canvasController.penSettings, selectedColor, true)
    }

    private fun getOffset(newPoint: Offset): Offset {
        return Offset(newPoint.x - viewPortPosition.value.x, newPoint.y - viewPortPosition.value.y)
    }
}