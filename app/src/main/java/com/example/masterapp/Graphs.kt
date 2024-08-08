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
import co.yml.charts.common.model.Point
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphColors
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphFillType
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphVisibility
import com.jaikeerthick.composable_graphs.style.LabelPosition


@Composable
fun WeightGraph(userHist: List<hist>,weightUnit:String) {
    val dataPoints = userHist.mapIndexed { index , hist ->
        if(weightUnit == "kg") {
            Point(x = index.toFloat(), y = hist.weight.toFloat())
        }
        else {
            val weightInPounds = Calculate.convertKgToPounds(hist.weight)
            Point(x = index.toFloat(), y = weightInPounds.toFloat())
        }
    }
//    LazyRow {
//        item {
//            LineGraph(
//                modifier = Modifier
//                    .padding(horizontal = 16.dp, vertical = 12.dp)
//                    .height(400.dp)
//                    .width(width.dp),
//                data = datapoints,
//                onPointClick = { value: LineData ->
//
//                },
//                style = LineGraphStyle(
//                    visibility = LineGraphVisibility(
//                        isYAxisLabelVisible = true,
//                        isGridVisible = true,
//                    ),
//                    yAxisLabelPosition = LabelPosition.LEFT,
//                    colors = LineGraphColors(
//                        lineColor = colorResource(id = R.color.Graph_Line_Color),
//                        pointColor = Color.Blue,
//                        fillType = LineGraphFillType.Gradient(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    colorResource(id = R.color.Graph_Start_Blue),
//                                    colorResource(id = R.color.Graph_End_Blue),
//                                    Color.White,
//                                    Color.White,
//                                    Color.White
//                                )
//                            )
//                        )
//                    )
//                )
//            )
//        }
//    }
    CustomGraph(userHist, dataPoints,weightUnit )


}

@Composable
fun BMIGraph(userHist: List<hist>,HeightInCM:Double){

    val datapoints = userHist.mapIndexed{  index , hist ->
        Point(index.toFloat(),Calculate.BMI(HeightInCM,hist.weight).toFloat())
    }
    CustomGraph(userHist, datapoints,"")

}

@Composable
fun BodyWaterPercentGraph(userHist:List<hist>,HeightInCM:Double,Age:Int,Gender:String="M"){
    var datapoints:List<Point>

    if(Gender=="M"){
        datapoints = userHist.mapIndexed{  index , hist ->
            val bwp = Calculate.BodyWaterPercent(Calculate.BodyWaterForMale(Age,HeightInCM,hist.weight),hist.weight)
            if(bwp<=100) {
                Point(index.toFloat(), bwp.toFloat())
            }
            else{
                Point(index.toFloat(), 100f)
            }
        }
    }
    else{
        datapoints = userHist.mapIndexed{  index , hist ->
            val bwp = Calculate.BodyWaterPercent(Calculate.BodyWaterForFemale(Age,HeightInCM,hist.weight),hist.weight)
            if(bwp<=100) {
                Point(index.toFloat(), bwp.toFloat())
            }
            else{
                Point(index.toFloat(), 100f)
            }
        }
    }
    CustomGraph(userHist = userHist, dataPoints = datapoints, Unit = "%")
}

@Composable
fun BodyFatPercentGraph(userHist:List<hist>,HeightInCM:Double,Age:Int,Gender:String="M"){
    var datapoints:List<Point>

    if(Gender=="M"){
        datapoints = userHist.mapIndexed{  index , hist ->
            val yValue = Calculate.BodyFatPercentforMale(Age, Calculate.BMI(HeightInCM, hist.weight)).toFloat()
            Point(index.toFloat(),if (yValue < 0) 0f else yValue)
        }
    }
    else{
        datapoints = userHist.mapIndexed{  index , hist ->
            val yValue = Calculate.BodyFatPercentforFemale(Age, Calculate.BMI(HeightInCM, hist.weight)).toFloat()
            Point(index.toFloat(),if (yValue < 0) 0f else yValue)
        }
    }
    CustomGraph(userHist = userHist, dataPoints = datapoints, Unit = "%")

}

@Composable
fun SkeletalMuscleGraph(userHist:List<hist>,HeightInCM: Double,Age: Int,Gender:String="M") {
    var datapoints: List<Point>

    if (Gender == "M") {
        datapoints = userHist.mapIndexed{  index , hist ->
            if(hist.impedance!=0) {
                Point(
                    index.toFloat(),
                    Calculate.SkeletalMusscleMassforMale(hist.impedance.toDouble(), HeightInCM, Age).toFloat()
                )
            }
            else{
                Point(index.toFloat(),0f)
            }
        }
    } else {
        datapoints = userHist.mapIndexed { index, hist ->
            if (hist.impedance != 0) {
                Point(
                    index.toFloat(),
                    Calculate.SkeletalMusscleMassforFemale(
                        hist.impedance.toDouble(),
                        HeightInCM,
                        Age
                    ).toFloat()
                )
            } else {
                Point(index.toFloat(), 0f)
            }
        }
    }
   CustomGraph(userHist = userHist, dataPoints = datapoints, Unit = "")
}

@Composable
fun LeanBodyMassGraph(userHist:List<hist>,Age:Int,HeightInCM: Double,Gender: String = "M") {
    var datapoints: List<Point>

    if (Gender == "M") {
        datapoints = userHist.mapIndexed{  index , hist ->
            Point(index.toFloat(),Calculate.LeanBodyMass(hist.weight,Calculate.BodyFatPercentforMale(Age,Calculate.BMI(HeightInCM,hist.weight))).toFloat())
        }
    }
    else{
        datapoints = userHist.mapIndexed{  index , hist ->
            Point(index.toFloat(),Calculate.LeanBodyMass(hist.weight,Calculate.BodyFatPercentforFemale(Age,Calculate.BMI(HeightInCM,hist.weight))).toFloat())
        }

    }
    CustomGraph(userHist = userHist, dataPoints = datapoints, Unit = "Kgs")
}

@Composable
fun BMRGraph(userHist:List<hist>,Age:Int,HeightInCM: Double,Gender: String = "M") {
    var datapoints: List<Point>

    if (Gender == "M") {
        datapoints = userHist.mapIndexed{  index , hist ->
            Point(index.toFloat(),Calculate.BMRforMale(hist.weight,HeightInCM,Age).toFloat())
        }
    }
    else{
        datapoints = userHist.mapIndexed{  index , hist ->
            Point(index.toFloat(),Calculate.BMRforFemale(hist.weight,HeightInCM,Age).toFloat())
        }

    }
    CustomGraph(userHist = userHist, dataPoints = datapoints, Unit = "Kcal")

}
