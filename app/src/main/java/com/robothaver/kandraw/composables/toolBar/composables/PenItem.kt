package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.viewModel.data.Tools


@Composable
fun PenItem(activeTool: Tools, tint: Color, size: Dp, padding: Dp, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size + 12.dp)
            .clip(CircleShape)
            .background(
                if (activeTool == Tools.Pen || activeTool == Tools.ColorPicker) {
                    MaterialTheme.colorScheme.primary
                } else Color.Transparent
            )
            .clickable {
                onClick()
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.pen_bg),
            contentDescription = null,
            modifier = Modifier.size(size).padding(padding),
            tint = tint
        )
        Icon(
            painter = painterResource(id = R.drawable.pen_outline),
            contentDescription = null,
            modifier = Modifier.size(size).padding(padding),
            tint = if (activeTool == Tools.Pen || activeTool == Tools.ColorPicker) {
                MaterialTheme.colorScheme.onPrimary
            } else Color.Black
        )
    }
}