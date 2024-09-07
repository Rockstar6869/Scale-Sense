package com.ujjolch.masterapp

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.masterapp.R

//@Composable
//fun RotatingCustomProgressIndicator(progress: Float, text: String, isRotating: Boolean) {
//    val infiniteTransition = rememberInfiniteTransition()
//    val rotationAngle by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 360f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    val animatedProgress by animateFloatAsState(
//        targetValue = progress,
//        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
//    )
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .size(200.dp)
//    ) {
//        Canvas(
//            modifier = Modifier
//                .size(200.dp)
//                .graphicsLayer(rotationZ = if (isRotating) rotationAngle else 0f)
//        ) {
//            drawCircleProgressIndicator(animatedProgress)
//        }
//        Text(
//            text = text,
//            fontSize = 35.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//    }
//}
//
//fun DrawScope.drawCircleProgressIndicator(progress: Float) {
//    val startAngle = -90f
//    val sweepAngle = 360 * progress
//    val strokeWidth = 8.dp.toPx()
//
//    // Background circle
//    drawCircle(
//        color = Color.Gray.copy(alpha = 0.3f),
//        radius = size.minDimension / 2,
//        style = Stroke(width = strokeWidth)
//    )
//
//    // Progress arc
//    drawArc(
//        color = Color.White,
//        startAngle = startAngle,
//        sweepAngle = sweepAngle,
//        useCenter = false,
//        style = Stroke(width = strokeWidth)
//    )
//}

@Composable
fun RotatingCustomProgressIndicator(progress: Float, textContent: @Composable () -> Unit, isRotating: Boolean) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(if(screenWidthDp<600.dp) 200.dp else 300.dp)
    ) {
        val RimColor = colorResource(id = R.color.Circular_Progress_Rim)
        Canvas(
            modifier = Modifier
                .size(if(screenWidthDp<600.dp) 200.dp else 300.dp)
                .graphicsLayer(rotationZ = if (isRotating) rotationAngle else 0f)
        ) {
            drawCircleProgressIndicator(animatedProgress,RimColor)
        }
        textContent()
    }
}

fun DrawScope.drawCircleProgressIndicator(progress: Float,RimColor:Color) {
    val startAngle = -90f
    val sweepAngle = 360 * progress
    val strokeWidth = 8.dp.toPx()
    val ProgressstrokeWidth = 2.dp.toPx()

    // Background circle
    drawCircle(
        color = RimColor,   //Rim Color
        radius = size.minDimension / 2,
        style = Stroke(width = strokeWidth)
    )

    // Progress arc
    drawArc(
        color = Color.White,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = ProgressstrokeWidth)
    )
}