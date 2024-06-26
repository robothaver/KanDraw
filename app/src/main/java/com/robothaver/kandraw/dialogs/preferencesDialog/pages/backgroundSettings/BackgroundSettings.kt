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
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.PreferencesBody
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.ColorSelectorIds
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.viewModel.data.GridSettings

@Composable
fun BackgroundSettings(
    canvasController: CanvasController,
    viewPortPosition: MutableState<Offset>,
    backgroundColor: Color,
    gridSettings: MutableState<GridSettings>,
    onGoBack: () -> Unit,
    changePage: (route: String) -> Unit
) {
    PreferencesBody(currentRoute = Screen.BackgroundSettings.route, onExit = { onGoBack() }) {
        ColorSelector(color = backgroundColor, title = "Background color") {
            changePage("${Screen.CustomColorSelector.route}/${ColorSelectorIds.BackgroundColor.name}")
        }

        FilledTonalButton(
            onClick = {
                viewPortPosition.value = Offset(0f, 0f)
                canvasController.getVisiblePaths()
                      },
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