package com.robothaver.kandraw.composables.customColorPicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.robothaver.kandraw.composables.customColorPicker.layouts.HorizontalColorPicker
import com.robothaver.kandraw.composables.customColorPicker.layouts.VerticalColorPicker
import com.robothaver.kandraw.utils.windowInfo.WindowType
import com.robothaver.kandraw.utils.data.PenColor

@Composable
fun CustomColorPicker(
    initialColor: Color,
    penColor: PenColor,
    layout: WindowType = WindowType.Compact,
    onDismiss: () -> Unit,
    onBrightnessChanged: (brightness: Float) -> Unit,
    onColorPickerActivated: () -> Unit,
    onColorSelected: (selectedColor: Color) -> Unit
) {
    val colorPickerController = rememberColorPickerController()
    val color by remember {
        mutableStateOf(initialColor)
    }
    Column {
        TopBar(
            onDismiss = { onDismiss() },
            onColorPickerActivated = { onColorPickerActivated() }
        )
        if (layout == WindowType.Compact) {
            VerticalColorPicker(
                colorPickerController = colorPickerController,
                penColor = penColor,
                initialColor = color,
                onColorSelected = { onColorSelected(it) },
                onBrightnessChanged = { onBrightnessChanged(it) }
            )
        } else {
            HorizontalColorPicker(
                colorPickerController = colorPickerController,
                penColor = penColor,
                initialColor = color,
                onColorSelected = { onColorSelected(it) },
                onBrightnessChanged = { onBrightnessChanged(it) }
            )
        }
    }
}

@Composable
private fun TopBar(onDismiss: () -> Unit, onColorPickerActivated: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable { onDismiss() }
        )
        Icon(
            painterResource(id = R.drawable.eye_dropper),
            null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable { onColorPickerActivated() }
        )
    }
}
