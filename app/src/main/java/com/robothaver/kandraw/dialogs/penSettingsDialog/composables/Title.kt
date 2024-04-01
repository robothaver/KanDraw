package com.robothaver.kandraw.dialogs.penSettingsDialog.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Title(text: String, fontSize: TextUnit = 22.sp, fontWeight: FontWeight = FontWeight.Bold, padding: Dp = 3.dp) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(vertical = padding)
    )
}