package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundSettings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robothaver.kandraw.dialogs.preferencesDialog.Screen
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
                    sizeRange = 5f..300f,
                    onColorChange = {
                        changePage("${Screen.CustomColorSelector.route}/SmallGridColor")
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
                            changePage("${Screen.CustomColorSelector.route}/LargeGridColor")
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

@Composable
private fun SwitchWithText(
    text: String,
    isChecked: Boolean,
    onChange: (isChecked: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onChange(!isChecked) }
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = isChecked,
            onCheckedChange = {
                onChange(it)
            }
        )
    }
}
