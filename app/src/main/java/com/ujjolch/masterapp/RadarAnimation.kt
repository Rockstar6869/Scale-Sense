package com.ujjolch.masterapp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.masterapp.R
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RadarAnimation(isAnimating: Boolean) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidthDp = configuration.screenWidthDp.dp

    val circleCount = 3 // Number of circles to animate
    val initialRadius = if(screenHeight>700.dp)  40f else 20f // Initial radius of the circle
    val maxRadius = if(screenHeight>700.dp)  300f else 150f // Maximum radius before the circle disappears
    val circleDuration = 5000 // Duration for each circle's animation (ms) - Slower

    // List of circles with their animation progress
    val circleList = remember { List(circleCount) { Animatable(initialRadius) } }
    val alphaList = remember { List(circleCount) { Animatable(1f) } }

    // Remember coroutine scope for controlling the animation
    val coroutineScope = rememberCoroutineScope()

    // Function to start the animations
    fun startAnimations() {
        // Loop through each circle and animate it
        circleList.forEachIndexed { index, animatable ->
            coroutineScope.launch {
                delay(index * (circleDuration / circleCount).toLong())
                launch {
                    animatable.animateTo(
                        targetValue = maxRadius,
                        animationSpec = infiniteRepeatable(
                            animation = tween(circleDuration, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
        }

        // Loop through each circle and animate its alpha value
        alphaList.forEachIndexed { index, alphaAnimatable ->
            coroutineScope.launch {
                delay(index * (circleDuration / circleCount).toLong())
                launch {
                    alphaAnimatable.animateTo(
                        targetValue = 0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(circleDuration, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                }
            }
        }
    }

    // Function to reset the animations
    fun resetAnimations() {
        coroutineScope.launch {
            circleList.forEach { it.snapTo(initialRadius) }
            alphaList.forEach { it.snapTo(1f) }
        }
    }

    // Start or stop the animations based on the boolean variable
    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            resetAnimations()
            startAnimations()
        } else {
            coroutineScope.coroutineContext.cancelChildren() // Cancel all animations when pausing
        }
    }

    // Drawing the circles
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxHeight(0.3f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val color = colorResource(id = R.color.Home_Screen_Blue)
        Canvas(modifier = Modifier) {
            val center = Offset(size.width / 2, size.height / 2)

            circleList.forEachIndexed { index, animatable ->
                val radius = animatable.value
                val alpha = alphaList[index].value
                val color = color.copy(alpha = alpha)

                drawCircle(
                    color = color,
                    radius = radius,
                    center = center
                )
            }
        }
    }
}