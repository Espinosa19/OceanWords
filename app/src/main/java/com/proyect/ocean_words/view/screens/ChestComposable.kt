package com.proyect.ocean_words.view.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import com.proyect.ocean_words.model.Bubble
import kotlin.random.Random

/* ---------------------------------------------------
   PANTALLA PRINCIPAL
--------------------------------------------------- */
@Composable
fun OceanChestScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B5ED7))
    ) {

        // Ondas de luz submarina
        LightRaysCanvas()

        // Burbujas animadas
        AnimatedBubblesCanvas()

        // Peces de fondo
        FishShadowCanvas()

        // Cofre al centro
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedOceanChest()
        }
    }
}@Composable
fun AnimatedOceanChest() {

    var openChest by remember { mutableStateOf(false) }

    /* ------------------ TRANSICIÓN PRINCIPAL ------------------ */
    val transition = updateTransition(
        targetState = openChest,
        label = "ChestTransition"
    )

    // Rotación principal de la tapa
    val lidRotation by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                keyframes {
                    durationMillis = 1200
                    0f at 0
                    12f at 150      // rebote previo
                    -90f at 900
                    -115f at 1200  // apertura final
                }
            } else {
                tween(600)
            }
        },
        label = "LidRotation"
    ) { opened ->
        if (opened) -115f else 0f
    }

    // Flotación constante del cofre
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -14f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Pequeño shake al abrir
    val shake by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                keyframes {
                    durationMillis = 600
                    0f at 0
                    -6f at 100
                    6f at 200
                    -4f at 300
                    4f at 400
                    0f at 600
                }
            } else {
                tween(300)
            }
        },
        label = "Shake"
    ) { 0f }

    // Glow mágico
    val glow by transition.animateFloat(
        transitionSpec = { tween(800) },
        label = "Glow"
    ) { opened ->
        if (opened) 1f else 0.3f
    }

    Box(
        modifier = Modifier
            .size(260.dp)
            .offset(y = floatAnim.dp)
            .clickable { openChest = !openChest },
        contentAlignment = Alignment.Center
    ) {

        // ✨ Partículas mágicas intensas
        MagicParticlesCanvas(isActive = openChest)

        // ✨ Aura mágica
        Box(
            modifier = Modifier
                .size(200.dp)
                .shadow(
                    elevation = (40 * glow).dp,
                    shape = CircleShape,
                    ambientColor = Color(0xFFFFD700).copy(alpha = glow),
                    spotColor = Color(0xFFFFF176).copy(alpha = glow)
                )
        )

        // 🧰 BASE DEL COFRE
        Box(
            modifier = Modifier
                .offset(x = shake.dp)
                .size(width = 220.dp, height = 130.dp)
                .background(
                    color = Color(0xFF8D6E63),
                    shape = RoundedCornerShape(24.dp)
                )
        )

        // 🧰 TAPA (APERTURA ROBUSTA)
        Box(
            modifier = Modifier
                .offset(x = shake.dp)
                .size(width = 220.dp, height = 90.dp)
                .graphicsLayer {
                    rotationX = lidRotation
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    cameraDistance = 18 * density
                }
                .background(
                    color = Color(0xFFA1887F),
                    shape = RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp
                    )
                )
                .align(Alignment.TopCenter)
        )
        ChestBase(shake)

        ChestLid(lidRotation, shake)

        // 💎 PERLA (RECOMPENSA)
        if (openChest) {
            MagicalPearl()
        }
    }
}
@Composable
fun ChestBase(shake: Float) {
    Box(
        modifier = Modifier
            .offset(x = shake.dp)
            .size(220.dp, 130.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF8D6E63),
                        Color(0xFF6D4C41)
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = 18.dp,
                    topEnd = 18.dp,
                    bottomStart = 28.dp,
                    bottomEnd = 28.dp
                )
            )
    ) {

        // Líneas de madera
        Canvas(Modifier.fillMaxSize()) {
            val plankColor = Color(0xFF5D4037).copy(alpha = 0.4f)
            val spacing = size.height / 5

            repeat(4) {
                drawLine(
                    plankColor,
                    Offset(0f, spacing * (it + 1)),
                    Offset(size.width, spacing * (it + 1)),
                    6f
                )
            }
        }

        // Bandas doradas
        ChestGoldBand(
            modifier = Modifier.align(Alignment.CenterStart)
        )
        ChestGoldBand(
            modifier = Modifier.align(Alignment.CenterEnd)
        )

        // Cerradura
        ChestLock(
            modifier = Modifier.align(Alignment.Center)
        )

        // Clavos dorados
        ChestNails()
    }
}


