package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.mainScreen.MainScreen
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.utils.windowInfo.WindowInfo

@Composable
fun PreferencesDialog(windowInfo: WindowInfo, canvasController: CanvasController) {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(0.3f)
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.MainScreen.route,
            enterTransition = {
                fadeIn(tween(300)) + slideInHorizontally(
                    tween(350)
                ) {
                    it / 2
                }
            },
            exitTransition = {
                fadeOut(tween(300)) + slideOutHorizontally(
                    tween(350)
                ) {
                    -it / 2
                }
            },
            popEnterTransition = {
                fadeIn(tween(300)) + slideInHorizontally(
                    tween(350)
                ) {
                    -it / 2
                }
            },
            popExitTransition = {
                fadeOut(tween(300)) + slideOutHorizontally(
                    tween(350)
                ) {
                    it / 2
                }
            }
        ) {
            composable(route = Screen.MainScreen.route) {
                MainScreen(navController = navController)
            }
            composable(route = Screen.SaveDrawing.route) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Coming soon...")
                }
            }
            composable(route = Screen.BackgroundImageScreen.route) {
                val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
                ) {
                    canvasController.processBackground(it)
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Select background image",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { canvasController.getBackground(singlePhotoPickerLauncher) }
                    ) {
                        Text(text = "Open image")
                    }
                }
            }
        }
    }
}
