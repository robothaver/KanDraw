package com.robothaver.kandraw.dialogs.preferencesDialog.pages.toolbarSettings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.Title
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.CheckboxWithText
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.DropdownMenuWithText
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.PreferencesBody
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.SwitchWithText
import com.robothaver.kandraw.viewModel.data.ToolbarSettings
import com.robothaver.kandraw.viewModel.data.ToolbarSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolbarSettingsPage(toolbarSettings: MutableState<ToolbarSettings>, onNavigate: () -> Unit) {
    PreferencesBody(currentRoute = Screen.ToolbarPreferencesScreen.route, onExit = onNavigate) {
        SwitchWithText(text = "Show toolbar", isChecked = toolbarSettings.value.isVisible) {
            toolbarSettings.value = toolbarSettings.value.copy(
                isVisible = it
            )
        }
        CheckboxWithText(
            isChecked = toolbarSettings.value.isPinned,
            title = "Pin toolbar",
            description = "The toolbar wont be movable"
        ) {
            toolbarSettings.value = toolbarSettings.value.copy(
                isPinned = it
            )
        }
        CheckboxWithText(
            isChecked = toolbarSettings.value.hideOnDraw,
            title = "Hide toolbar on draw",
            description = "The toolbar will disappear whenever you draw or erase"
        ) {
            toolbarSettings.value = toolbarSettings.value.copy(
                hideOnDraw = it
            )
        }
        Title(text = "Toolbar type", fontSize = 18.sp, padding = 0.dp)
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = toolbarSettings.value.isHorizontal,
                onClick = {
                    toolbarSettings.value = toolbarSettings.value.copy(
                        isHorizontal = true
                    )
                },
                shape = RoundedCornerShape(bottomStart = 16.dp, topStart = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Horizontal")

                }
            }
            SegmentedButton(
                selected = !toolbarSettings.value.isHorizontal,
                onClick = {
                    toolbarSettings.value = toolbarSettings.value.copy(
                        isHorizontal = false
                    )
                },
                shape = RoundedCornerShape(bottomEnd = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Vertical")
                }
            }
        }

        val isExpanded = remember {
            mutableStateOf(false)
        }
        DropdownMenuWithText(
            selectedItem = toolbarSettings.value.size.name,
            title = "Size",
            expanded = isExpanded.value,
            onSelected = {isExpanded.value = true},
            onDismissRequest = { isExpanded.value = false }
        ) {
            ToolbarSize.values().forEach { size ->
                DropdownMenuItem(text = {
                    Text(
                        text = size.name,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }, onClick = {
                    toolbarSettings.value = toolbarSettings.value.copy(
                        size = size
                    )
                    isExpanded.value = false
                })
            }
        }
    }
}
