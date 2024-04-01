package com.robothaver.kandraw.dialogs.penSettingsDialog.composables.penEffect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kandraw.R
import com.robothaver.kandraw.dialogs.penSettingsDialog.composables.Title
import com.robothaver.kandraw.viewModel.PenSettings

@Composable
fun PenEffectOptions(penSettings: MutableState<PenSettings>, fontSize: TextUnit = 22.sp) {
    val isPreviewEnabled = remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Title("Pen effects",  fontSize = fontSize)
        Icon(
            if (isPreviewEnabled.value) painterResource(id = R.drawable.preview_off) else painterResource(
                id = R.drawable.preview_on
            ),
            null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .clickable { isPreviewEnabled.value = !isPreviewEnabled.value }
                .size(32.dp)
        )
    }
    PenPreview(penSettings = penSettings, isVisible = isPreviewEnabled)
    PenEffectChanger(penSettings = penSettings)
    SelectedPenEffectSettings(penSettings = penSettings)
}