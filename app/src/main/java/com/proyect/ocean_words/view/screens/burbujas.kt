package com.proyect.ocean_words.view.screens


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.BurbujaEstado
import kotlin.random.Random
import kotlin.math.sin

private val bubbles = List(75) {
    BurbujaEstado(
        initialX = Random.nextFloat(),
        delay = Random.nextInt(0, 3000),
        duration = Random.nextInt(4000, 8000),
        sizeRange = Random.nextFloat() *150f + 40f,
        horizontalWave = Random.nextFloat() * 20f + 10f
    )
}

@Composable
fun burbujas(
    modifier: Modifier = Modifier,
) {
    val bubbleBitmap = ImageBitmap.imageResource(id = R.drawable.burbujas)

    val infiniteTransition = rememberInfiniteTransition(label = "bubble_transition")
    val timeFactor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "time_factor"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val imageSize = bubbleBitmap.width.toFloat()

        bubbles.forEach { bubble ->
            val elapsed = (timeFactor * 10000).toLong()
            val localTime = ((elapsed - bubble.delay).toFloat() / bubble.duration)
                .coerceIn(0f, 1f)

            if (localTime > 0f) {
                val yProgress = 1f - localTime
                val horizontalOffset = sin((timeFactor * 2f + bubble.initialX) * 2f * Math.PI.toFloat()) * bubble.horizontalWave

                val xCenter = bubble.initialX * width + horizontalOffset
                val yCenter = yProgress * (height + 100.dp.toPx()) - 100.dp.toPx()

                val currentSize = bubble.sizeRange * localTime.coerceAtLeast(0.5f)

                val scaleFactor = currentSize / imageSize

                with(drawContext.canvas.nativeCanvas) {
                    val originalSave = save()

                    translate(xCenter, yCenter)

                    scale(scaleFactor * 2, scaleFactor * 2)

                    drawImage(
                        image = bubbleBitmap,
                        topLeft = Offset(-imageSize / 2f, -imageSize / 2f),
                        alpha = 1.0f
                    )

                    restoreToCount(originalSave)
                }
            }
        }
    }
}