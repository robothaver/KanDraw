package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.robothaver.kandraw.dialogs.preferencesDialog.PreferencesBody
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
import com.robothaver.kandraw.viewModel.data.GridSettings

@Composable
fun BackgroundSettings(
    viewPortPosition: MutableState<Offset>,
    backgroundColor: Color,
    gridSettings: MutableState<GridSettings>,
    onGoBack: () -> Unit,
    changePage: (route: String) -> Unit
) {
    PreferencesBody(currentRoute = Screen.BackgroundSettings.route, onExit = { onGoBack() }) {
        ColorChanger(color = backgroundColor, title = "Background color") {
            changePage("${Screen.CustomColorSelector.route}/BackgroundColor")
        }

        FilledTonalButton(
            onClick = { viewPortPosition.value = Offset(0f, 0f) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset viewport position")
        }

        HorizontalDivider()

        BackgroundGridSettings(gridSettings = gridSettings) {
            changePage(it)
        }
    }
}