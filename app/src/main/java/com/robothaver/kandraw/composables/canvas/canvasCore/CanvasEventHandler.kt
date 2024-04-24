package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.utils.changeColorBrightness.changeColorBrightness
import com.robothaver.kandraw.utils.data.Tools

class CanvasEventHandler(
    private val selectedPosition: MutableState<Offset>,
    private val isTouchEventActive: MutableState<Boolean>,
    private val activeTool: MutableState<Tools>,
    private val canvasController: CanvasController,
    private val viewPortPosition: MutableState<Offset>,
    private val gridOffset: MutableState<Offset>
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
            val cellSize = 80 * 5
            viewPortPosition.value += offset
            when {
                gridOffset.value.x + offset.x >= cellSize -> {
                    gridOffset.value = gridOffset.value.copy(x = 0f)
                }
                gridOffset.value.x + offset.x <= -cellSize -> {
                    gridOffset.value = gridOffset.value.copy(x = 0f)
                }
                gridOffset.value.y + offset.y >= cellSize -> {
                    gridOffset.value = gridOffset.value.copy(y = 0f)
                }
                gridOffset.value.y + offset.y <= -cellSize -> {
                    gridOffset.value = gridOffset.value.copy(y = 0f)
                }
                else -> {
                    gridOffset.value += offset
                }
            }
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