package com.robothaver.kandraw.domain.canvasController

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import com.robothaver.kandraw.composables.canvas.canvasCore.PathProcessor
import com.robothaver.kandraw.domain.canvasController.penEffect.getPenEffect
import com.robothaver.kandraw.viewModel.CanvasViewModel
import com.robothaver.kandraw.viewModel.data.Actions
import com.robothaver.kandraw.viewModel.data.PathData
import dev.shreyaspatil.capturable.controller.CaptureController


class CanvasController(
    canvasViewModel: CanvasViewModel,
    activity: Activity,
    captureController: CaptureController,
    val visiblePaths: SnapshotStateList<PathData>
) {
    private val maxUndoSteps = 64
    private val allPathBackup = canvasViewModel.allPathBackup
    private val contentResolver = activity.contentResolver
    private val bitmapProcessor = BitmapProcessor()
    private val saveOptions = canvasViewModel.imageSaveOptions
    private val viewportPosition = canvasViewModel.viewportPosition
    private val allPaths = canvasViewModel.allPaths
    val undoPaths = canvasViewModel.undoPaths
    val redoPaths = canvasViewModel.redoPaths
    val touchActive = mutableStateOf(false)
    val imageSaver = ImageSaver(captureController, activity)
    val canvasSize = mutableStateOf(IntSize(0, 0))
    val backgroundImage = canvasViewModel.backgroundImage
    val penSettings = canvasViewModel.penSettings
    val backgroundColor = canvasViewModel.backgroundColor
    val eraserWidth = canvasViewModel.eraserWidth
    val gridSettings = canvasViewModel.gridSettings

    fun getVisiblePaths() {
        // Find the paths currently visible on the screen
        visiblePaths.clear()
        visiblePaths.addAll(
            PathProcessor(allPaths, viewportPosition.value, canvasSize.value).processPaths()
        )
    }

    fun resizeBitmap() {
        backgroundImage.value = backgroundImage.value.copy(
            image = getResizedBitmap(backgroundImage.value.originalImage!!)
        )
    }

    fun addNewPath(newPoint: Offset) {
        val newPathData = createNewPathData(newPoint)
        allPaths.add(newPathData)
        visiblePaths.add(newPathData)
        addUndoStep(newPathData)
        redoPaths.clear()
    }

    fun expandPath(newPoint: Offset) {
        if (allPaths.isNotEmpty()) {
            visiblePaths.last().path.lineTo(newPoint.x, newPoint.y)
            visiblePaths[visiblePaths.lastIndex] =
                visiblePaths.last().copy(points = getNewPoints(newPoint))
            allPaths[allPaths.lastIndex] = visiblePaths.last()
            undoPaths[undoPaths.lastIndex] = visiblePaths.last()
        }
    }

    fun eraseSelectedPath(currentlySelectedPosition: Offset) {
        val selectedPath =
            getSelectedPath(currentlySelectedPosition, eraserWidth.floatValue)
        if (selectedPath != null) {
            addUndoStep(
                selectedPath.copy(
                    action = Actions.Erase, index = visiblePaths.indexOf(selectedPath)
                )
            )
            redoPaths.clear()
            allPaths.remove(selectedPath)
            visiblePaths.remove(selectedPath)
        }
    }

    fun clearCanvas() {
        if (allPaths.isNotEmpty()) {
            val newBackup = mutableListOf<PathData>()
            newBackup.addAll(allPaths)
            allPathBackup.addAll(listOf(newBackup))
            addUndoStep(
                allPaths.last().copy(action = Actions.Clear, index = allPathBackup.lastIndex)
            )
            visiblePaths.clear()
            allPaths.clear()
            redoPaths.clear()
        }
    }

    suspend fun saveDrawing() {
        val wasBgImageEnabled = backgroundImage.value.isVisible
        val wasGridEnabled = gridSettings.value.isGridEnabled

        toggleElements(isBgImageVisible = false, isGirdVisible = false)
        imageSaver.saveImage(saveOptions.value, backgroundColor.value.color)
        toggleElements(isBgImageVisible = wasBgImageEnabled, isGirdVisible = wasGridEnabled)
    }

    /**
     * @param globalPosition the selected position with global offset.
     * @param localPosition the selected point on the screen without the global offset.
     */
    fun getSelectedPathColor(globalPosition: Offset, localPosition: Offset): Color {
        val path = getSelectedPath(globalPosition, 20f)
        if (path != null) {
            return path.color
        }
        val imgColor = bitmapProcessor.getColorFromBitmap(
            backgroundImage.value,
            if (backgroundImage.value.stickToBackground) localPosition else globalPosition
        )
        if (imgColor != null) {
            return imgColor
        }
        return backgroundColor.value.color
    }

    fun loadBackgroundImage(uri: Uri?) {
        if (uri != null) {
            val bitmap = bitmapProcessor.createBitmapFromUri(uri, contentResolver)
            backgroundImage.value = backgroundImage.value.copy(
                image = getResizedBitmap(bitmap),
                originalImage = bitmap
            )
        }
    }

    fun undo() {
        if (undoPaths.isNotEmpty()) {
            redoPaths.add(undoPaths.last())
            if (undoPaths.last().action == Actions.Clear && allPathBackup.isNotEmpty()) {
                try {
                    allPaths.addAll(allPathBackup[undoPaths.last().index])
                } catch (e: IndexOutOfBoundsException) {
                    allPaths.addAll(allPathBackup[undoPaths.last().index - 1])
                }
            } else if (undoPaths.last().action == Actions.Erase) {
                allPaths.add(undoPaths.last().index, undoPaths.last())
                if (visiblePaths.isNotEmpty()) {
                    visiblePaths.add(undoPaths.last().index, undoPaths.last())
                }
            } else {
                allPaths.removeLast()
                if (visiblePaths.isNotEmpty()) {
                    visiblePaths.removeLast()
                }
            }
            undoPaths.removeLast()
        }
    }

    fun redo() {
        if (redoPaths.isNotEmpty()) {
            undoPaths.add(redoPaths.last())
            if (redoPaths.last().action == Actions.Clear && allPathBackup.isNotEmpty()) {
                try {
                    allPaths.removeAll(allPathBackup[redoPaths.last().index])
                    visiblePaths.removeAll(allPathBackup[redoPaths.last().index])
                } catch (e: IndexOutOfBoundsException) {
                    allPaths.removeAll(allPathBackup.last())
                    visiblePaths.removeAll(allPathBackup.last())
                }

            } else if (redoPaths.last().action == Actions.Erase) {
                allPaths.removeAt(redoPaths.last().index)
                if (visiblePaths.isNotEmpty() || redoPaths.last().index != visiblePaths.lastIndex) {
                    visiblePaths.removeAt(redoPaths.last().index)
                }
            } else {
                allPaths.add(redoPaths.last())
                visiblePaths.add(redoPaths.last())
            }
            redoPaths.removeLast()
        }
    }

    private fun toggleElements(isBgImageVisible: Boolean, isGirdVisible: Boolean) {
        if (!saveOptions.value.includeBackgroundImage) {
            backgroundImage.value = backgroundImage.value.copy(
                isVisible = isBgImageVisible
            )
        }
        if (!saveOptions.value.includeGrid) {
            gridSettings.value = gridSettings.value.copy(
                isGridEnabled = isGirdVisible
            )
        }
    }

    private fun getResizedBitmap(bitmap: Bitmap): Bitmap {
        return bitmapProcessor.resizeBitmap(
            bitmap = bitmap,
            newSize = bitmapProcessor.getNewBitmapSize(
                backgroundImage.value.scaleMode,
                canvasSize.value,
                IntSize(bitmap.width, bitmap.height)
            )
        )
    }

    private fun createNewPathData(newPoint: Offset): PathData {
        val newPath = Path()
        newPath.moveTo(newPoint.x, newPoint.y)
        newPath.lineTo(newPoint.x, newPoint.y)
        return PathData(
            path = newPath,
            points = listOf(newPoint),
            color = penSettings.value.penColor.color,
            cap = penSettings.value.cap,
            strokeWidth = penSettings.value.strokeWidth,
            alpha = penSettings.value.alpha,
            style = getPenEffect(penSettings.value)
        )
    }

    private fun getNewPoints(newPoint: Offset): MutableList<Offset> {
        val newPoints = mutableListOf<Offset>()
        newPoints.addAll(allPaths.last().points)
        newPoints.add(newPoint)
        return newPoints
    }

    private fun addUndoStep(path: PathData) {
        if (undoPaths.size == maxUndoSteps) {
            if (undoPaths.first().action == Actions.Clear) {
                try {
                    allPathBackup.removeAt(undoPaths.first().index)
                } catch (_: IndexOutOfBoundsException) {
                    allPathBackup.removeFirst()
                }
                val newUndos = mutableListOf<PathData>()
                undoPaths.forEach {
                    if (it.action == Actions.Clear) {
                        newUndos.add(it.copy(index = it.index - 1))
                    } else {
                        newUndos.add(it)
                    }
                }
                undoPaths.clear()
                undoPaths.addAll(newUndos)
            }
            undoPaths.removeFirst()
        }
        undoPaths.add(path)
    }

    private fun getSelectedPath(
        circle: Offset, radius: Float
    ): PathData? {
        for (path in visiblePaths.reversed()) {
            for (index in 0..path.points.lastIndex) {
                val currentPoint = path.points[index]
                if (index != path.points.lastIndex) {
                    val nextPoint = path.points[index + 1]
                    val collision = PathIntersectionChecker().lineCircle(
                        currentPoint.x,
                        currentPoint.y,
                        nextPoint.x,
                        nextPoint.y,
                        circle.x,
                        circle.y,
                        path.strokeWidth / 2 + radius
                    )
                    if (collision) return path
                } else {
                    if (PathIntersectionChecker().pointCircle(
                            circle.x,
                            circle.y,
                            currentPoint.x,
                            currentPoint.y,
                            path.strokeWidth / 2 + radius
                        )
                    ) {
                        return path
                    }
                }
            }
        }
        return null
    }
}