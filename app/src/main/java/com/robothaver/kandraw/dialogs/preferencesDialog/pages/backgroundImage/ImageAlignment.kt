package com.robothaver.kandraw.dialogs.preferencesDialog.pages.backgroundImage

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.viewModel.data.backgroundImage.BackgroundImage
import com.robothaver.kandraw.viewModel.data.backgroundImage.HorizontalAlignment
import com.robothaver.kandraw.viewModel.data.backgroundImage.ImageAlignment
import com.robothaver.kandraw.viewModel.data.backgroundImage.VerticalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageAlignment(
    backgroundImage: MutableState<BackgroundImage>,
    isHorizontal: Boolean = true
) {
    val alignment = backgroundImage.value.alignment
    MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            checked = isChecked(
                isHorizontal,
                alignment,
                HorizontalAlignment.Start,
                VerticalAlignment.Top
            ),
            onCheckedChange = {
                onClick(
                    isHorizontal,
                    backgroundImage,
                    HorizontalAlignment.Start,
                    VerticalAlignment.Top
                )
            },
            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
        ) {
            Text(
                text = if (isHorizontal) {
                    "Start"
                } else {
                    "Top"
                }
            )
        }
        SegmentedButton(
            checked = isChecked(
                isHorizontal,
                alignment,
                HorizontalAlignment.Center,
                VerticalAlignment.Center
            ),
            onCheckedChange = {
                onClick(
                    isHorizontal,
                    backgroundImage,
                    HorizontalAlignment.Center,
                    VerticalAlignment.Center
                )
            },
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(text = "Center")
        }
        SegmentedButton(
            checked = isChecked(
                isHorizontal,
                alignment,
                HorizontalAlignment.End,
                VerticalAlignment.Bottom
            ),
            onCheckedChange = {
                onClick(
                    isHorizontal,
                    backgroundImage,
                    HorizontalAlignment.End,
                    VerticalAlignment.Bottom
                )
            },
            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
        ) {
            Text(
                text = if (isHorizontal) {
                    "End"
                } else {
                    "Bottom"
                }
            )
        }
    }
}

private fun onClick(
    isHorizontal: Boolean,
    backgroundImage: MutableState<BackgroundImage>,
    horizontalAlignment: HorizontalAlignment,
    verticalAlignment: VerticalAlignment
) {
    if (isHorizontal) {
        setAlignment(backgroundImage, horizontal = horizontalAlignment)
    } else {
        setAlignment(backgroundImage, vertical = verticalAlignment)
    }
}

private fun isChecked(
    isHorizontal: Boolean,
    alignment: ImageAlignment,
    horizontalAlignment: HorizontalAlignment,
    verticalAlignment: VerticalAlignment
): Boolean {
    return if (isHorizontal) {
        alignment.horizontal == horizontalAlignment
    } else {
        alignment.vertical == verticalAlignment
    }
}

private fun setAlignment(
    backgroundImage: MutableState<BackgroundImage>,
    horizontal: HorizontalAlignment = backgroundImage.value.alignment.horizontal,
    vertical: VerticalAlignment = backgroundImage.value.alignment.vertical
) {
    backgroundImage.value = backgroundImage.value.copy(
        alignment = ImageAlignment(horizontal, vertical)
    )
}
