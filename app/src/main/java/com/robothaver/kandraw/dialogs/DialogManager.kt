package com.robothaver.kandraw.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.robothaver.kandraw.dialogs.eraserSettingsDialog.EraserSettingsDialog
import com.robothaver.kandraw.dialogs.penSettingsDialog.PenSettingsDialog
import com.robothaver.kandraw.dialogs.preferencesDialog.PreferencesDialog
import com.robothaver.kandraw.domain.canvasController.CanvasController
import com.robothaver.kandraw.utils.WindowManager
import com.robothaver.kandraw.utils.windowInfo.WindowInfo
import com.robothaver.kandraw.viewModel.CanvasViewModel

@Composable
fun DialogManager(
    viewModel: CanvasViewModel,
    windowInfo: WindowInfo,
    canvasController: CanvasController,
    windowManager: WindowManager
) {
    val selectedDialog = viewModel.selectedDialog
    if (selectedDialog.value == Dialogs.None) return
    Dialog(
        onDismissRequest = {
            selectedDialog.value = Dialogs.None
            windowManager.hideSystemBars()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                .padding(16.dp)
        ) {
            when (selectedDialog.value) {
                Dialogs.PenSettings -> {
                    PenSettingsDialog(
                        selectedDialog,
                        viewModel.penSettings,
                        viewModel.activeTool,
                        windowInfo
                    )
                }

                Dialogs.EraserSettings -> {
                    EraserSettingsDialog(
                        viewModel.eraserWidth,
                        windowInfo, canvasController
                    )
                }

                Dialogs.Preferences -> {
                    windowManager.showSystemBars()
                    PreferencesDialog(windowInfo, canvasController, viewModel)
                }
                Dialogs.None -> Unit
            }
        }
    }
}

enum class Dialogs {
    PenSettings,
    EraserSettings,
    Preferences,
    None
}
