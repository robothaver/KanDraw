package com.example.kandraw.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColorItem(color: Color, currentColor: Color, size: Dp, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .size(size)
            .border(
                border = if (color == currentColor) {
                    BorderStroke(2.dp, color)
                } else {
                    BorderStroke(0.dp, Color.Transparent)
                },
                shape = CircleShape
            )
            .padding(if (color == currentColor) 4.dp else 0.dp)
            .clip(CircleShape)
            .background(color)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    )
    {
        AnimatedVisibility(
            visible = color == currentColor,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Icon(Icons.Filled.Check, null, tint = Color.White)
        }
    }
}