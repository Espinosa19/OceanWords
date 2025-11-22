package com.proyect.ocean_words.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.math.cos
import kotlin.random.Random

@Composable
fun LoadingScreenOceanWordsAnimated(navController: NavController) {

    val infinite = rememberInfiniteTransition(label = "")

    // Texto animado
    val textoAlpha by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1400, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = ""
    )

    val logoScale by infinite.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            tween(1800, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = ""
    )

    val tituloAlpha = remember { Animatable(0f) }
    val tituloScale = remember { Animatable(0.8f) }

    LaunchedEffect(true) {
        tituloAlpha.animateTo(1f, tween(800))
        tituloScale.animateTo(1f, tween(800))
    }

    // Timer → navegar a LOGIN
    LaunchedEffect(true) {
        delay(3000)
        navController.navigate("login") {
            popUpTo(0)
        }
    }

    Box(Modifier.fillMaxSize()) {

        // Fondo océano con olas 3D y partículas
        OceanBackground3D()

        // Peces y burbujas animados
        AnimatedBubbles(R.drawable.burbujas, 8000, 0.7f, 30, 50)
        AnimatedBubbles(R.drawable.burbujas, 12000, 0.35f, 200, 90)
        AnimatedBubbles(R.drawable.burbujas, 15000, 0.5f, 450, 70)
        AnimatedBubbles(R.drawable.burbujas, 10000, 0.4f, 700, 80)
        AnimatedBubbles(R.drawable.burbujas, 13000, 0.3f, 850, 60)

        AnimatedFish(R.drawable.grupopeces, 160, 14000, false, 420)
        AnimatedFish(R.drawable.pez_naranja, 70, 12000, true, 310)
        AnimatedFish(R.drawable.pescado, 100, 22000, false, 560)
        AnimatedFish(R.drawable.pez_naranja, 50, 10000, false, 220)
        AnimatedFish(R.drawable.pescado, 90, 18000, true, 500)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_ocean),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer {
                        scaleX = logoScale
                        scaleY = logoScale
                    }
            )

            Spacer(Modifier.height(20.dp))

            Text(
                text = "CARGANDO...",
                color = Color.White,
                modifier = Modifier
                    .alpha(textoAlpha * tituloAlpha.value)
                    .graphicsLayer {
                        scaleX = tituloScale.value
                        scaleY = tituloScale.value
                    }
            )

            Spacer(Modifier.height(25.dp))

            OceanLoadingBar()

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Preparando tus aventuras oceánicas...",
                color = Color.White.copy(alpha = textoAlpha)
            )
        }

        Text(
            text = "© INFINITY DEVS",
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
fun OceanBackground3D() {
    val infinite = rememberInfiniteTransition(label = "")

    val waveShift1 by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Restart),
        label = ""
    )
    val waveShift2 by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(18000, easing = LinearEasing), RepeatMode.Restart),
        label = ""
    )
    val lightShift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 800f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing), RepeatMode.Reverse),
        label = ""
    )

    val particles = remember { List(50) { Particle(Random.nextFloat() * 1080, Random.nextFloat() * 1920, Random.nextFloat() * 2 + 1) } }

    Box(Modifier.fillMaxSize().background(Color(0xFF00111A))) {
        // Olas 3D
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            fun drawWave(shift: Float, amplitude: Float, color: Color, yOffset: Float) {
                val path = Path()
                path.moveTo(0f, height / 2 + yOffset)
                for (x in 0..width.toInt() step 5) {
                    val y = (sin((x + shift) * 2 * Math.PI / 300) * amplitude).toFloat() + height / 2 + yOffset
                    path.lineTo(x.toFloat(), y)
                }
                path.lineTo(width, height)
                path.lineTo(0f, height)
                path.close()
                drawPath(path, color)
            }

            drawWave(waveShift1, 25f, Color(0x2200E5FF), 0f)
            drawWave(waveShift2, 40f, Color(0x3300FFFF), 20f)
        }

        // Partículas tipo plancton
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { p ->
                drawCircle(Color(0x33FFFFFF), p.size, Offset(p.x % size.width, p.y % size.height))
            }
        }

        // Destellos de luz
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color(0x33FFFFFF),
                radius = 150f,
                center = Offset(lightShift % size.width, size.height / 3)
            )
            drawCircle(
                color = Color(0x22FFFFFF),
                radius = 100f,
                center = Offset((lightShift * 0.5f) % size.width, size.height / 2)
            )
        }
    }
}

data class Particle(var x: Float, var y: Float, val size: Float)

@Composable
fun OceanLoadingBar() {
    val infinite = rememberInfiniteTransition(label = "")

    val progress by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2500, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = ""
    )

    Box(
        Modifier
            .width(260.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0x8022CFE6))
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .clip(RoundedCornerShape(40.dp))
                .background(Color(0xFF00E5FF))
        )
    }
}

@Composable
fun AnimatedFish(
    drawableId: Int,
    size: Int,
    duration: Int,
    reverse: Boolean,
    startY: Int
) {
    val infinite = rememberInfiniteTransition(label = "")

    val startX = if (reverse) 1200f else -400f
    val endX = if (reverse) -400f else 1200f

    val offsetX by infinite.animateFloat(
        initialValue = startX,
        targetValue = endX,
        animationSpec = infiniteRepeatable(
            tween(duration, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = ""
    )

    val offsetY by infinite.animateFloat(
        initialValue = startY.toFloat(),
        targetValue = startY.toFloat() + 30f,
        animationSpec = infiniteRepeatable(
            tween(duration / 4, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = ""
    )

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = null,
        modifier = Modifier
            .size(size.dp)
            .offset(offsetX.dp, offsetY.dp)
    )
}

@Composable
fun AnimatedBubbles(
    drawableId: Int,
    duration: Int,
    alpha: Float,
    startX: Int,
    startSize: Int
) {
    val infinite = rememberInfiniteTransition(label = "")

    val offsetY by infinite.animateFloat(
        initialValue = 1100f,
        targetValue = -200f,
        animationSpec = infiniteRepeatable(
            tween(duration, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = ""
    )

    val sizeAnim by infinite.animateFloat(
        initialValue = startSize.toFloat(),
        targetValue = startSize * 1.3f,
        animationSpec = infiniteRepeatable(
            tween(duration / 2, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = ""
    )

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = null,
        modifier = Modifier
            .size(sizeAnim.dp)
            .offset(startX.dp, offsetY.dp)
            .alpha(alpha)
    )
}
