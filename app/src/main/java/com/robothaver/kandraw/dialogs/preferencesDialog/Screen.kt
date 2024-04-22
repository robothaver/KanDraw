package com.robothaver.kandraw.dialogs.preferencesDialog

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object ToolbarPreferencesScreen: Screen("second_screen")
    object BackgroundImageScreen: Screen("second_screen")
    object SaveDrawing: Screen("second_screen")
    object Other: Screen("second_screen")

}