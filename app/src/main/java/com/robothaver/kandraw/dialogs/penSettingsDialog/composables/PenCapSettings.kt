package com.robothaver.kandraw.dialogs.penSettingsDialog.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.viewModel.PenSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenCapSettings(penSettings: MutableState<PenSettings>) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = penSettings.value.cap == StrokeCap.Round,
            icon = {},
            onClick = {
                penSettings.value = penSettings.value.copy(cap = StrokeCap.Round)
            },
            shape = RoundedCornerShape(bottomStart = 16.dp, topStart = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.round_cap),
                    null,
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "Rounded")

            }
        }
        SegmentedButton(
            selected = penSettings.value.cap == StrokeCap.Square,
            icon = { },
            onClick = {
                penSettings.value = penSettings.value.copy(cap = StrokeCap.Square)
            },
            shape = RoundedCornerShape(bottomEnd = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painterResource(id = R.drawable.square_cap),
                    null,
                    modifier = Modifier.size(48.dp)
                )
                Text(text = "Square")
            }
        }
    }
}