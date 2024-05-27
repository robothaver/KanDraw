package com.robothaver.kandraw.dialogs.preferencesDialog.pages.mainScreen

import com.example.kandraw.R
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen


val menuOptions = listOf(
    MenuOption("Background image", R.drawable.add_background_image, Screen.BackgroundImageScreen.route),
    MenuOption("Save drawing", R.drawable.save_drawing, Screen.SaveDrawing.route),
    MenuOption("Toolbar preferences", R.drawable.toolbar_preferences, Screen.ToolbarPreferencesScreen.route),
    MenuOption("Background settings", R.drawable.grid, Screen.BackgroundSettings.route)
)

data class MenuOption(
    val title: String,
    val icon: Int,
    val route: String
)
