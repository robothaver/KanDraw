package com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.robothaver.kandraw.composables.customColorPicker.CustomColorPicker
import com.robothaver.kandraw.utils.windowInfo.WindowType
import com.robothaver.kandraw.viewModel.CanvasViewModel

@Composable
fun CustomColorSelector(
    viewModel: CanvasViewModel,
    selectedData: ColorSelectorData,
    layout: WindowType,
    onExit: () -> Unit
) {
    val customColorSelectorHandler = CustomColorSelectorHandler(selectedData = selectedData)
    CustomColorPicker(
        modifier = Modifier.fillMaxWidth(),
        initialColor = selectedData.color.hue,
        penColor = selectedData.color,
        layout = layout,
        onDismiss = {
            onExit()
        },
        onBrightnessChanged = { brightness ->
            customColorSelectorHandler.updateBrightness(brightness, viewModel)
        }
    ) { newColor ->
        customColorSelectorHandler.updateColor(newColor, viewModel)
    }
}
