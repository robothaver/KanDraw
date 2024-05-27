package com.robothaver.kandraw.dialogs.preferencesDialog.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.Title

@Composable
fun DropdownMenuWithText(
    title: String,
    selectedItem: String,
    onSelected: () -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    composable: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                onSelected()
            }
            .border(
                width = 3.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Title(text = title, fontSize = 18.sp)
        Spacer(Modifier.weight(1f))
        Text(
            text = selectedItem,
            color = MaterialTheme.colorScheme.onBackground
        )
        Icon(
            Icons.Filled.KeyboardArrowDown,
            null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.clip(
                RoundedCornerShape(6.dp)
            )
        ) {
            composable()
        }
    }
}
