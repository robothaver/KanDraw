package com.robothaver.kandraw.composables.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.viewModel.data.ToolbarSettings

@Composable
fun ToolbarVisibilityButton(toolbarSettings: MutableState<ToolbarSettings>, containerSize: IntSize) {
    if (!toolbarSettings.value.isVisible) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(containerSize.width - 200, containerSize.height - 200)
                }
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
                .clickable {
                    toolbarSettings.value = toolbarSettings.value.copy(
                        isVisible = true
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painterResource(id = R.drawable.visible),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}