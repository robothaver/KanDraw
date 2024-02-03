package com.example.kandraw.composables.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path


fun processPaths(points: List<Offset>, viewPortPosition: Offset, size: Size): Path {
    val newPath = Path()
    newPath.moveTo(points[0].x, points[0].y)
    for (i in points.indices) {
        // Checking if the path is visible on the screen
        if (points[i].x + viewPortPosition.x in 0f..size.width &&
            points[i].y + viewPortPosition.y in 0f..size.height
        ) {
            newPath.lineTo(points[i].x, points[i].y)
        } else {
            if (i != 0) {
                if (points[i - 1].x + viewPortPosition.x in 0f..size.width &&
                    points[i - 1].y + viewPortPosition.y in 0f..size.height
                ) {
                    newPath.lineTo(points[i].x, points[i].y)
                } else {
                    newPath.moveTo(points[i].x, points[i].y)
                }
            }
        }
    }
    return newPath
}