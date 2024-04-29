package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.gridSettings.GridSettings
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.mainScreen.MainScreen
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.utils.windowInfo.WindowInfo
import com.robothaver.kandraw.viewModel.CanvasViewModel

@Composable
fun PreferencesDialog(
    windowInfo: WindowInfo,
    canvasController: CanvasController,
    viewModel: CanvasViewModel
) {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .verticalScroll(rememberScrollState())
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
                MainScreen {
                    println(it)
                    navController.navigate(it)
                }
            }
            composable(route = Screen.SaveDrawing.route) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Coming soon...")
                }
            }
            composable(route = Screen.Other.route) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "OTHER PAGE")
                }
            }
            composable(route = Screen.ToolbarPreferencesScreen.route) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "TOOLBAR PAGE")
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
            composable(route = Screen.GridSettings.route) {
                GridSettings(viewModel.gridSettings) {
                    navController.navigate(it)
                }
            }

            composable(
                route = Screen.CustomColorSelector.route
            ) {
                TODO("Implement custom color picker")
//                CustomColorPicker(
//                    initialColor = ,
//                    penColor = ,
//                    onDismiss = { /*TODO*/ },
//                    onBrightnessChanged = ,
//                    onColorPickerActivated = { /*TODO*/ }) {
//
//                }
            }
        }
    }
}



