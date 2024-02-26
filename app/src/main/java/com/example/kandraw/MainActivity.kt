package com.example.kandraw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kandraw.composables.canvas.CanvasController
import com.example.kandraw.composables.canvas.MainCanvas
import com.example.kandraw.composables.canvas.PathData
import com.example.kandraw.composables.toolBar.ToolBar
import com.example.kandraw.dialogs.penSettingsDialog.PenSettingsDialog
import com.example.kandraw.ui.theme.CanvasTestTheme
import com.example.kandraw.utils.windowInfo.getWindowInfo
import com.example.kandraw.viewModel.CanvasViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanvasTestTheme(dynamicColor = false) {
                val windowInfo = getWindowInfo()
                val viewModel = viewModel<CanvasViewModel>()
                val isEditing = remember { mutableStateOf(false) }
                val canvasController = CanvasController(
                    viewModel
                )

//                viewModel.setInitial(1..10000)

                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        Text(text = "All paths: ${viewModel.allPaths.size}", fontSize = 22.sp)
                        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                            MainCanvas(
                                viewModel.backgroundColor,
                                viewModel.activeTool,
                                viewModel.penSettings,
                                viewModel.viewportPosition,
                                canvasController
                            )
                            ToolBar(
                                canvasController,
                                viewModel.activeTool,
                                isEditing,
                                IntSize(constraints.maxWidth, constraints.maxHeight)
                            )
                        }
                        PenSettingsDialog(isEditing, viewModel.penSettings)
                    }
                }
            }
        }
    }
}
