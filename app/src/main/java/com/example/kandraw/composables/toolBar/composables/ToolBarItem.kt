package com.example.kandraw.composables.toolBar.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ToolBarItem(icon: ImageVector, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        modifier = Modifier.clip(CircleShape),
        onClick = { onClick() },
        enabled = enabled,
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            imageVector = icon,
            contentDescription = null
        )
    }
}