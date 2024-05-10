package com.robothaver.kandraw.viewModel

import android.graphics.Bitmap
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.robothaver.kandraw.dialogs.Dialogs
import com.robothaver.kandraw.viewModel.data.GridSettings
import com.robothaver.kandraw.viewModel.data.PathData
import com.robothaver.kandraw.viewModel.data.PenColor
import com.robothaver.kandraw.viewModel.data.PenSettings
import com.robothaver.kandraw.viewModel.data.Tools

class CanvasViewModel : ViewModel() {
    // Canvas
    val allPaths = mutableStateListOf<PathData>()
    val allPathBackup = mutableStateListOf<List<PathData>>()
    val undoPaths = mutableStateListOf<PathData>()
    val redoPaths = mutableStateListOf<PathData>()
    val penSettings = mutableStateOf(PenSettings())
    val activeTool = mutableStateOf(Tools.Pen)
    val eraserWidth = mutableFloatStateOf(20f)
    val viewportPosition = mutableStateOf(Offset(0f, 0f))
    val backgroundImage = mutableStateOf<Bitmap?>(null)
    val backgroundColor = mutableStateOf(PenColor(color = Color.DarkGray, hue = Color.White, brightness = 0.27f))
    val gridSettings = mutableStateOf(GridSettings())
    // Dialogs
    val selectedDialog = mutableStateOf(Dialogs.Preferences)

    fun setInitial(range: IntRange) {
        val newPaths = mutableListOf<PathData>()
        val points = listOf(Offset(360.0f, 1148.3f), Offset(357.1f, 1151.4f), Offset(360.0f, 1148.3f), Offset(452.0f, 980.9f), Offset(503.0f, 850.9f), Offset(515.4f, 837.9f), Offset(521.1f, 819.9f), Offset(524.8f, 819.9f), Offset(540.0f, 781.8f), Offset(569.0f, 718.9f), Offset(594.0f, 673.0f), Offset(595.0f, 663.9f), Offset(595.0f, 670.1f), Offset(629.2f, 793.9f), Offset(679.0f, 946.5f), Offset(709.0f, 1080.9f), Offset(722.9f, 1163.8f), Offset(722.9f, 1165.3f), Offset(732.4f, 1163.9f), Offset(909.9f, 1109.0f), Offset(995.6f, 1061.9f), Offset(1043.9f, 1028.9f), Offset(1041.9f, 1031.9f), Offset(1035.9f, 1035.9f), Offset(1019.9f, 1064.8f), Offset(894.9f, 1211.9f), Offset(792.9f, 1322.6f), Offset(789.0f, 1332.9f), Offset(806.0f, 1357.0f), Offset(927.0f, 1579.9f), Offset(935.9f, 1603.9f), Offset(971.9f, 1689.9f), Offset(971.9f, 1691.9f), Offset(961.0f, 1683.1f), Offset(865.7f, 1595.7f), Offset(755.9f, 1489.9f), Offset(638.2f, 1357.9f), Offset(606.0f, 1331.8f), Offset(606.0f, 1328.9f), Offset(606.0f, 1332.9f), Offset(587.9f, 1451.1f), Offset(550.9f, 1580.9f), Offset(518.0f, 1664.6f), Offset(507.0f, 1694.9f), Offset(489.0f, 1714.2f), Offset(481.0f, 1716.9f), Offset(474.0f, 1716.9f), Offset(467.0f, 1709.9f), Offset(463.0f, 1709.9f), Offset(459.0f, 1696.9f), Offset(450.0f, 1660.4f), Offset(426.0f, 1542.9f), Offset(419.0f, 1452.9f), Offset(415.0f, 1388.8f), Offset(415.0f, 1342.5f), Offset(409.4f, 1343.9f), Offset(351.1f, 1365.9f), Offset(251.6f, 1387.9f), Offset(175.3f, 1398.9f), Offset(130.1f, 1398.9f), Offset(106.7f, 1398.9f), Offset(115.1f, 1390.9f), Offset(157.9f, 1344.8f), Offset(236.0f, 1258.9f), Offset(297.9f, 1203.9f), Offset(349.0f, 1150.4f))
        newPaths.add(PathData(points = mutableListOf(Offset(360.0f, 1148.3f))))
        for (i in range) {
            val path = Path()
            path.moveTo(newPaths[i - 1].points[newPaths[i - 1].points.lastIndex].x + 1000f, 100f)
            val newPoints = mutableListOf<Offset>()
            points.forEach { point -> path.lineTo(point.x + i * 1000, point.y)
            newPoints.add(Offset(point.x + i * 1000, point.y))}
//            path.lineTo(newPaths[i - 1].points[newPaths[i - 1].points.lastIndex].x + 120f, 100f)
            newPaths.add(PathData(points = newPoints, path = path))
        }
        newPaths.removeLast()
        allPaths.addAll(newPaths)
    }
}
