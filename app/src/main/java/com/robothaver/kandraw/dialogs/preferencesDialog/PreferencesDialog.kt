package com.robothaver.kandraw.dialogs.preferencesDialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robothaver.kandraw.dialogs.getDialogSize
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundImage.BackgroundImageSettings
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings.BackgroundSettings
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.ColorSelectorData
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.ColorSelectorIds
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.CustomColorSelector
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.mainScreen.MainScreen
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.saveDrawing.SaveDrawing
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.toolbarSettings.ToolbarSettingsPage
import com.robothaver.kandraw.dialogs.scaleIntoContainer
import com.robothaver.kandraw.dialogs.scaleOutOfContainer
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.utils.backgroundDefaultColors
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
        ColorSelectorData(
            viewModel.backgroundColor.value,
            ColorSelectorIds.BackgroundColor,
            backgroundDefaultColors
        ),
        ColorSelectorData(
            viewModel.gridSettings.value.smallCellColor,
            ColorSelectorIds.SmallCellColor
        ),
        ColorSelectorData(
            viewModel.gridSettings.value.largeCellColor,
            ColorSelectorIds.LargeCellColor
        )
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
                MainScreen(screenWidthInfo = windowInfo.screenWidthInfo) {
                    navController.navigate(it)
                }
            }
            composable(route = Screen.SaveDrawing.route) {
                SaveDrawing(viewModel.imageSaveOptions, canvasController) {
                    navController.popBackStack()
                }
            }
            composable(route = Screen.ToolbarPreferencesScreen.route) {
                ToolbarSettingsPage(viewModel.toolbarSettings) {
                    navController.popBackStack()
                }
            }
            composable(route = Screen.BackgroundImageScreen.route) {
                BackgroundImageSettings(viewModel.backgroundImage, canvasController) {
                    navController.popBackStack()
                }
            }
            composable(route = Screen.BackgroundSettings.route) {
                BackgroundSettings(
                    canvasController = canvasController,
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
