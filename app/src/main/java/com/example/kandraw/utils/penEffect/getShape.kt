package com.example.kandraw.utils.penEffect

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.pill
import androidx.graphics.shapes.pillStar
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import com.example.kandraw.viewModel.Shapes

fun getShape(selectedShapes: Shapes, strokewidth: Float): Path {
    return when (selectedShapes) {
        Shapes.Star -> {
            RoundedPolygon.star(
                5,
                innerRadius = strokewidth / 2,
                radius = strokewidth,
                centerX = 0f, centerY = 0f
            ).toPath().asComposePath()
        }

        Shapes.Triangle -> RoundedPolygon(
            numVertices = 3, radius = strokewidth,
            centerX = 0f, centerY = 0f
        ).toPath().asComposePath()

        Shapes.Hexagon -> RoundedPolygon(
            numVertices = 6, radius = strokewidth,
            centerX = 0f, centerY = 0f
        ).toPath().asComposePath()


        Shapes.Arrow -> {
            Path().apply {
                listOf(
                    Offset(-1.65f, 2f),
                    Offset(-0.65f, 3f),
                    Offset(1.7f, 0f),
                    Offset(-0.65f, -3f),
                    Offset(-1.65f, -2f),
                    Offset(0f, 0f)
                ).forEach { point ->
                    lineTo(point.x * strokewidth, point.y * strokewidth)
                }
            }
        }
    }
}