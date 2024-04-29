package com.robothaver.kandraw.dialogs.preferencesDialog

sealed class Screen(val route: String) {
    object MainScreen: Screen("main_screen")
    object ToolbarPreferencesScreen: Screen("toolbar_preferences_screen")
    object BackgroundImageScreen: Screen("background_image_screen")
    object SaveDrawing: Screen("save_drawing_screen")
    object Other: Screen("other_screen")
    object GridSettings: Screen("grid_settings")
    object CustomColorSelector: Screen("custom_color_selector")
}