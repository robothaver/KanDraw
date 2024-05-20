package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundImage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kandraw.R
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage

@Composable
fun OpenImageButton(image: BackgroundImage, onOpen: () -> Unit, onRemove: () -> Unit) {
    Button(
        onClick = onOpen,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.add_background_image),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = if (image.image == null) "Select image" else "Select other image",
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        if (image.image != null) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            VerticalDivider(modifier = Modifier.padding(horizontal = 18.dp))
            Icon(
                Icons.Filled.Clear,
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable {
                        onRemove()
                    }
            )
        }
    }
}