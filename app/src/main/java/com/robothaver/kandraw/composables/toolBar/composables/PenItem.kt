package com.robothaver.kandraw.composables.toolBar.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.viewModel.data.Tools

@Composable
fun PenItem(activeTool: Tools, tint: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.clip(CircleShape),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            if (activeTool == Tools.Pen || activeTool == Tools.ColorPicker) {
                MaterialTheme.colorScheme.primary
            } else Color.Transparent
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.pen_bg),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = tint
            )
            Icon(
                painter = painterResource(id = R.drawable.pen_outline),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (activeTool == Tools.Pen || activeTool == Tools.ColorPicker) {
                    MaterialTheme.colorScheme.onPrimary
                } else Color.Black
            )
        }
    }
}