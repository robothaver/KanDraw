package com.example.kandraw.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.kandraw.composables.canvas.PathData

class CanvasViewModel : ViewModel() {
    var allPaths = mutableStateListOf<PathData>()
    var undoPaths = mutableStateListOf<PathData>()
    var redoPaths = mutableStateListOf<PathData>()
    var penSettings = mutableStateOf(PenSettings())
    var activeTool = mutableStateOf("pen")
    var viewportPosition = mutableStateOf(Offset(0f, 0f))
    var backgroundColor by mutableStateOf(Color.DarkGray)
        private set
}
