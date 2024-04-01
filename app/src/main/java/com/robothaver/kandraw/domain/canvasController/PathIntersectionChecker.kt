package com.robothaver.kandraw.domain.canvasController

import kotlin.math.pow
import kotlin.math.sqrt

class PathIntersectionChecker {
//    https://www.jeffreythompson.org/collision-detection/line-circle.php#:~:text=Finally%2C%20we%20get%20the%20distance,same%20as%20Point%2FCircle).
    fun lineCircle(x1: Float, y1: Float, x2: Float, y2: Float, cx: Float, cy: Float, r: Float): Boolean {

        val inside1 = pointCircle(x1, y1, cx, cy, r)
        val inside2 = pointCircle(x2, y2, cx, cy, r)
        if (inside1 || inside2) return true

        val distX = x1 - x2
        val distY = y1 - y2
        val len = sqrt((distX * distX) + (distY * distY))

        val dot = (((cx - x1) * (x2 - x1)) + ((cy - y1) * (y2 - y1))) / len.pow(2)

        val closestX = x1 + (dot * (x2 - x1))
        val closestY = y1 + (dot * (y2 - y1))

        val onSegment = linePoint(x1, y1, x2, y2, closestX, closestY)
        if (!onSegment) return false

        val distXToClosest = closestX - cx
        val distYToClosest = closestY - cy
        val distance = sqrt((distXToClosest * distXToClosest) + (distYToClosest * distYToClosest))

        return distance <= r
    }

    fun pointCircle(px: Float, py: Float, cx: Float, cy: Float, r: Float): Boolean {
        val distX = px - cx
        val distY = py - cy
        val distance = sqrt((distX * distX) + (distY * distY))

        return distance <= r
    }

    private fun linePoint(x1: Float, y1: Float, x2: Float, y2: Float, px: Float, py: Float): Boolean {
        val d1 = sqrt((px - x1) * (px - x1) + (py - y1) * (py - y1))
        val d2 = sqrt((px - x2) * (px - x2) + (py - y2) * (py - y2))
        val lineLen = sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
        val buffer = 0.1
        return d1 + d2 >= lineLen - buffer && d1 + d2 <= lineLen + buffer
    }
}