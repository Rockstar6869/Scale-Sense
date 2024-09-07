package com.ujjolch.masterapp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masterapp.R

@Composable
fun CustomSlider3(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    blueText: String,
    greenText: String,
    orangeText: String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange = colorResource(id = R.color.Slider_Orange)
        val totalRange = blueRange.endInclusive + (greenRange.endInclusive - greenRange.start) + (orangeRange.endInclusive - orangeRange.start)


        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (Modifier.fillMaxWidth(0.3f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(),
                        textAlign = TextAlign.Center
                    )
                }
                Row (Modifier.fillMaxWidth(0.6f),
                    horizontalArrangement = Arrangement.End) {
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = (blueRange.endInclusive / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width

                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = orangeText,
                    modifier = Modifier.weight(1f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..totalRange,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}


@Composable
fun BodyWaterPercentSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    orangeText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange= colorResource(id = R.color.Slider_Orange)
        val totalRange = (blueRange.endInclusive-blueRange.start) + (greenRange.endInclusive - greenRange.start) + (orangeRange.endInclusive - orangeRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (Modifier.fillMaxWidth(0.33f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(),
                        textAlign = TextAlign.Center
                    )
                }
                Row (Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = ((blueRange.endInclusive-blueRange.start) / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 30.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 16.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = orangeText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 50.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 35f..80f,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun CustomSlider4(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    redRange: ClosedFloatingPointRange<Float>,
    blueText: String,
    greenText: String,
    orangeText: String,
    redText: String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange = colorResource(id = R.color.Slider_Orange)
        val red = Color.Red
        val totalRange = blueRange.endInclusive +
                (greenRange.endInclusive - greenRange.start) +
                (orangeRange.endInclusive - orangeRange.start) +
                (redRange.endInclusive - redRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = blueRange.endInclusive.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 70.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenRange.endInclusive.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 60.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = orangeRange.endInclusive.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 50.dp),
                    textAlign = TextAlign.Center
                )
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight.dp)
                    .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = (blueRange.endInclusive / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width
                val redWidth = ((redRange.endInclusive - redRange.start) / totalRange) * width

                // Draw the first part of the track (blue)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (orange)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the fourth part of the track (red)
                drawLine(
                    color = red,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth + redWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 20.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier =Modifier.padding(start = 10.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = orangeText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 30.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = redText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 40.dp),
                    textAlign = TextAlign.Center
                )
            }



            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..totalRange,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun BodyFatPercentSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    redRange: ClosedFloatingPointRange<Float>,
    blueText: String,
    greenText: String,
    orangeText: String,
    redText: String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange = colorResource(id = R.color.Slider_Orange)
        val red = Color.Red
        val totalRange = blueRange.endInclusive +
                (greenRange.endInclusive - greenRange.start) +
                (orangeRange.endInclusive - orangeRange.start) +
                (redRange.endInclusive - redRange.start)



        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row (Modifier.fillMaxWidth(0.1578f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
                Row (Modifier.fillMaxWidth(0.5189f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
                Row (Modifier.fillMaxWidth(0.6105f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = orangeRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight.dp)
                    .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = (blueRange.endInclusive / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width
                val redWidth = ((redRange.endInclusive - redRange.start) / totalRange) * width

                // Draw the first part of the track (blue)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (orange)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the fourth part of the track (red)
                drawLine(
                    color = red,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth + redWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 15.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = orangeText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = redText,
                    fontSize = 14.sp,
                    modifier =Modifier
                        .padding(end = 30.dp),
                    textAlign = TextAlign.Center
                )
            }



            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..totalRange,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun CustomSlider3Green(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    darkGreenRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    darkGreenText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val darkGreen= colorResource(id = R.color.Slider_DarkGreen)
        val totalRange = blueRange.endInclusive + (greenRange.endInclusive - greenRange.start) + (darkGreenRange.endInclusive - darkGreenRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueRange.endInclusive.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 100.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenRange.endInclusive.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 100.dp),
                    textAlign = TextAlign.Center
                )
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = (blueRange.endInclusive / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((darkGreenRange.endInclusive - darkGreenRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = darkGreen,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 50.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = darkGreenText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 50.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..totalRange,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}
@Composable
fun BMISlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    redRange: ClosedFloatingPointRange<Float>,
    blueText: String,
    greenText: String,
    orangeText: String,
    redText: String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange = colorResource(id = R.color.Slider_Orange)
        val red = Color.Red
        val totalRange = blueRange.endInclusive +
                (greenRange.endInclusive - greenRange.start) +
                (orangeRange.endInclusive - orangeRange.start) +
                (redRange.endInclusive - redRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = greenRange.endInclusive.toString(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Row (horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth(0.3f)){
                    Text(
                        text = orangeRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(trackHeight.dp)
                    .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = (blueRange.endInclusive / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width
                val redWidth = ((redRange.endInclusive - redRange.start) / totalRange) * width

                // Draw the first part of the track (blue)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (orange)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the fourth part of the track (red)
                drawLine(
                    color = red,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth + redWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(2f)
                )
                Row (Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.SpaceBetween){

                    Text(
                        text = greenText,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = orangeText,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = redText,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }



            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..totalRange,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun LeanBodyMassPercentSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    orangeText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange= colorResource(id = R.color.Slider_Orange)
        val totalRange = (blueRange.endInclusive-blueRange.start) + (greenRange.endInclusive - greenRange.start) + (orangeRange.endInclusive - orangeRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (Modifier.fillMaxWidth(0.33f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Row(Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = ((blueRange.endInclusive-blueRange.start) / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                        Text(
                            text = blueText,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = greenText,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = orangeText,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .weight(1f),
                            textAlign = TextAlign.Center
                        )

            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 46f..112f,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun BMRSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    darkGreenRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    darkGreenText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val darkGreen= colorResource(id = R.color.Slider_DarkGreen)
        val totalRange = (blueRange.endInclusive-blueRange.start) + (greenRange.endInclusive - greenRange.start) + (darkGreenRange.endInclusive - darkGreenRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(0.33f)) {
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
                Row(horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = ((blueRange.endInclusive-blueRange.start) / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val darkGreenWidth = ((darkGreenRange.endInclusive - darkGreenRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = darkGreen,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + darkGreenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 50.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start =40.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = darkGreenText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 50.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 1250f..1850f,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun BonePercentSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    darkGreenRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    darkGreenText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val darkGreen= colorResource(id = R.color.Slider_DarkGreen)
        val totalRange = (blueRange.endInclusive-blueRange.start) + (greenRange.endInclusive - greenRange.start) + (darkGreenRange.endInclusive - darkGreenRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (Modifier.fillMaxWidth(0.33f),
                    horizontalArrangement = Arrangement.End){
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
                Row (Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = ((blueRange.endInclusive-blueRange.start) / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val darkGreenWidth = ((darkGreenRange.endInclusive - darkGreenRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = darkGreen,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + darkGreenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = darkGreenText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 5f..20f,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun ProteinPercentSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    orangeText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange= colorResource(id = R.color.Slider_Orange)
        val totalRange = (blueRange.endInclusive-blueRange.start) + (greenRange.endInclusive - greenRange.start) + (orangeRange.endInclusive - orangeRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(Modifier.fillMaxWidth(0.33f),
                    horizontalArrangement = Arrangement.End) {
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
                Row (Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.Start){
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = ((blueRange.endInclusive-blueRange.start) / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp)
            ) {
                    Text(
                        text = blueText,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = greenText,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = orangeText,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 11.9f..24.2f,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun MusclePercentSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    darkGreenRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    darkGreenText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val darkGreen= colorResource(id = R.color.Slider_DarkGreen)
        val totalRange = (blueRange.endInclusive-blueRange.start) + (greenRange.endInclusive - greenRange.start) + (darkGreenRange.endInclusive - darkGreenRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (Modifier.fillMaxWidth(0.33f),
                    horizontalArrangement = Arrangement.End){

                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }

                Row(Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = ((blueRange.endInclusive - blueRange.start) / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((darkGreenRange.endInclusive - darkGreenRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = darkGreen,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp)
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = darkGreenText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            // Draw the Slider

            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 65f..89f,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun VisceralFatIndexSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    redRange: ClosedFloatingPointRange<Float>,
    greenText:String,
    orangeText:String,
    redText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val green = colorResource(id = R.color.Slider_Green)
        val orange= colorResource(id = R.color.Slider_Orange)
        val totalRange = greenRange.endInclusive + (orangeRange.endInclusive - orangeRange.start) + (redRange.endInclusive - redRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (Modifier.fillMaxWidth(0.4f),
                    horizontalArrangement = Arrangement.End
                ){
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
                Row(Modifier.fillMaxWidth(0.57f),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = orangeRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = (greenRange.endInclusive / totalRange) * width
                val greenWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width
                val orangeWidth = ((redRange.endInclusive - redRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = Color.Red,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 40.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = orangeText,
                    fontSize = 14.sp,
                    modifier = Modifier,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = redText,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 30.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..totalRange,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun SubcutaneousFatSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    blueRange: ClosedFloatingPointRange<Float>,
    greenRange: ClosedFloatingPointRange<Float>,
    orangeRange: ClosedFloatingPointRange<Float>,
    blueText:String,
    greenText:String,
    orangeText:String,
    trackHeight: Float = 32f
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val blue = colorResource(id = R.color.Slider_Blue)
        val green = colorResource(id = R.color.Slider_Green)
        val orange= colorResource(id = R.color.Slider_Orange)
        val totalRange = blueRange.endInclusive + (greenRange.endInclusive - greenRange.start) + (orangeRange.endInclusive - orangeRange.start)

        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(Modifier.fillMaxWidth(0.33f),
                    horizontalArrangement = Arrangement.End) {
                    Text(
                        text = blueRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
                Row(Modifier.fillMaxWidth(0.5f),
                    horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = greenRange.endInclusive.toString(),
                        fontSize = 14.sp,
                        modifier = Modifier,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight.dp)
                .align(Alignment.CenterStart)
            ) {
                val width = size.width
                val blueWidth = (blueRange.endInclusive / totalRange) * width
                val greenWidth = ((greenRange.endInclusive - greenRange.start) / totalRange) * width
                val orangeWidth = ((orangeRange.endInclusive - orangeRange.start) / totalRange) * width

                // Draw the first part of the track (red)
                drawLine(
                    color = blue,
                    start = androidx.compose.ui.geometry.Offset(0f, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the second part of the track (green)
                drawLine(
                    color = green,
                    start = androidx.compose.ui.geometry.Offset(blueWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )

                // Draw the third part of the track (magenta)
                drawLine(
                    color = orange,
                    start = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth, center.y),
                    end = androidx.compose.ui.geometry.Offset(blueWidth + greenWidth + orangeWidth, center.y),
                    strokeWidth = trackHeight,
                    cap = StrokeCap.Round
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = blueText,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = greenText,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = orangeText,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }

            // Draw the Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..totalRange,  // Set the total range for the slider
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                )
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun CustomSlider3Preview() {
    var sliderValue by remember { mutableStateOf(0f) }
    CustomSlider3(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 0f..40f,  // Define the maximum value for the red range
        greenRange = 40f..80f,  // Define the maximum value for the green range
        orangeRange = 80f..120f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Standard",
        orangeText = "High"
    )
}

@Preview(showBackground = true)
@Composable
fun CustomSlider4Preview() {
    var sliderValue by remember { mutableStateOf(0f) }
    CustomSlider4(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 0f..50f,  // Define the maximum value for the red range
        greenRange = 50f..100f,  // Define the maximum value for the green range
        orangeRange = 100f..150f, // Define the maximum value for the magenta range
        redRange = 150f..200f,
        blueText = "Low",
        greenText = "Standard",
        orangeText = "High",
        redText = "Over"
    )
}

@Preview(showBackground = true)
@Composable
fun CustomSlider3GreenPreview() {
    var sliderValue by remember { mutableStateOf(0f) }
    CustomSlider3Green(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 0f..50f,  // Define the maximum value for the red range
        greenRange = 50f..100f,  // Define the maximum value for the green range
        darkGreenRange = 100f..150f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Good",
        darkGreenText = "Excellent"
    )
}

@Preview(showBackground = true)
@Composable
fun BMISliderPreview() {
    var sliderValue by remember { mutableStateOf(0f) }
    BMISlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 0f..18.5f,  // Define the maximum value for the red range
        greenRange = 18.5f..25f,  // Define the maximum value for the green range
        orangeRange = 25f..30f, // Define the maximum value for the magenta range
        redRange = 30f..35f,
        blueText = "Underweight",
        greenText = "Normal",
        orangeText = "High",
        redText = "Overweight"
    )
}

@Preview(showBackground = true)
@Composable
fun BodyWaterPercentPreview() {
    var sliderValue by remember { mutableStateOf(65f) }
    BodyWaterPercentSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 35f..50f,  // Define the maximum value for the red range
        greenRange = 50f..65f,  // Define the maximum value for the green range
        orangeRange = 65f..80f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Standard",
        orangeText = "High"
    )
}

@Preview(showBackground = true)
@Composable
fun BodyFatPercentSliderPreview() {
    var sliderValue by remember { mutableStateOf(0f) }
    BodyFatPercentSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 0f..6f,  // Define the maximum value for the red range
        greenRange = 6f..22f,  // Define the maximum value for the green range
        orangeRange = 22f..27f, // Define the maximum value for the magenta range
        redRange = 27f..38f,
        blueText = "Low",
        greenText = "Standard",
        orangeText = "High",
        redText = "Over"
    )
}

@Preview(showBackground = true)
@Composable
fun LeanBodyMassPercentSliderPreview() {
    var sliderValue by remember { mutableStateOf(100f) }
    LeanBodyMassPercentSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 46f..68f,  // Define the maximum value for the red range
        greenRange = 68f..90f,  // Define the maximum value for the green range
        orangeRange = 90f..112f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Standard",
        orangeText = "High"
    )
}

@Preview(showBackground = true)
@Composable
fun BMRSliderPreview() {
    var sliderValue by remember { mutableStateOf(1850f) }
    BMRSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 1250f..1450f,  // Define the maximum value for the red range
        greenRange = 1450f..1650f,  // Define the maximum value for the green range
        darkGreenRange = 1650f..1850f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Good",
        darkGreenText = "Excellent"
    )
}

@Preview(showBackground = true)
@Composable
fun BonePercentSliderPreview() {
    var sliderValue by remember { mutableStateOf(0f) }
    BonePercentSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 5f..10f,  // Define the maximum value for the red range
        greenRange = 10f..15f,  // Define the maximum value for the green range
        darkGreenRange = 15f..20f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Standard",
        darkGreenText = "Excellent"
    )
}

@Preview(showBackground = true)
@Composable
fun ProteinPercentSliderPreview() {
    var sliderValue by remember { mutableStateOf(0f) }
    ProteinPercentSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 11.9f..16f,  // Define the maximum value for the red range
        greenRange = 16f..20.1f,  // Define the maximum value for the green range
        orangeRange = 20.1f..24.2f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Standard",
        orangeText = "High"
    )
}

@Preview(showBackground = true)
@Composable
fun MusclePercentSliderPreview() {
    var sliderValue by remember { mutableStateOf(67f) }
    MusclePercentSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 65f..73f,  // Define the maximum value for the red range
        greenRange = 73f..81f,  // Define the maximum value for the green range
        darkGreenRange = 81f..89f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Standard",
        darkGreenText = "Excellent"
    )
}

@Preview(showBackground = true)
@Composable
fun VisceralFatIndexSliderPreview() {
    var sliderValue by remember { mutableStateOf(0f) }
    VisceralFatIndexSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        greenRange = 0f..10f,  // Define the maximum value for the red range
        orangeRange = 10f..17f,  // Define the maximum value for the green range
        redRange = 17f..25f,  // Define the maximum value for the magenta range
        greenText = "Standard",
        orangeText = "High",
        redText = "Over"
    )
}

@Preview(showBackground = true)
@Composable
fun SubcutaneousFatPreview() {
    var sliderValue by remember { mutableStateOf(0f) }
    SubcutaneousFatSlider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        blueRange = 0f..5f,  // Define the maximum value for the red range
        greenRange = 5f..10.5f,  // Define the maximum value for the green range
        orangeRange = 10.5f..15f,  // Define the maximum value for the magenta range
        blueText = "Low",
        greenText = "Standard",
        orangeText = "High"
    )
}