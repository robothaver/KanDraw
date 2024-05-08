package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.composables.customColorPicker.utils.changeColorBrightness
import com.robothaver.kandraw.viewModel.data.GridSettings
import com.robothaver.kandraw.viewModel.data.Tools

class CanvasEventHandler(
    private val selectedPosition: MutableState<Offset>,
    private val isTouchEventActive: MutableState<Boolean>,
    private val activeTool: MutableState<Tools>,
    private val canvasController: CanvasController,
    private val viewPortPosition: MutableState<Offset>,
    private val gridSettings: MutableState<GridSettings>
) {
    fun tap(offset: Offset) {
        when (activeTool.value) {
            Tools.Eraser -> {
                selectedPosition.value = offset
                canvasController.eraseSelectedPath(getOffset(offset))
            }

            Tools.ColorPicker -> {
                selectedPosition.value = offset
                val color = canvasController.getSelectedPathColor(getOffset(offset))
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
        if (activeTool.value == Tools.Pen) {
            canvasController.addNewPath(
                getOffset(offset)
            )
        } else if (activeTool.value == Tools.Eraser || activeTool.value == Tools.ColorPicker) {
            isTouchEventActive.value = true
        }
    }

    fun drag(change: Offset, offset: Offset) = when (activeTool.value) {
        Tools.Eraser -> {
            selectedPosition.value = change
            canvasController.eraseSelectedPath(getOffset(change))
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

    fun dragEnd() {
        isTouchEventActive.value = false
    }

    private fun setSelectedColor(canvasController: CanvasController, selectedColor: Color) {
        canvasController.penSettings.value = canvasController.penSettings.value.copy(
            customColor = selectedColor,
            penColor = canvasController.penSettings.value.penColor.copy(
                color = changeColorBrightness(
                    selectedColor,
                    canvasController.penSettings.value.penColor.brightness
                ),
                hue = selectedColor
            )
        )
    }

    private fun getOffset(newPoint: Offset): Offset {
        return Offset(newPoint.x - viewPortPosition.value.x, newPoint.y - viewPortPosition.value.y)
    }
}