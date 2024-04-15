package com.robothaver.kandraw

import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        cutoutMode(enable = true)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        setWindowSettings(windowInsetsController)
        setContent {
            CanvasTestTheme(dynamicColor = false, theme = dark) {
                val windowInfo = getWindowInfo()
                val viewModel = viewModel<CanvasViewModel>()
                val canvasController = CanvasController(viewModel)
                val containerSize = remember {
                    mutableStateOf(IntSize(0, 0))
                }
                val selectedDialog = remember {
                    mutableStateOf(Dialogs.None)
                }
                val singlePhotoPickerLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                        if (it != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                val source = ImageDecoder.createSource(this.contentResolver, it)
                                viewModel.backgroundImage.value =
                                    ImageDecoder.decodeBitmap(source) { imageDecoder, _, _ ->
                                        imageDecoder.isMutableRequired = true
                                    }
                            } else {
                                viewModel.backgroundImage.value =
                                    MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                            }
                        }
                    }
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        Button(onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Text(text = "Open image")
                        }
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .onSizeChanged { containerSize.value = it }
                        ) {
                            MainCanvas(
                                viewModel.backgroundColor,
                                viewModel.activeTool,
                                viewModel.viewportPosition,
                                canvasController,
                                viewModel.backgroundImage
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
                        )
                    }
                }
            }
        }
    }
}

fun ComponentActivity.cutoutMode(enable: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (enable) {
            window.attributes?.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
        } else {
            window.attributes?.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
        }
    }
}

fun setWindowSettings(windowInsetsController: WindowInsetsControllerCompat) {
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}
