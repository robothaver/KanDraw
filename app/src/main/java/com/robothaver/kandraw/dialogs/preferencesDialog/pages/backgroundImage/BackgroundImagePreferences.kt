package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundImage

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kandraw.R
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.CheckboxWithText
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.PreferencesBody
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.RadioButtonWithText
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.SwitchWithText
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.BackgroundImage
import com.robothaver.kandraw.viewModel.data.ScaleModes

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
        Button(
            onClick = { canvasController.getBackground(singlePhotoPickerLauncher) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.add_background_image),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = if (image.image == null) "Select image" else "Select other image",
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            if (image.image != null) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                VerticalDivider(modifier = Modifier.padding(horizontal = 18.dp))
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable {
                            backgroundImage.value = backgroundImage.value.copy(
                                image = null
                            )
                        }
                )
            }
        }
        CheckboxWithText(
            isChecked = image.stickToBackground,
            title = "Stick to background",
            description = "The image will always be visible when moving"
        ) {
            backgroundImage.value = backgroundImage.value.copy(
                stickToBackground = it
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.image_scale),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = "Image scaling",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ScaleModes.values().forEach {
                RadioButtonWithText(
                    selected = image.scaleImageToFill == it,
                    text = it.name.replace("_", " ")
                ) {
                    backgroundImage.value = backgroundImage.value.copy(
                        scaleImageToFill = it
                    )
                }
            }
        }
    }
}