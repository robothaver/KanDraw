package com.robothaver.kandraw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robothaver.kandraw.composables.canvas.CanvasPage
import com.robothaver.kandraw.dialogs.DialogManager
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.ui.customThemes.newFilcTheme
import com.robothaver.kandraw.ui.theme.KanDrawTheme
import com.robothaver.kandraw.utils.WindowManager
import com.robothaver.kandraw.utils.windowInfo.getWindowInfo
import com.robothaver.kandraw.viewModel.CanvasViewModel
import com.robothaver.kandraw.viewModel.data.PathData
import dev.shreyaspatil.capturable.controller.rememberCaptureController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KanDrawTheme(dynamicColor = true, theme = newFilcTheme) {
                val windowManager = WindowManager(window)
                windowManager.hideSystemBars()
                val windowInfo = getWindowInfo()
                val viewModel = viewModel<CanvasViewModel>()
                val visiblePaths = remember { mutableStateListOf<PathData>() }
                val captureController = rememberCaptureController()
                val canvasController = CanvasController(
                    canvasViewModel = viewModel,
                    activity =  this,
                    captureController = captureController,
                    visiblePaths = visiblePaths
                )

                CanvasPage(
                    viewModel = viewModel,
                    canvasController = canvasController,
                    captureController = captureController
                )
                DialogManager(
                    viewModel,
                    windowInfo,
                    canvasController,
                    windowManager
                )
            }
        }
    }
}
