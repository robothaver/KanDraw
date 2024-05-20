package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundImage

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.Title
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.CheckboxWithText
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.PreferencesBody
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.SwitchWithText
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage

@Composable
fun BackgroundImagePreferences(
    backgroundImage: MutableState<BackgroundImage>,
    canvasController: CanvasController,
    onNavigate: () -> Unit
) {
    val image = backgroundImage.value
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        canvasController.processBackground(it)
    }
    PreferencesBody(currentRoute = Screen.BackgroundImageScreen.route, onExit = { onNavigate() }) {
        AnimatedVisibility(
            visible = image.image != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            SwitchWithText(text = "Show background image", isChecked = image.isVisible) {
                backgroundImage.value = backgroundImage.value.copy(
                    isVisible = it
                )
            }
        }
        OpenImageButton(
            image = backgroundImage.value,
            onOpen = { canvasController.getBackground(singlePhotoPickerLauncher) },
            onRemove = {
                backgroundImage.value = backgroundImage.value.copy(
                    image = null
                )
            }
        )
        AnimatedVisibility(visible = image.image != null, enter = fadeIn(), exit = fadeOut()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CheckboxWithText(
                    isChecked = image.stickToBackground,
                    title = "Stick to background",
                    description = "The image will always be visible when moving"
                ) {
                    backgroundImage.value = backgroundImage.value.copy(
                        stickToBackground = it
                    )
                }

                ImageScaling(selectedScaleModes = image.scaleMode) {
                    backgroundImage.value = backgroundImage.value.copy(
                        scaleMode = it
                    )
                }

                Title(text = "Horizontal alignment", fontSize = 18.sp)
                ImageAlignment(backgroundImage = backgroundImage)

                Title(text = "Vertical alignment", fontSize = 18.sp)
                ImageAlignment(backgroundImage = backgroundImage, isHorizontal = false)
            }
        }
    }
}