package com.robothaver.kandraw.domain.canvasController

import android.content.ContentResolver
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.IntSize
import com.robothaver.kandraw.domain.canvasController.penEffect.getPenEffect
import com.robothaver.kandraw.viewModel.CanvasViewModel
import com.robothaver.kandraw.viewModel.data.Actions
import com.robothaver.kandraw.viewModel.data.PathData


class CanvasController(
    canvasViewModel: CanvasViewModel,
    private val contentResolver: ContentResolver,
    val visiblePaths: SnapshotStateList<PathData>,
    val imageSaver: ImageSaver
) {
    private val maxUndoSteps = 64
    private val undoPaths = canvasViewModel.undoPaths
    private val redoPaths = canvasViewModel.redoPaths
    private val allPathBackup = canvasViewModel.allPathBackup
    private val bitmapProcessor = BitmapProcessor()
    val backgroundImage = canvasViewModel.backgroundImage
    val allPaths = canvasViewModel.allPaths
    val penSettings = canvasViewModel.penSettings
    val backgroundColor = canvasViewModel.backgroundColor
    val eraserWidth = canvasViewModel.eraserWidth
    val gridSettings = canvasViewModel.gridSettings

    // Fix visible paths!!!!!

    fun resizeBitmap(screenSize: IntSize) {
        backgroundImage.value = backgroundImage.value.copy(
            image = bitmapProcessor.resizeBitmap(
                backgroundImage = backgroundImage.value,
                newSize = bitmapProcessor.getNewBitmapSize(backgroundImage.value, screenSize)
            )
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
        visiblePaths.last().path.lineTo(newPoint.x, newPoint.y)
        visiblePaths[visiblePaths.lastIndex] =
            visiblePaths.last().copy(points = getNewPoints(newPoint))
        allPaths[allPaths.lastIndex] = visiblePaths.last()
        undoPaths[undoPaths.lastIndex] = visiblePaths.last()
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


    fun getSelectedPathColor(selectedPos: Offset): Color {
        val imgColor = bitmapProcessor.getColorFromBitmap(
            backgroundImage.value,
            selectedPos
        )
        val path = getSelectedPath(selectedPos, 20f)
        return when {
            path != null -> path.color
            imgColor != null -> imgColor
            else -> backgroundColor.value.color
        }
    }

    fun processBackground(uri: Uri?) {
        if (uri != null) {
            val source = ImageDecoder.createSource(contentResolver, uri)
            val bitmap = ImageDecoder.decodeBitmap(source) { imageDecoder, _, _ ->
                imageDecoder.isMutableRequired = true
            }
            backgroundImage.value = backgroundImage.value.copy(
                image = bitmap,
                originalSize = IntSize(bitmap.width, bitmap.height)
            )
        }
    }

    fun getBackground(singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun addVisiblePaths(newVisiblePaths: List<PathData>) {
        visiblePaths.clear()
        visiblePaths.addAll(newVisiblePaths)
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
                visiblePaths.add(undoPaths.last().index, undoPaths.last())
            } else {
                allPaths.removeLast()
                visiblePaths.removeLast()
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
                visiblePaths.removeAt(redoPaths.last().index)
            } else {
                allPaths.add(redoPaths.last())
                visiblePaths.add(redoPaths.last())
            }
            redoPaths.removeLast()
        }
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