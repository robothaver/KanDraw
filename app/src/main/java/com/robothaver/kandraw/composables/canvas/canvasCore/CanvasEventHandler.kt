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
        selectedPosition.value = offset
        isTouchEventActive.value = true
        when (activeTool.value) {
            Tools.Eraser -> {
                canvasController.eraseSelectedPath(getOffset(offset))
            }

            Tools.ColorPicker -> {
                val color = canvasController.getSelectedPathColor(getOffset(offset), offset)
                setSelectedColor(canvasController, color)
            }

            Tools.Pen -> {
                canvasController.addNewPath(getOffset(offset))
            }

            else -> Unit
        }
    }

    fun drag(change: Offset, previousPosition: Offset) {
        when (activeTool.value) {
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
                viewPortPosition.value += change - previousPosition
                canvasController.getVisiblePaths()
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

        private fun setSelectedColor(canvasController: CanvasController, selectedColor: Color) {
            canvasController.penSettings.value = canvasController.penSettings.value.copy(
                customColor = selectedColor,
            )
            setColorToCustom(canvasController.penSettings, selectedColor, true)
        }

        private fun getOffset(newPoint: Offset): Offset {
            return Offset(
                newPoint.x - viewPortPosition.value.x,
                newPoint.y - viewPortPosition.value.y
            )
        }
    }