package com.ujjolch.masterapp

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.masterapp.R

@Composable
fun CustomGraph(userHist: List<hist>,dataPoints:List<Point>,
                Unit:String) {
    val initialColor = Color.Blue
    val AxisColor = Color.Black
    val HighlightPointColor = MaterialTheme.colorScheme.secondary
    val LineColor = colorResource(id = R.color.Home_Screen_Blue)
    val initialGridLinesColor = MaterialTheme.colorScheme.outline

    val secondaryGradientColor = colorResource(id = R.color.Graph_End_Blue)

    var steps by remember { mutableStateOf(5) }
    var chartLineColor by remember { mutableStateOf(LineColor) }
    var xAxisLineColor by remember { mutableStateOf(AxisColor) }
    var yAxisLineColor by remember { mutableStateOf(AxisColor) }
    var intersectionPointColor by remember { mutableStateOf(LineColor) }

    val datehist by remember(userHist) {
        mutableStateOf(userHist.map { it.date })
    }

    val modifiedDataPoints: List<Point> = if (dataPoints.size >= 2 && dataPoints[0].y == dataPoints[1].y) {
        val newPoint = dataPoints[1].copy(y = dataPoints[1].y - 0.01f)
        listOf(dataPoints[0], newPoint) + dataPoints.drop(2)
    } else {
        dataPoints
    }
    Log.d("Graphtag","$modifiedDataPoints")

//    val dataPoints = userHist.mapIndexed { index , hist ->
//        Point(x = index.toFloat(), y = hist.weight.toFloat())
//    }

//    val dataPoints = remember {  //Dummy Data
//        mutableStateListOf(
//            Point(x = 0f, y = 72f),
//            Point(x = 1f, y = 73f),
//            Point(x = 2f, y = 72f),
//            Point(x = 3f, y = 71f),
//            Point(x = 4f, y = 75f)
//        )
//    }

//    val colorLinearGradient = Brush.linearGradient(
//        listOf(
//            chartLineColor,
//            intersectionPointColor,
//            xAxisLineColor,
//            yAxisLineColor
//        )
//    )
    var showGridLines by remember { mutableStateOf(false) }
    var isDotted by remember { mutableStateOf(false) }

    val gridLines: GridLines by remember { mutableStateOf(GridLines(initialGridLinesColor)) }
    val labelModifier = Modifier.padding(horizontal = 16.dp)

    val xAxisData = remember(modifiedDataPoints, datehist) {
        AxisData.Builder().axisStepSize(100.dp).steps(modifiedDataPoints.size - 1)
            .labelData { value ->
                if (value != 0) datehist[value].substring(0, datehist[value].length - 5)
                else ("          " + ((datehist[value].substring(0, datehist[value].length - 5))))
            }.labelAndAxisLinePadding(15.dp)
            .axisLineColor(xAxisLineColor).axisLabelColor(Color.Black)
            .backgroundColor(Color.White)
            .axisLabelFontSize(12.sp).build()
    }

    val yAxisData = remember(modifiedDataPoints, steps) {
        AxisData.Builder().steps(1).labelAndAxisLinePadding(20.dp).labelData { i ->
            val yMin = modifiedDataPoints.minOf { it.y }
            val yMax = modifiedDataPoints.maxOf { it.y }
            val yScale = (yMax - yMin) / 1
            (((i * yScale) + yMin).toDouble()).format(2)
        }.axisLineColor(yAxisLineColor).axisLabelColor(Color.Black)
        .backgroundColor(Color.White)
            .axisLabelFontSize(12.sp).build()
    }

    val lineChartData = remember(
        modifiedDataPoints,
        chartLineColor,
        intersectionPointColor,
        xAxisLineColor,
        yAxisLineColor,
        showGridLines
    ){
    LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = modifiedDataPoints,
                    LineStyle(
                        color = LineColor,
                        lineType = LineType.SmoothCurve(isDotted = isDotted),
                    ),
                    intersectionPoint = IntersectionPoint(intersectionPointColor),
                    selectionHighlightPoint = SelectionHighlightPoint(HighlightPointColor),
                    shadowUnderLine = ShadowUnderLine(
                        alpha = 0.5f, brush = Brush.verticalGradient(
                            colors = listOf(
                                LineColor,
                                secondaryGradientColor,
                                Color.White,
                                Color.White
                            )
                        )
                    ),
                    selectionHighlightPopUp = SelectionHighlightPopUp(
                        labelAlignment = Paint.Align.LEFT,
                        popUpLabel = { x, y ->
                            "${y.toDouble().format(2)} $Unit"
                        }
                    )
                )
            )
        ),
        containerPaddingEnd = 80.dp,
        backgroundColor = Color.White,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = if (showGridLines) gridLines else null

    )
}
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val graphHeight = (screenHeight.value/1.455).dp

    Box (
        Modifier
            .fillMaxSize()
            .padding(bottom = 230.dp)
    ){
        LineChart(
            modifier = Modifier
                .height(graphHeight),
            lineChartData = lineChartData
        )
    }
}


