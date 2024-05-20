package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundImage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kandraw.R
import com.robothaver.kandraw.dialogs.preferencesDialog.composables.RadioButtonWithText
import com.robothaver.kandraw.viewModel.data.backgroundImage.ScaleModes

@Composable
fun ImageScaling(selectedScaleModes: ScaleModes, onSelected: (mode: ScaleModes) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        Icon(
            painterResource(id = R.drawable.image_scale),
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Image scaling",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
    Column(
        modifier = Modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        ScaleModes.values().forEach {
            RadioButtonWithText(
                selected = selectedScaleModes == it,
                text = it.name.split(Regex("(?=\\p{Upper})")).joinToString(" ")
            ) {
                onSelected(it)
            }
        }
    }
}