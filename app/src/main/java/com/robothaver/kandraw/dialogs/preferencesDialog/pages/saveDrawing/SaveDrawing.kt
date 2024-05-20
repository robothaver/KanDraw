package com.robothaver.kandraw.dialogs.preferencesDialog.pages.saveDrawing

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.dialogs.PermissionDialog
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.CheckboxWithText
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.PreferencesBody
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.ImageSaveOptions
import kotlinx.coroutines.launch

@Composable
fun SaveDrawing(
    saveOptions: MutableState<ImageSaveOptions>,
    canvasController: CanvasController,
    onExit: () -> Unit
) {
    val options = saveOptions.value
    val scope = rememberCoroutineScope()
    val showDialog = remember {
        mutableStateOf(false)
    }
    val hasAsked = remember {
        mutableStateOf(false)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scope.launch {
                canvasController.imageSaver.saveImage(options)
            }
        } else {
            showDialog.value = true
            hasAsked.value = true
        }
    }
    PreferencesBody(currentRoute = Screen.SaveDrawing.route, onExit = onExit) {
        Button(
            onClick = {
                scope.launch {
                    if (!canvasController.imageSaver.isNewApi() && !canvasController.imageSaver.hasPermission()) {
                        showDialog.value = true
                    } else {
                        canvasController.imageSaver.saveImage(options)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.save_drawing),
                null,
                modifier = Modifier.size(28.dp)
            )
            Text(text = "Save drawing")
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

        CheckboxWithText(
            isChecked = options.includeBackgroundImage,
            title = "Include background image",
            description = "The background image will be visible in the picture"
        ) {
            saveOptions.value = options.copy(
                includeBackgroundImage = it
            )
        }

        CheckboxWithText(
            isChecked = options.includeGrid,
            title = "Include grid",
            description = "The grid will be visible in the picture"
        ) {
            saveOptions.value = options.copy(
                includeGrid = it
            )
        }

        CheckboxWithText(
            isChecked = options.transparentBackground,
            title = "Transparent background",
            description = "The background color will be transparent in the final image"
        ) {
            saveOptions.value = options.copy(
                transparentBackground = it
            )
        }
        if (canvasController.imageSaver.isNewApi()) {
            CheckboxWithText(
                isChecked = options.createAlbum,
                title = "Create album",
                description = "The app will create an album and save the photo there"
            ) {
                saveOptions.value = options.copy(
                    createAlbum = it
                )
            }
        }
    }

    if (showDialog.value) {
        PermissionDialog(
            hasAsked = hasAsked.value,
            onDismiss = {
                showDialog.value = false
            },
            onOkClick = {
                showDialog.value = false
                canvasController.imageSaver.getPermissions(launcher)
            },
            onGoToAppSettingsClick = {
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", canvasController.imageSaver.activity.packageName, null)
                ).also(canvasController.imageSaver.activity::startActivity)
            }
        )
    }
}
