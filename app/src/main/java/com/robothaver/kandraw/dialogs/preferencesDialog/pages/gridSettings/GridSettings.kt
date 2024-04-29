package com.robothaver.kandraw.dialogs.preferencesDialog.pages.gridSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import kotlin.math.roundToInt

@Composable
fun GridSettings(gridSettings: MutableState<GridSettings>, changePage: (route: String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SwitchWithText("Enable grid", gridSettings.value.isGridEnabled) {
            gridSettings.value = gridSettings.value.copy(isGridEnabled = it)
        }

        HorizontalDivider()

        Text(
            text = "Small cell settings",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceBright)
                .padding(12.dp)
        ) {
            Text(
                text = "Cell size: ${gridSettings.value.smallCellSize.roundToInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = gridSettings.value.smallCellSize,
                onValueChange = {
                    gridSettings.value = gridSettings.value.copy(smallCellSize = it)
                },
                valueRange = 10f..300f
            )

            Text(
                text = "Stroke width: ${gridSettings.value.smallCellStrokeWidth.roundToInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = gridSettings.value.smallCellStrokeWidth,
                onValueChange = {
                    gridSettings.value = gridSettings.value.copy(smallCellStrokeWidth = it)
                },
                valueRange = 5f..50f,
                steps = 5
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    changePage(Screen.CustomColorSelector.route)
                }
                .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cell color",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(gridSettings.value.smallCellColor)
                )
            }
        }

        HorizontalDivider()

        SwitchWithText("Enable large grid cells", gridSettings.value.isLargeGridEnabled) {
            gridSettings.value = gridSettings.value.copy(isLargeGridEnabled = it)
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { }
            .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Large cell color", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(gridSettings.value.largeCellColor)
                    .padding(horizontal = 12.dp)
            )
        }

        Text(text = gridSettings.value.largeCellSize.toString())
        Slider(
            value = gridSettings.value.largeCellSize.toFloat(),
            onValueChange = {
                gridSettings.value = gridSettings.value.copy(largeCellSize = it.roundToInt())
            },
            valueRange = 2f..40f,
        )

    }
}

@Composable
fun SwitchWithText(
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
