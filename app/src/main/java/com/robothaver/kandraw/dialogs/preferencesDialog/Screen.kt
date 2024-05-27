package com.robothaver.kandraw.dialogs.preferencesDialog

sealed class Screen(val route: String) {
    object MainScreen: Screen("main")
    object ToolbarPreferencesScreen: Screen("toolbar_preferences")
    object BackgroundImageScreen: Screen("background_image")
    object SaveDrawing: Screen("save_drawing")
    object BackgroundSettings: Screen("background_settings")
    object CustomColorSelector: Screen("custom_color_selector")
}