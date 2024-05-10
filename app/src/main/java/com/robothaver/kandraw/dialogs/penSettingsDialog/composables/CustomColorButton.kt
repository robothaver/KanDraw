package com.robothaver.kandraw.dialogs.penSettingsDialog.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.composables.customColorPicker.utils.setColorToCustom
import com.robothaver.kandraw.viewModel.data.PenSettings

@Composable
fun CustomColorButton(penSettings: MutableState<PenSettings>, size: Dp, onClick: () -> Unit) {
    val customColor = penSettings.value.customColor
    val isSelected = customColor == penSettings.value.penColor.hue ||
            customColor == penSettings.value.penColor.color

    Box(modifier = Modifier
        .padding(6.dp)
        .clip(CircleShape)
        .size(size)
        .border(
            color = if (isSelected) penSettings.value.customColor else Color.Transparent,
            width = 2.dp,
            shape = CircleShape
        )
        .clickable {
            if (isSelected) {
                onClick()
            } else {
                setColorToCustom(penSettings, customColor)
            }

        }) {
        Icon(
            painter = painterResource(id = R.drawable.palette),
            contentDescription = null,
            tint = customColor,
            modifier = Modifier.padding(if (isSelected) 4.dp else 0.dp)
        )
    }
}
