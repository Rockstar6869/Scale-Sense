package com.example.masterapp


import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphColors
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphFillType
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphVisibility
import com.jaikeerthick.composable_graphs.style.LabelPosition


@Composable
fun WeightGraph(userHist: List<hist>) {
    val datapoints = userHist.map{
        val modifiedDate =  it.date.substring(0,(it.date).length-5)
        LineData(modifiedDate,it.weight)
    }
    val width = datapoints.size*100
    LazyRow {
        item {
            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(400.dp)
                    .width(width.dp),
                data = datapoints,
                onPointClick = { value: LineData ->

                },
                style = LineGraphStyle(
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true,
                    ),
                    yAxisLabelPosition = LabelPosition.LEFT,
                    colors = LineGraphColors(
                        lineColor = colorResource(id = R.color.Graph_Line_Color),
                        pointColor = Color.Blue,
                        fillType = LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.Graph_Start_Blue),
                                    colorResource(id = R.color.Graph_End_Blue),
                                    Color.White,
                                    Color.White,
                                    Color.White
                                )
                            )
                        )
                    )
                )
            )
        }
    }


}

@Composable
fun BMIGraph(userHist: List<hist>,HeightInCM:Double){

    val datapoints = userHist.map{
        val modifiedDate =  it.date.substring(0,(it.date).length-5)
        LineData(modifiedDate,Calculate.BMI(HeightInCM,it.weight))
    }
    val width = datapoints.size*100
    LazyRow {
        item {
            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(400.dp)
                    .width(width.dp),
                data = datapoints,
                onPointClick = { value: LineData ->
                    // do something with value
                },
                style = LineGraphStyle(
                    yAxisLabelPosition = LabelPosition.LEFT,
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true
                    ),
                    colors = LineGraphColors(
                        lineColor = colorResource(id = R.color.Graph_Line_Color),
                        pointColor = Color.Blue,
                        fillType = LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.Graph_Start_Blue),
                                    colorResource(id = R.color.Graph_End_Blue),
                                    Color.White,
                                    Color.White,
                                    Color.White
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}

@Composable
fun BodyWaterPercentGraph(userHist:List<hist>,HeightInCM:Double,Age:Int,Gender:String="M"){
    var datapoints:List<LineData>

    if(Gender=="M"){
        datapoints = userHist.map{
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            LineData(modifiedDate,Calculate.BodyWaterPercent(Calculate.BodyWaterForMale(Age,HeightInCM,it.weight),it.weight))
        }
    }
    else{
        datapoints = userHist.map{
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            LineData(modifiedDate,Calculate.BodyWaterPercent(Calculate.BodyWaterForFemale(Age,HeightInCM,it.weight),it.weight))
        }
    }
    val width = datapoints.size*100
    LazyRow {
        item {
            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(400.dp)
                    .width(width.dp),
                data = datapoints,
                onPointClick = { value: LineData ->
                    // do something with value
                },
                style = LineGraphStyle(
                    yAxisLabelPosition = LabelPosition.LEFT,
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true
                    ),
                    colors = LineGraphColors(
                        lineColor = colorResource(id = R.color.Graph_Line_Color),
                        pointColor = Color.Blue,
                        fillType = LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.Graph_Start_Blue),
                                    colorResource(id = R.color.Graph_End_Blue),
                                    Color.White,
                                    Color.White,
                                    Color.White
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}

@Composable
fun BodyFatPercentGraph(userHist:List<hist>,HeightInCM:Double,Age:Int,Gender:String="M"){
    var datapoints:List<LineData>

    if(Gender=="M"){
        datapoints = userHist.map{
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            val yValue = Calculate.BodyFatPercentforMale(Age, Calculate.BMI(HeightInCM, it.weight))
            LineData(modifiedDate, if (yValue < 0) 0.0 else yValue)
        }
    }
    else{
        datapoints = userHist.map{
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            val yValue = Calculate.BodyFatPercentforFemale(Age, Calculate.BMI(HeightInCM, it.weight))
            LineData(modifiedDate, if (yValue < 0) 0.0 else yValue)
        }
    }
    val width = datapoints.size*100
    LazyRow {
        item {
            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(400.dp)
                    .width(width.dp),
                data = datapoints,
                onPointClick = { value: LineData ->
                    // do something with value
                },
                style = LineGraphStyle(
                    yAxisLabelPosition = LabelPosition.LEFT,
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true
                    ),
                    colors = LineGraphColors(
                        lineColor = colorResource(id = R.color.Graph_Line_Color),
                        pointColor = Color.Blue,
                        fillType = LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.Graph_Start_Blue),
                                    colorResource(id = R.color.Graph_End_Blue),
                                    Color.White,
                                    Color.White,
                                    Color.White
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}

@Composable
fun SkeletalMuscleGraph(userHist:List<hist>,HeightInCM: Double,Age: Int,Gender:String="M") {
    var datapoints: List<LineData>

    if (Gender == "M") {
        datapoints = userHist.map {
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            if(it.impedance!=0) {
                LineData(
                    modifiedDate,
                    Calculate.SkeletalMusscleMassforMale(it.impedance.toDouble(), HeightInCM, Age)
                )
            }
            else{
                LineData( modifiedDate,0.0)

            }}
    } else {
        datapoints = userHist.map {
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            LineData(
                modifiedDate,
                Calculate.SkeletalMusscleMassforFemale(it.impedance.toDouble(), HeightInCM, Age)
            )
        }
    }
    val width = datapoints.size*100
    LazyRow {
        item {
            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(400.dp)
                    .width(width.dp),
                data = datapoints,
                onPointClick = { value: LineData ->
                    // do something with value
                },
                style = LineGraphStyle(
                    yAxisLabelPosition = LabelPosition.LEFT,
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true
                    ),
                    colors = LineGraphColors(
                        lineColor = colorResource(id = R.color.Graph_Line_Color),
                        pointColor = Color.Blue,
                        fillType = LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.Graph_Start_Blue),
                                    colorResource(id = R.color.Graph_End_Blue),
                                    Color.White,
                                    Color.White,
                                    Color.White
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}

@Composable
fun LeanBodyMassGraph(userHist:List<hist>,Age:Int,HeightInCM: Double,Gender: String = "M") {
    var datapoints: List<LineData>

    if (Gender == "M") {
        datapoints = userHist.map {
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            LineData(modifiedDate,Calculate.LeanBodyMass(it.weight,Calculate.BodyFatPercentforMale(Age,Calculate.BMI(HeightInCM,it.weight))))
        }
    }
    else{
        datapoints = userHist.map {
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            LineData(modifiedDate,Calculate.LeanBodyMass(it.weight,Calculate.BodyFatPercentforFemale(Age,Calculate.BMI(HeightInCM,it.weight))))
        }

    }
    val width = datapoints.size*100
    LazyRow {
        item {
    LineGraph(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(400.dp)
            .width(width.dp),
        data = datapoints,
        onPointClick = { value: LineData ->
            // do something with value
        },
        style = LineGraphStyle(
            yAxisLabelPosition = LabelPosition.LEFT,
            visibility = LineGraphVisibility(
                isYAxisLabelVisible = true,
                isGridVisible = true
            ),
            colors = LineGraphColors(
                lineColor = colorResource(id = R.color.Graph_Line_Color),
                pointColor = Color.Blue,
                fillType = LineGraphFillType.Gradient(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.Graph_Start_Blue),
                            colorResource(id = R.color.Graph_End_Blue),
                            Color.White,
                            Color.White,
                            Color.White
                        )
                    )
                )
            )
        )
    )}}
}

@Composable
fun BMRGraph(userHist:List<hist>,Age:Int,HeightInCM: Double,Gender: String = "M") {
    var datapoints: List<LineData>

    if (Gender == "M") {
        datapoints = userHist.map {
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            LineData(modifiedDate,Calculate.BMRforMale(it.weight,HeightInCM,Age))
        }
    }
    else{
        datapoints = userHist.map {
            val modifiedDate =  it.date.substring(0,(it.date).length-5)
            LineData(modifiedDate,Calculate.BMRforFemale(it.weight,HeightInCM,Age))
        }

    }
    val width = datapoints.size*100
    LazyRow {
        item {
            LineGraph(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(400.dp)
                    .width(width.dp),
                data = datapoints,
                onPointClick = { value: LineData ->
                    // do something with value
                },
                style = LineGraphStyle(
                    yAxisLabelPosition = LabelPosition.LEFT,
                    visibility = LineGraphVisibility(
                        isYAxisLabelVisible = true,
                        isGridVisible = true
                    ),
                    colors = LineGraphColors(
                        lineColor = colorResource(id = R.color.Graph_Line_Color),
                        pointColor = Color.Blue,
                        fillType = LineGraphFillType.Gradient(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    colorResource(id = R.color.Graph_Start_Blue),
                                    colorResource(id = R.color.Graph_End_Blue),
                                    Color.White,
                                    Color.White,
                                    Color.White
                                )
                            )
                        )
                    )
                )
            )
        }
    }

}
