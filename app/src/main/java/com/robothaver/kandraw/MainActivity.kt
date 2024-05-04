package com.robothaver.kandraw

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robothaver.kandraw.composables.canvas.MainCanvas
import com.robothaver.kandraw.composables.toolBar.ToolBar
import com.robothaver.kandraw.dialogs.DialogManager
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.ui.customThemes.reFilc
import com.robothaver.kandraw.ui.theme.CanvasTestTheme
import com.robothaver.kandraw.utils.windowInfo.getWindowInfo
import com.robothaver.kandraw.viewModel.CanvasViewModel
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch
import java.io.IOException


class MainActivity : ComponentActivity() {
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        cutoutMode(enable = true)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        setWindowSettings(windowInsetsController)
        setContent {
            CanvasTestTheme(dynamicColor = true, theme = reFilc) {
                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
                        println("PERMISSION GRANTED")

                    } else {
                        println("PERMISSION DENIED")
                    }
                }
                val windowInfo = getWindowInfo()
                val viewModel = viewModel<CanvasViewModel>()
                val canvasController = CanvasController(viewModel, this.contentResolver)
                val containerSize = remember {
                    mutableStateOf(IntSize(0, 0))
                }
                val scope = rememberCoroutineScope()
                val controller = rememberCaptureController()
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(innerPadding)
                    ) {
                        Row {
                            Button(onClick = {
                                checkPermissions(launcher)
                            }) {
                                Text(text = "Get permissions")
                            }
                            Button(onClick = {
                                scope.launch {
                                    viewModel.gridSettings.value = viewModel.gridSettings.value.copy(isGridEnabled = false)
                                    saveImage(createImage(controller), "KanDraw")
                                    viewModel.gridSettings.value = viewModel.gridSettings.value.copy(isGridEnabled = true)
                                }
                            }) {
                                Text(text = "Save screen")
                            }
                        }

                        Box(modifier = Modifier
                            .fillMaxSize()
                            .onSizeChanged { containerSize.value = it }
                        ) {
                            MainCanvas(
                                viewModel.activeTool,
                                viewModel.viewportPosition,
                                canvasController,
                                viewModel.backgroundImage,
                                controller,
                            )
                            ToolBar(
                                canvasController,
                                viewModel.activeTool,
                                viewModel.selectedDialog,
                                containerSize,
                                viewModel.undoPaths.isNotEmpty(),
                                viewModel.redoPaths.isNotEmpty()
                            )
                        }
                        DialogManager(
                            viewModel.selectedDialog,
                            viewModel,
                            windowInfo,
                            canvasController
                        ) { setWindowSettings(windowInsetsController) }
                    }
                }
            }
        }
    }

    fun <T> apiLevel29orUp(onApiLevel29: () -> T): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onApiLevel29()
        } else null
    }

    fun saveImage(image: Bitmap, displayName: String) {
        val imageCollection = apiLevel29orUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            apiLevel29orUp {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/KanDraw")
            }
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.png")
            put(MediaStore.Images.Media.HEIGHT, image.height)
            put(MediaStore.Images.Media.WIDTH, image.width)
        }

        try {
            contentResolver.insert(imageCollection, contentValues)?.also {
                contentResolver.openOutputStream(it).use { outputStream ->
                    if (!image.compress(Bitmap.CompressFormat.PNG, 95, outputStream!!)) {
                        throw IOException("Couldn't save image")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
        } catch (e: IOException) {
            println(e)
        }
    }


    private fun checkPermissions(launcher: ManagedActivityResultLauncher<String, Boolean>) {
        // Use LocalContext.current
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasMinSdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        val permissionToRequest = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (!hasMinSdk) {
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    @OptIn(ExperimentalComposeApi::class)
    private suspend fun createImage(controller: CaptureController): Bitmap {
        return controller.captureAsync().await().asAndroidBitmap()
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
