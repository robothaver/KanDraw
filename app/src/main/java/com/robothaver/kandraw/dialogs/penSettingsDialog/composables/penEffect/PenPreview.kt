package com.robothaver.kandraw.dialogs.penSettingsDialog.composables.penEffect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.robothaver.kandraw.utils.penEffect.getPenEffect
import com.robothaver.kandraw.viewModel.PenSettings

@Composable
fun PenPreview(penSettings: MutableState<PenSettings>, isVisible: MutableState<Boolean>) {
    Box(modifier = Modifier.animateContentSize()) {
        AnimatedVisibility(
            visible = isVisible.value,
            enter = fadeIn(animationSpec = tween(200)) + slideInVertically(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200)) + slideOutVertically(animationSpec = tween(200))
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clipToBounds()
                .drawBehind {
                    val path = Path().apply {
                        cubicTo(
                            0f, size.height,
                            size.width, 0f,
                            size.width, size.height,
                        )
                    }
                    drawPath(
                        path = path,
                        color = penSettings.value.penColor.color,
                        style = Stroke(
                            width = penSettings.value.strokeWidth,
                            cap = penSettings.value.cap,
                            join = StrokeJoin.Round,
                            pathEffect = getPenEffect(penSettings.value)
                        )
                    )
                })
        }
    }
}