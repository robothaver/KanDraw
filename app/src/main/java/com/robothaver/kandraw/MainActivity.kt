package com.robothaver.kandraw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robothaver.kandraw.composables.canvas.MainCanvas
import com.robothaver.kandraw.composables.toolBar.ToolBar
import com.robothaver.kandraw.dialogs.DialogManager
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.ui.customThemes.dark
import com.robothaver.kandraw.ui.theme.CanvasTestTheme
import com.robothaver.kandraw.utils.windowInfo.getWindowInfo
import com.robothaver.kandraw.viewModel.CanvasViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTestTheme(dynamicColor = false, theme = dark) {
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                setWindowSettings(windowInsetsController)

                val windowInfo = getWindowInfo()
                val viewModel = viewModel<CanvasViewModel>()
                val canvasController = CanvasController(viewModel)
                val containerSize = remember {
                    mutableStateOf(IntSize(0, 0))
                }
                val selectedDialog = remember {
                    mutableStateOf<Dialogs>(Dialogs.None)
                }

                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
//                            Spacer(modifier = Modifier.padding(vertical = 32.dp))
//                            Text(text = "undos: ${viewModel.undoPaths.size.toString()}")
//                            Text(text = "clears: ${viewModel.allPathBackup.size.toString()}")
//                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .onSizeChanged { containerSize.value = it }
                        ) {
                            MainCanvas(
                                viewModel.backgroundColor,
                                viewModel.activeTool,
                                viewModel.viewportPosition,
                                canvasController
                            )
                            ToolBar(
                                canvasController,
                                viewModel.activeTool,
                                selectedDialog,
                                containerSize,
                                viewModel.undoPaths.isNotEmpty(),
                                viewModel.redoPaths.isNotEmpty()
                            )
                        }
                        DialogManager(
                            selectedDialog,
                            viewModel,
                            windowInfo,
                            canvasController
                        ) { setWindowSettings(windowInsetsController) }
                    }
                }
            }
        }
    }
}

fun setWindowSettings(windowInsetsController: WindowInsetsControllerCompat) {
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}