@Composable
fun ChestGoldBand(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(16.dp)
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF176),
                        Color(0xFFFFC107)
                    )
                ),
                shape = RoundedCornerShape(10.dp)
            )
    )
}
@Composable
fun ChestLock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFF59D),
                        Color(0xFFFFA000)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(22.dp)) {
            drawCircle(
                color = Color(0xFF5D4037),
                radius = size.minDimension / 3,
                center = center
            )
        }
    }
}
@Composable
fun ChestNails() {
    Canvas(Modifier.fillMaxSize()) {
        val nailColor = Color(0xFFFFD54F)
        val radius = 4f

        val positions = listOf(
            Offset(24f, 24f),
            Offset(size.width - 24f, 24f),
            Offset(24f, size.height - 24f),
            Offset(size.width - 24f, size.height - 24f)
        )

        positions.forEach {
            drawCircle(nailColor, radius, it)
        }
    }
}
@Composable
fun SandBase() {
    Canvas(
        modifier = Modifier
            .size(260.dp, 60.dp)
            .offset(y = 90.dp)
    ) {
        drawOval(
            color = Color(0xFFFFCC80),
            size = size
        )
    }
}



/* ---------------------------------------------------
   TAPA DEL COFRE (DETALLADA)
--------------------------------------------------- */
@Composable
fun ChestLid(rotationX: Float, shake: Float) {
    Box(
        modifier = Modifier
            .offset(x = shake.dp)
            .size(220.dp, 95.dp)
            .graphicsLayer {
                this.rotationX = rotationX
                transformOrigin = TransformOrigin(0.5f, 0f)
                cameraDistance = 20 * density
            }
    ) {
        Canvas(Modifier.fillMaxSize()) {

            // Forma curva tipo barril
            val path = Path().apply {
                moveTo(0f, size.height)
                quadraticBezierTo(
                    size.width / 2,
                    -size.height * 0.6f,
                    size.width,
                    size.height
                )
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFA1887F),
                        Color(0xFF6D4C41)
                    )
                )
            )

            // Línea central dorada
            drawRoundRect(
                color = Color(0xFFFFD54F),
                topLeft = Offset(size.width / 2 - 8f, 0f),
                size = Size(16f, size.height),
                cornerRadius = CornerRadius(12f)
            )
        }
    }
}

@Composable
fun MagicalPearl() {

    // Animación infinita de flotación
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -22f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    // Animación de brillo (pulse)
    val glowAnim by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Box(
        modifier = Modifier
            .offset(y = floatAnim.dp)
            .size(56.dp)
            .graphicsLayer {
                scaleX = glowAnim
                scaleY = glowAnim
            }
            .shadow(
                elevation = 30.dp,
                shape = CircleShape,
                ambientColor = Color(0xFFFFF9C4),
                spotColor = Color(0xFFFFF176)
            )
            .background(
                color = Color(0xFFFFFDE7),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Brillo interno
        Canvas(modifier = Modifier.size(40.dp)) {
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = size.minDimension / 4,
                center = center
            )
        }
    }
}

@Composable
fun AnimatedBubblesCanvas() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val moveY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(30) {
            val x = Random.nextFloat() * size.width
            val y = (Random.nextFloat() * size.height + moveY) % size.height
            val radius = Random.nextInt(6, 16).toFloat()

            drawCircle(
                color = Color.White.copy(alpha = 0.25f),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
@Composable
fun MagicParticlesCanvas(isActive: Boolean) {
    if (!isActive) return

    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        0.2f, 0.8f,
        infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Canvas(
        modifier = Modifier
            .size(220.dp)
    ) {
        repeat(20) {
            val x = Random.nextFloat() * size.width
            val y = Random.nextFloat() * size.height
            val radius = Random.nextInt(3, 6).toFloat()

            drawCircle(
                color = Color(0xFFFFF176).copy(alpha = alpha),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}
@Composable
fun LightRaysCanvas() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            tween(5000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(6) {
            val x = Random.nextFloat() * size.width

            drawRect(
                color = Color.Cyan.copy(alpha = alpha),
                topLeft = Offset(x, 0f),
                size = Size(120f, size.height)
            )
        }
    }
}
@Composable
fun FishShadowCanvas() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val moveX by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 1400f,
        animationSpec = infiniteRepeatable(
            tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(4) {
            val y = Random.nextFloat() * size.height * 0.7f

            drawOval(
                color = Color.Black.copy(alpha = 0.08f),
                topLeft = Offset(moveX + it * 300, y),
                size = Size(120f, 40f)
            )
        }
    }
}
