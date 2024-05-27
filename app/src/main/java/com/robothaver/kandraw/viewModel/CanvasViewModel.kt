package com.robothaver.kandraw.viewModel

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.viewModel.data.GridSettings
import com.robothaver.kandraw.viewModel.data.ImageSaveOptions
import com.robothaver.kandraw.viewModel.data.PathData
import com.robothaver.kandraw.viewModel.data.PenColor
import com.robothaver.kandraw.viewModel.data.PenSettings
import com.robothaver.kandraw.viewModel.data.ToolbarSettings
import com.robothaver.kandraw.viewModel.data.Tools
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage

class CanvasViewModel : ViewModel() {
    // Canvas
    val allPaths = mutableStateListOf<PathData>()
    val allPathBackup = mutableStateListOf<List<PathData>>()
    val undoPaths = mutableStateListOf<PathData>()
    val redoPaths = mutableStateListOf<PathData>()
    val penSettings = mutableStateOf(PenSettings())
    val activeTool = mutableStateOf(Tools.Pen)
    val eraserWidth = mutableFloatStateOf(20f)
    val viewportPosition = mutableStateOf(Offset(0f, 0f))
    val backgroundColor = mutableStateOf(
        PenColor(color = Color(0xFF1E1E1E), hue = Color.White, brightness = 0.12f)
    )

    // Dialogs
    val selectedDialog = mutableStateOf(Dialogs.None)

    // Settings
    val backgroundImage = mutableStateOf(BackgroundImage())
    val imageSaveOptions = mutableStateOf(ImageSaveOptions())
    val gridSettings = mutableStateOf(GridSettings())
    val toolbarSettings = mutableStateOf(ToolbarSettings())
}
