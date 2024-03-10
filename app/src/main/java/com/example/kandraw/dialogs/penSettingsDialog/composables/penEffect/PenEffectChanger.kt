package com.example.kandraw.dialogs.penSettingsDialog.composables.penEffect

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kandraw.dialogs.penSettingsDialog.layouts.updateEffect
import com.example.kandraw.utils.penEffect.getShape
import com.example.kandraw.viewModel.PenSettings
import com.example.kandraw.viewModel.Effects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenEffectChanger(penSettings: MutableState<PenSettings>) {
    val selectedEffect = penSettings.value.penEffectSettings.effect
    SingleChoiceSegmentedButtonRow(modifier = Modifier.padding(vertical = 6.dp)) {
        SegmentedButton(
            selected = selectedEffect == Effects.Default,
            onClick = {
                updateEffect(penSettings, penSettings.value.penEffectSettings.copy(
                    effect = Effects.Default
                ))
            },
            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
        ) {
            Text(text = "Default")
        }
        SegmentedButton(
            selected = selectedEffect == Effects.Dashed,
            onClick = {
                updateEffect(penSettings, penSettings.value.penEffectSettings.copy(
                    effect = Effects.Dashed
                ))
            },
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = "Dashed")
        }
        SegmentedButton(
            selected = selectedEffect == Effects.Stamped,
            onClick = {
                updateEffect(penSettings, penSettings.value.penEffectSettings.copy(
                    effect = Effects.Stamped,
                    shape = getShape(penSettings.value.penEffectSettings.selectedShape, penSettings.value.strokeWidth)
                ))
            },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
        ) {
            Text(text = "Stamped")
        }
    }
}