package com.robothaver.kandraw

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.ContextCompat
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
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import java.io.IOException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        cutoutMode(enable = true)
        setContent {
            KanDrawTheme(dynamicColor = true, theme = newFilcTheme) {
                val windowManager = WindowManager(window)
                windowManager.hideSystemBars()
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
                val visiblePaths = remember {
                    mutableStateListOf<PathData>()
                }
                val canvasController = CanvasController(viewModel, this.contentResolver, visiblePaths)
                val scope = rememberCoroutineScope()
                val controller = rememberCaptureController()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
//                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
//                        Button(onClick = {
//                            checkPermissions(launcher)
//                        }) {
//                            Text(text = "Get permissions")
//                        }
//                        Button(onClick = {
//                            scope.launch {
//                                viewModel.gridSettings.value =
//                                    viewModel.gridSettings.value.copy(isGridEnabled = false)
//                                saveImage(createImage(controller), "KanDraw")
//                                viewModel.gridSettings.value =
//                                    viewModel.gridSettings.value.copy(isGridEnabled = true)
//                            }
//                        }) {
//                            Text(text = "Save screen")
//                        }
//                        Column {
//                            Text(text = "All paths: ${viewModel.allPaths.size}", color= Color.White)
//                            Text(text = "Visible paths: ${canvasController.visiblePaths.size}", color= Color.White)
//                        }
//
//                    }
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

    private fun <T> apiLevel29orUp(onApiLevel29: () -> T): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onApiLevel29()
        } else null
    }

    private fun saveImage(image: Bitmap, displayName: String) {
        val imageCollection = apiLevel29orUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            apiLevel29orUp {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/KanDraw"
                )
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
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasMinSdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        if (!hasMinSdk && !writePermission) {
            launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    @OptIn(ExperimentalComposeApi::class)
    private suspend fun createImage(controller: CaptureController): Bitmap {
        return controller.captureAsync().await().asAndroidBitmap()
    }

}
