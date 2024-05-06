package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robothaver.kandraw.dialogs.getDialogSize
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings.BackgroundSettings
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
    val colorPickerData = listOf(
        ColorPickerData(viewModel.backgroundColor.value, ColorPickerIds.BackgroundColor),
        ColorPickerData(viewModel.gridSettings.value.smallCellColor, ColorPickerIds.SmallGridColor),
        ColorPickerData(viewModel.gridSettings.value.largeCellColor, ColorPickerIds.LargeGridColor)
    )
    val size = getDialogSize(windowInfo.screenWidthInfo, windowInfo.screenHeightInfo)

    Column(
        modifier = Modifier
            .fillMaxWidth(size.width)
            .fillMaxHeight(size.height),
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.MainScreen.route,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(true)
            },
            popEnterTransition = {
                scaleIntoContainer()
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) {
            composable(route = Screen.MainScreen.route) {
                Column(Modifier.fillMaxSize()) {
                    MainScreen {
                        navController.navigate(it)
                    }
                }
            }
            composable(route = Screen.SaveDrawing.route) {
                Column(modifier = Modifier.fillMaxSize()) {
                    PreferencesHeader(currentRoute = Screen.SaveDrawing.route) {

                    }
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
                PreferencesBody(
                    currentRoute = Screen.BackgroundImageScreen.route,
                    onExit = { navController.popBackStack() }
                ) {
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
            composable(route = Screen.BackgroundSettings.route) {
                BackgroundSettings(
                    viewPortPosition = viewModel.viewportPosition,
                    backgroundColor = viewModel.backgroundColor.value.color,
                    gridSettings = viewModel.gridSettings,
                    onGoBack = {
                        navController.popBackStack()
                    }
                ) {
                    navController.navigate(it)
                }
            }

            composable(
                route = "${Screen.CustomColorSelector.route}/{colorId}",
                arguments = listOf(navArgument("colorId") { type = NavType.StringType })
            ) {
                val colorId = it.arguments?.getString("colorId")!!
                val selectedData = colorPickerData.find { data ->
                    data.id.name == colorId
                }!!
                CustomColorSelector(viewModel = viewModel, selectedData = selectedData) {
                    navController.popBackStack()
                }
            }
        }
    }
}

private fun scaleIntoContainer(
    isReversed: Boolean = false,
    initialScale: Float = if (isReversed) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

private fun scaleOutOfContainer(
    isReversed: Boolean = false,
    targetScale: Float = if (isReversed) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}
