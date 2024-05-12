package com.robothaver.kandraw.dialogs.preferencesDialog.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PreferencesBody(
    currentRoute: String,
    onExit: () -> Unit,
    composable: @Composable () -> Unit
) {
    Column {
        PreferencesHeader(currentRoute = currentRoute) {
            onExit()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            composable()
        }
    }
}