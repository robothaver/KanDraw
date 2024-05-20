package com.robothaver.kandraw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robothaver.kandraw.composables.canvas.CanvasPage
import com.robothaver.kandraw.dialogs.DialogManager
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.domain.canvasController.ImageSaver
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
                val controller = rememberCaptureController()
                val imageSaver = ImageSaver(controller, this)
                val canvasController = CanvasController(viewModel, this.contentResolver, visiblePaths, imageSaver)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    CanvasPage(
                        viewModel = viewModel,
                        canvasController = canvasController,
                        captureController = controller
                    )
                    DialogManager(
                        viewModel.selectedDialog,
                        viewModel,
                        windowInfo,
                        canvasController,
                        windowManager
                    )
                }
            }
        }
    }
}
