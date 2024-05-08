package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.PreferencesBody
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.PreferencesHeader
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings.BackgroundSettings
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.ColorSelectorData
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.ColorSelectorIds
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.CustomColorSelector
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
        ColorSelectorData(viewModel.backgroundColor.value, ColorSelectorIds.BackgroundColor),
        ColorSelectorData(viewModel.gridSettings.value.smallCellColor, ColorSelectorIds.SmallCellColor),
        ColorSelectorData(viewModel.gridSettings.value.largeCellColor, ColorSelectorIds.LargeCellColor)
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
            enterTransition = { scaleIntoContainer() },
            exitTransition = { scaleOutOfContainer(true) },
            popEnterTransition = { scaleIntoContainer() },
            popExitTransition = { scaleOutOfContainer() }
        ) {
            composable(route = Screen.MainScreen.route) {
                MainScreen {
                    navController.navigate(it)
                }
            }
            composable(route = Screen.SaveDrawing.route) {
                Column(modifier = Modifier.fillMaxSize()) {
                    PreferencesHeader(currentRoute = Screen.SaveDrawing.route) {}
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

                CustomColorSelector(
                    viewModel = viewModel,
                    selectedData = selectedData,
                    layout = windowInfo.screenWidthInfo
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}
