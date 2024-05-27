package com.robothaver.kandraw.dialogs.penSettingsDialog.composables.penEffect

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.StampStyles
import com.robothaver.kandraw.dialogs.penSettingsDialog.utils.updateEffect
import com.robothaver.kandraw.viewModel.data.PenSettings

@Composable
fun PenStampStyleChanger(penSettings: MutableState<PenSettings>) {
    val isExpended = remember {
        mutableStateOf(false)
    }
    Text(
        text = "Selected style",
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            .clickable { isExpended.value = !isExpended.value }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = penSettings.value.penEffectSettings.stampStyle.toString(),
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                Icons.Filled.KeyboardArrowDown,
                null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        DropdownMenu(expanded = isExpended.value, onDismissRequest = { isExpended.value = false }) {
            StampStyles.values().forEach { style ->
                DropdownMenuItem(
                    text = { Text(style.toString(), color = MaterialTheme.colorScheme.onBackground) },
                    onClick = {
                        updateEffect(
                            penSettings,
                            penSettings.value.penEffectSettings.copy(
                                stampStyle = when (style) {
                                    StampStyles.Rotate -> StampedPathEffectStyle.Rotate
                                    StampStyles.Morph -> StampedPathEffectStyle.Morph
                                    StampStyles.Translate -> StampedPathEffectStyle.Translate
                                }
                            )
                        )
                        isExpended.value = false
                    }
                )
            }
        }
    }
}