package com.robothaver.kandraw.domain.canvasController

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import com.robothaver.kandraw.viewModel.data.ImageSaveOptions
import dev.shreyaspatil.capturable.controller.CaptureController
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ImageSaver(
    private val captureController: CaptureController,
    val activity: Activity
) {
    private val contentResolver = activity.contentResolver

    fun isNewApi(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.P
    }

    private fun getContentValues(createAlbum: Boolean, image: Bitmap): ContentValues {
        val now = LocalDateTime.now()
        val fileName =
            "KanDraw_${now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))}.png"
        return ContentValues().apply {
            if (createAlbum) {
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/KanDraw"
                )
            }
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.HEIGHT, image.height)
            put(MediaStore.Images.Media.WIDTH, image.width)
        }
    }

    suspend fun saveImage(
        saveOptions: ImageSaveOptions,
        backgroundColor: Color
    ) {
        // Capturing the canvas into bitmap
        val image =
            createImage(if (saveOptions.transparentBackground) Color.Transparent else backgroundColor)

        // Creating URI
        val imageCollection = apiLevel29orUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = getContentValues(saveOptions.createAlbum, image)

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

    fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getPermissions(launcher: ManagedActivityResultLauncher<String, Boolean>) {
        launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    @OptIn(ExperimentalComposeApi::class)
    private suspend fun createImage(backgroundColor: Color): Bitmap {
        val screen = captureController.captureAsync().await().asAndroidBitmap()
            .copy(Bitmap.Config.ARGB_8888, true)

        return if (backgroundColor == Color.Transparent) {
            screen
        } else {
            setBitmapBackgroundColor(screen, backgroundColor)
        }
    }

    private fun setBitmapBackgroundColor(bitmap: Bitmap, backgroundColor: Color): Bitmap {
        val newBitmap = Bitmap.createBitmap(
            bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(newBitmap)
        canvas.drawColor(backgroundColor.toArgb())
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        return newBitmap
    }

    private fun <T> apiLevel29orUp(onApiLevel29: () -> T): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            onApiLevel29()
        } else null
    }
}