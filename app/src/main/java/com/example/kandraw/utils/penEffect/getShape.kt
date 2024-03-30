package com.example.kandraw.utils.penEffect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import com.example.kandraw.viewModel.Shapes

fun getShape(selectedShapes: Shapes, strokeWidth: Float): Path {
    val strokeWidth = strokeWidth * 0.6f
    return when (selectedShapes) {
        Shapes.Star -> {
            RoundedPolygon.star(
                5,
                innerRadius = strokeWidth / 2,
                radius = strokeWidth,
                centerX = 0f, centerY = 0f
            ).toPath().asComposePath()
        }

        Shapes.Triangle -> RoundedPolygon(
            numVertices = 3, radius = strokeWidth,
            centerX = 0f, centerY = 0f
        ).toPath().asComposePath()

        Shapes.Hexagon -> RoundedPolygon(
            numVertices = 6, radius = strokeWidth,
            centerX = 0f, centerY = 0f
        ).toPath().asComposePath()


        Shapes.Arrow -> {
            Path().apply {
                listOf(
                    Offset(-0.165f, 0.2f),
                    Offset(-0.065f, 0.3f),
                    Offset(0.17f, 0f),
                    Offset(-0.065f, -0.3f),
                    Offset(-0.165f, -0.2f),
                    Offset(0f, 0f)
                ).forEach { point ->
                    lineTo(point.x * strokeWidth, point.y * strokeWidth)
                }
            }
        }
    }
}