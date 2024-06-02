package com.robothaver.kandraw.composables.canvas.canvasCore

import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.PointerInputScope

suspend fun PointerInputScope.detectTouchEvent(canvasEventHandler: CanvasEventHandler) {
    awaitEachGesture {
        val pointer = awaitFirstDown()
        canvasEventHandler.tap(pointer.position)
        do {
            val event = awaitDragOrCancellation(pointer.id)
            if (event != null && event.pressed) {
                canvasEventHandler.drag(event.position, event.previousPosition)
            }
        } while (event?.pressed == true)
        canvasEventHandler.dragEnd()
    }
}