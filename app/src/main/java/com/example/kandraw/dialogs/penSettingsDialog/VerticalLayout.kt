package com.example.kandraw.dialogs.penSettingsDialog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kandraw.R
import com.example.kandraw.composables.alphaSlider.AlphaSlider
import com.example.kandraw.composables.colorBrightnessSlider.ColorBrightnessSlider
import com.example.kandraw.utils.changeColorBrightness.changeColorBrightness
import com.example.kandraw.viewModel.PenSettings
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VerticalSettingsLayout(
    colorPickerController: ColorPickerController,
    penSettings: MutableState<PenSettings>,
) {
    val color = remember {
        mutableStateOf(Color(penSettings.value.penColor.color.toArgb()))
    }
    val isOn = remember {
        mutableStateOf(false)
    }
    val colors = listOf<Color>(
        Color(0xFFE80101),
        Color(0xFFFE2738),
        Color(0xFFFE8101),
        Color(0xFFFEBE01),
        Color(0xFF3C4EBD),
        Color(0xFF019DEC),
        Color(0xFF9B01B0),
        Color(0xFF00BB72),
        Color(0xFF00823A),
    )
    AnimatedContent(
        targetState = isOn.value,
        content = { isVisible ->
            if (!isVisible) {
                Column {
                    Title("Color")
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 0..4) {
                            ColorItem(color = colors[i], penSettings.value.penColor.hue) {
                                updateColor(penSettings, newColor = colors[i], newHue = colors[i])
                            }
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (i in 5..8) {
                            ColorItem(color = colors[i], penSettings.value.penColor.hue) {
                                updateColor(penSettings, newColor = colors[i], newHue = colors[i])
                            }
                        }
                        CustomColorButton(currentColor = penSettings.value.penColor.color) {
                            isOn.value = !isOn.value
                        }

                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    AlphaSlider(
                        alpha = penSettings.value.alpha,
                        color = penSettings.value.penColor.hue
                    ) {
                        penSettings.value = penSettings.value.copy(alpha = it)
                    }

                    ColorBrightnessSlider(
                        penColor = penSettings.value.penColor
                    ) {
                        updateColor(penSettings, brightness = it)
                    }

                    Divider(
                        modifier = Modifier.padding(vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Title("Width: ${penSettings.value.strokeWidth.roundToInt()}")
                    Slider(
                        value = penSettings.value.strokeWidth,
                        onValueChange = {
                            penSettings.value = penSettings.value.copy(strokeWidth = it)
                        },
                        valueRange = 1f..300f,
                    )
                }
            } else {
                Column {
                    Icon(
                        Icons.Filled.ArrowBack,
                        null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.clip(CircleShape).size(32.dp).clickable {
                            isOn.value = !isOn.value
                        }
                    )
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(275.dp)
                            .padding(10.dp),
                        controller = colorPickerController,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            updateColor(penSettings, colorEnvelope.color, colorEnvelope.color)
                        },
                        initialColor = color.value
                    )
                    ColorBrightnessSlider(
                        penColor = penSettings.value.penColor
                    ) {
                        updateColor(penSettings, brightness = it)
                    }
                }
            }
        }, label = "",
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    )


//    Title(penSettings.value.strokeWidth.roundToInt().toString(), fontSize = 16.sp, fontWeight = FontWeight.Normal)

}

@Composable
fun Title(text: String, fontSize: TextUnit = 22.sp, fontWeight: FontWeight = FontWeight.Bold) {
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}

fun updateColor(
    penSettings: MutableState<PenSettings>,
    newColor: Color = penSettings.value.penColor.color,
    newHue: Color = penSettings.value.penColor.hue,
    brightness: Float = penSettings.value.penColor.brightness
) {
    penSettings.value = penSettings.value.copy(
        penColor = penSettings.value.penColor.copy(
            color = changeColorBrightness(
                newHue,
                brightness
            ),
            hue = newHue,
            brightness = brightness
        )
    )
}

@Composable
fun CustomColorButton(currentColor: Color, onClick: () -> Unit) {
    Box(modifier = Modifier
        .clip(CircleShape)
        .border(2.dp, currentColor, CircleShape)
        .padding(4.dp)
        .clickable { onClick() }
        .size(42.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.palette),
            contentDescription = null,
            tint = currentColor
        )
    }
}