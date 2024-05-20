package com.robothaver.kandraw.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun PermissionDialog(
    hasAsked: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .clip(
                    RoundedCornerShape(22.dp)
                )
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Icon(Icons.Rounded.Warning, null, modifier = Modifier.size(36.dp))
            Text(
                text = "Storage permission needed",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (!hasAsked) {
                Text(
                    text = "This app needs access to your phones storage so it can save your drawings!",
                    modifier = Modifier.padding(vertical = 18.dp)
                )
                Text(
                    text = "In case you have permanently denied access, you can go to the app settings to grant it",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            } else {
                Text(
                    text = "You may have permanently denied stroage permission for the app. You can go to the app settings to grant it",
                    modifier = Modifier.padding(vertical = 18.dp)
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onOkClick,
                shape = RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp,
                    bottomStart = 6.dp,
                    bottomEnd = 6.dp
                )
            ) {
                Text(text = "Accept")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                onClick = onGoToAppSettingsClick,
                shape = RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 6.dp,
                    bottomStart = 12.dp,
                    bottomEnd = 12.dp
                )
            ) {
                Icon(Icons.Rounded.Settings, null)
                Text(text = "Take me to settings", modifier = Modifier.padding(horizontal = 12.dp))
            }
        }
    }
}