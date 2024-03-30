package com.example.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.kandraw.domain.canvasController.CanvasController
import com.example.kandraw.utils.changeColorBrightness.changeColorBrightness
import com.example.kandraw.viewModel.Tools

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
                canvasController.eraseSelectedPath(getOffset(offset), 20f)
            }

            Tools.ColorPicker -> {
                selectedPosition.value = offset
                val color = canvasController.getSelectedPathColor(getOffset(offset))
                setSelectedColor(canvasController, color)
                activeTool.value = Tools.Pen
            }

            Tools.Pen -> {
                canvasController.addNewPath(getOffset(offset), true)
            }

            else -> Unit
        }
    }

    fun dragStart(offset: Offset) {
        if (activeTool.value == Tools.Pen) {
            canvasController.addNewPath(
                getOffset(offset)
            )
        } else if (activeTool.value == Tools.Eraser || activeTool.value == Tools.ColorPicker) {
            isTouchEventActive.value = true
        }
    }

    fun drag(change: Offset, offset: Offset) {
        when (activeTool.value) {
            Tools.Eraser -> {
                selectedPosition.value = change
                canvasController.eraseSelectedPath(getOffset(change), 20f)
            }

            Tools.ColorPicker -> {
                selectedPosition.value = change
                val color = canvasController.getSelectedPathColor(getOffset(change))
                setSelectedColor(canvasController, color)
            }

            Tools.Mover -> {
                viewPortPosition.value += offset
            }

            else -> {
                canvasController.expandPath(getOffset(change))
            }
        }
    }

    fun dragEnd() {
        isTouchEventActive.value = false
        if (activeTool.value == Tools.ColorPicker) {
            activeTool.value = Tools.Pen
        }
    }

    private fun setSelectedColor(canvasController: CanvasController, selectedColor: Color?) {
        var newColor = canvasController.backgroundColor
        if (selectedColor != null) {
            newColor = selectedColor
        }
        canvasController.penSettings.value = canvasController.penSettings.value.copy(
            customColor = newColor,
            penColor = canvasController.penSettings.value.penColor.copy(
                color = changeColorBrightness(newColor, canvasController.penSettings.value.penColor.brightness),
                hue = newColor
            )
        )
    }

    private fun getOffset(newPoint: Offset): Offset {
        return Offset(newPoint.x - viewPortPosition.value.x, newPoint.y - viewPortPosition.value.y)
    }
}