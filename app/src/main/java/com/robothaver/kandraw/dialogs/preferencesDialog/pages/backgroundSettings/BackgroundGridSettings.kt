package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.SwitchWithText
import com.robothaver.kandraw.dialogs.preferencesDialog.pages.customColorSelector.ColorSelectorIds
import com.robothaver.kandraw.viewModel.data.GridSettings

@Composable
fun BackgroundGridSettings(
    gridSettings: MutableState<GridSettings>,
    changePage: (route: String) -> Unit
) {
    val currentGridSettings = gridSettings.value
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SwitchWithText("Enable grid", gridSettings.value.isGridEnabled) {
            gridSettings.value = gridSettings.value.copy(isGridEnabled = it)
        }
        AnimatedVisibility(visible = currentGridSettings.isGridEnabled) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                CellSettings(
                    title = "Small cell settings",
                    cellSize = currentGridSettings.smallCellSize,
                    cellStrokeWidth = currentGridSettings.smallCellStrokeWidth,
                    cellColor = currentGridSettings.smallCellColor.color,
                    sizeRange = 25f..300f,
                    onColorChange = {
                        changePage(
                            "${Screen.CustomColorSelector.route}/${ColorSelectorIds.SmallCellColor.name}"
                        )
                    },
                    onSizeChange = {
                        gridSettings.value = gridSettings.value.copy(smallCellSize = it)
                    },
                    onStrokeWidthChange = {
                        gridSettings.value = gridSettings.value.copy(smallCellStrokeWidth = it)
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                SwitchWithText("Enable large cells", gridSettings.value.isLargeCellEnabled) {
                    gridSettings.value = gridSettings.value.copy(isLargeCellEnabled = it)
                }

                AnimatedVisibility(visible = currentGridSettings.isLargeCellEnabled) {
                    CellSettings(
                        title = "Large cell settings",
                        cellSize = gridSettings.value.largeCellSize.toFloat(),
                        cellStrokeWidth = gridSettings.value.largeCellStrokeWidth,
                        cellColor = gridSettings.value.largeCellColor.color,
                        sizeRange = 2f..30f,
                        onColorChange = {
                            changePage("${Screen.CustomColorSelector.route}/${ColorSelectorIds.LargeCellColor.name}")
                        },
                        onSizeChange = {
                            gridSettings.value =
                                gridSettings.value.copy(largeCellSize = it.toInt())
                        },
                        onStrokeWidthChange = {
                            gridSettings.value =
                                gridSettings.value.copy(largeCellStrokeWidth = it)
                        }
                    )
                }
            }
        }
    }
}

