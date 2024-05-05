package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robothaver.kandraw.composables.customColorPicker.CustomColorPicker
import com.robothaver.kandraw.dialogs.getSettingsDialogSize
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings.BackgroundSettings
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.mainScreen.MainScreen
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.utils.changeColorBrightness.changeColorBrightness
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
    val size = getSettingsDialogSize(windowInfo.screenWidthInfo, windowInfo.screenHeightInfo)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember { derivedStateOf { currentBackStackEntry?.destination?.route ?: "main" } }
    Column(
        modifier = Modifier
            .fillMaxWidth(size.width)
            .fillMaxHeight(size.height),
    ) {
        PreferencesHeader(currentRoute = currentRoute) {
            navController.navigate(Screen.MainScreen.route)
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.MainScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = LinearEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = LinearEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = LinearEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 400,
                            easing = LinearEasing
                        )
                    )
                }
            ) {
                composable(route = Screen.MainScreen.route) {
                    MainScreen {
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
                    BackgroundSettings(
                        viewPortPosition = viewModel.viewportPosition,
                        backgroundColor = viewModel.backgroundColor.value.color,
                        gridSettings = viewModel.gridSettings
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
                    Column(modifier = Modifier.fillMaxSize()) {
                        CustomColorPicker(
                            modifier = Modifier.size(500.dp),
                            initialColor = selectedData.color.hue,
                            penColor = selectedData.color,
                            onDismiss = {
                                navController.navigate(Screen.GridSettings.route)
                            },
                            onBrightnessChanged = { brightness ->

                            },
                            onColorPickerActivated = {}
                        ) { newColor ->
                            val brightness = viewModel.backgroundColor.value.brightness
                            val updatedColor = changeColorBrightness(newColor, brightness)
                            when (selectedData.id) {
                                ColorPickerIds.SmallGridColor -> {
                                    viewModel.gridSettings.value = viewModel.gridSettings.value.copy(
                                        smallCellColor = viewModel.gridSettings.value.smallCellColor.copy(
                                            hue = updatedColor,
                                            color = updatedColor
                                        )
                                    )
                                }

                                ColorPickerIds.LargeGridColor -> {
                                    viewModel.gridSettings.value = viewModel.gridSettings.value.copy(
                                        largeCellColor = viewModel.gridSettings.value.largeCellColor.copy(
                                            hue = updatedColor,
                                            color = updatedColor
                                        )
                                    )
                                }

                                ColorPickerIds.BackgroundColor -> {
                                    viewModel.backgroundColor.value =
                                        viewModel.backgroundColor.value.copy(
                                            hue = updatedColor,
                                            color = updatedColor
                                        )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

fun exitTransition(reversed: Boolean): ExitTransition {
    return fadeOut(tween(300)) + slideOutHorizontally(
        targetOffsetX = { if (reversed) -300 else 300 },
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearEasing
        )
    )
}

fun enterTransition(reversed: Boolean): EnterTransition {
    return fadeIn(tween(300)) + slideInHorizontally(
        initialOffsetX = { if (reversed) -300 else 300 },
        animationSpec = tween(
            durationMillis = 400,
            easing = LinearEasing
        )
    )
}

