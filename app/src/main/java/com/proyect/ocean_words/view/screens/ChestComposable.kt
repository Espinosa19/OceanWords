import android.graphics.Camera
import android.graphics.Matrix as AndroidMatrix
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.hypot
import kotlin.math.sin
import com.proyect.ocean_words.R
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import com.proyect.ocean_words.view.theme.BricolageGrotesque
import com.proyect.ocean_words.view.theme.LightOlive


@Composable
fun RealisticStylizedChest(
    especieId: String,
    levelId: String,
    imagenEncoded: Int,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isOpen by remember { mutableStateOf(false) }
    var yaAbierto by remember { mutableStateOf(false) } // <- nueva variable

    var mostrarPremios by remember { mutableStateOf(true) }
    val openProgress by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = spring(0.55f, Spring.StiffnessLow),
        label = "ChestAnimation"
    )

    // Animación de levitación para el tesoro interno
    val infiniteTransition = rememberInfiniteTransition(label = "PearlAnim")
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "Float"
    )
    val glowTransition = rememberInfiniteTransition(label = "GlowAnim")
    val glowGrowth by animateFloatAsState(
        targetValue = if (openProgress > 0.15f) openProgress else 0f,
        animationSpec = tween(
            durationMillis = 900,
            easing = FastOutSlowInEasing
        ),
        label = "GlowGrowth"
    )

    val glowPulse by glowTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowPulse"
    )




    Box(
        modifier = modifier.fillMaxSize().clickable(enabled = !yaAbierto) { // <- solo clic si no se ha abierto
            isOpen = true
            yaAbierto = true
        },
        contentAlignment = Alignment.Center
    ) {

        val outlineColor = Color(0xFF2B1506)


        val chestSize = 320.dp
        val chestSizePx = with(LocalDensity.current) { chestSize.toPx() }

        val chestTopY = (chestSizePx / 2f) - (chestSizePx * 0.55f / 2f)

        val centerX = chestSizePx / 2f
        val centerY = chestSizePx / 2f + 30f
        Canvas(modifier = Modifier.size(320.dp)) {

            val w = size.width * 0.75f
            val h = w * 0.55f

            // 1. Sombra de suelo
            drawOval(
                color = Color.Black.copy(alpha = 0.2f),
                topLeft = Offset(centerX - w/2, centerY + h - 10f),
                size = Size(w, 30f)
            )

            // 2. Fondo interno (Solo se ve al abrirse)
            if (openProgress > 0.1f) {
                val minGlowRadius = 0.5f

                // Fondo interno oscuro

                val strength = glowGrowth.coerceIn(0f, 1f)
                val screenRadius = hypot(size.width, size.height)

// crecimiento NO lineal (más natural)
                val growEase = strength * strength

                val baseRadius = (w * 0.9f * growEase * glowPulse)
                    .coerceAtLeast(minGlowRadius)

                /* =========================
                   🔆 NÚCLEO ENERGÉTICO
                   ========================= */
                if (strength > 0.02f) {

                    /* =========================
                       🔆 NÚCLEO ENERGÉTICO
                       ========================= */
                    drawCircle(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.0f to Color(0xFFFFFFF0).copy(alpha = 0.85f * strength),
                                0.15f to Color(0xFFFFE082).copy(alpha = 0.60f * strength),
                                0.35f to Color(0xFFFFC107).copy(alpha = 0.40f * strength),
                                0.55f to Color(0xFFFFA000).copy(alpha = 0.25f * strength),
                                0.75f to Color(0xFFFF6F00).copy(alpha = 0.12f * strength),
                                0.90f to Color(0xFFFF6F00).copy(alpha = 0.05f * strength),
                                1.0f to Color.Transparent
                            ),
                            center = Offset(centerX, centerY - 30f),
                            radius = screenRadius * glowPulse
                        ),
                        center = Offset(centerX, centerY - 30f),
                        radius = screenRadius * glowPulse
                    )



                }


                /* =========================
                   🌟 ONDA EXPANSIVA
                   ========================= */
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFE082).copy(alpha = 0.35f * strength),
                            Color.Transparent
                        )
                    ),
                    center = Offset(centerX, centerY - 20f),
                    radius = baseRadius * 1.4f
                )
                // 🫧 Perla mágica flotante

            }



            // 3. BASE DEL COFRE
            drawDetailedBase(centerX, centerY, w, h, outlineColor)
// 1. SOMBRA DE BASE (PESO Y PROFUNDIDAD)
            val shadowY = centerY + h - (-90f)

// Núcleo oscuro (contacto con el suelo)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.45f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, shadowY),
                    radius = w * 0.45f
                ),
                topLeft = Offset(centerX - w * 0.45f, shadowY - 12f),
                size = Size(w * 0.9f, 72f)
            )

// Difuminado exterior (suavidad)
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, shadowY + 6f),
                    radius = w * 0.6f
                ),
                topLeft = Offset(centerX - w * 0.6f, shadowY - 6f),
                size = Size(w * 1.2f, 36f)
            )

            // 4. TAPA CON PERSPECTIVA 3D
            drawContext.canvas.nativeCanvas.save()
            val camera = Camera()
            val matrix = AndroidMatrix()
            camera.save()
            camera.rotateX(-115f * openProgress)
            camera.getMatrix(matrix)
            camera.restore()

            matrix.preTranslate(-centerX, -centerY)
            matrix.postTranslate(centerX, centerY)
            drawContext.canvas.nativeCanvas.concat(matrix)

            drawDetailedLid(centerX, centerY, w, h, outlineColor)

            drawContext.canvas.nativeCanvas.restore()
        }

        CoinRewardDropdown(
            openProgress = openProgress,
            chestWidthPx = chestSizePx * 0.75f,
            chestTopY = chestTopY
        )
        if(!isOpen){
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .offset(y = (-45).dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "✨ Toca el cofre ✨",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = BricolageGrotesque,


                    )
            }
        }else{
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .offset(y = (-85).dp),
                contentAlignment = Alignment.Center
            ) {

                Button(
                    onClick = {
                        navController.navigate(
                            "caracteristicas/$especieId/$imagenEncoded/$levelId"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LightOlive),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.width(320.dp).height(80.dp)
                ) {
                    Text(
                        text = "Continuar",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                }
            }
        }




    }
}
@Composable
fun CoinRewardDropdown(
    openProgress: Float,
    chestWidthPx: Float,
    chestTopY: Float
) {
    val show = openProgress > 0.3f

    val expandProgress by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween(
            durationMillis = 700,
            easing = FastOutSlowInEasing
        ),
        label = "CoinExpand"
    )

    val alpha by animateFloatAsState(
        targetValue = if (show) 1f else 0f,
        animationSpec = tween(400),
        label = "CoinAlpha"
    )

    // ✨ pequeño pulso para la moneda
    val pulse by animateFloatAsState(
        targetValue = if (show) 1.05f else 1f,
        animationSpec = tween(800, easing = EaseInOutSine),
        label = "CoinPulse"
    )

    if (alpha > 0f) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    this.alpha = alpha
                    scaleY = expandProgress
                    transformOrigin = TransformOrigin(0.5f, 0f)
                }
                .width(with(LocalDensity.current) { chestWidthPx.toDp() * 0.9f })
                .height(72.dp)
                .offset {
                    IntOffset(
                        x = 0,
                        y = chestTopY.toInt() - 350
                    )
                }
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFE082),
                            Color(0xFFFFC107),
                            Color(0xFFFFA000)
                        )
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFFFFF3E0),
                    shape = RoundedCornerShape(18.dp)
                )
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(18.dp),
                    clip = false
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dolar),
                    contentDescription = "Moneda",
                    modifier = Modifier
                        .size(42.dp)
                        .graphicsLayer {
                            scaleX = pulse
                            scaleY = pulse
                        }
                )

                Text(
                    text = "+50",
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E342E),
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.25f),
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
            }
        }
    }
}



private fun DrawScope.drawDetailedBase(
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    outline: Color
) {
    /* =========================
       📐 BASE DEL COFRE
       ========================= */

    val outerRect = Rect(x - w / 2f, y, x + w / 2f, y + h)
    val metalThickness = w * 0.06f

    val innerRect = Rect(
        outerRect.left + metalThickness,
        outerRect.top + metalThickness,
        outerRect.right - metalThickness,
        outerRect.bottom - metalThickness
    )

    /* =========================
       🟡 ORO METÁLICO CARTOON
       ========================= */

    val goldBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color(0xFFFFF9C4),
            0.20f to Color(0xFFFFE082),
            0.45f to Color(0xFFFFC107),
            0.60f to Color(0xFFFFD54F),
            0.80f to Color(0xFFFFB300),
            1.00f to Color(0xFFB8860B)
        )
    )

    /* =========================
       🪵 MADERA
       ========================= */


    val woodBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color(0xFF6E3F25),
            0.45f to Color(0xFF7A452C),
            0.75f to Color(0xFF5C311D),
            1.00f to Color(0xFF4A2716)
        )
    )

    drawRoundRect(
        brush = goldBrush,
        topLeft = outerRect.topLeft,
        size = outerRect.size,
        cornerRadius = CornerRadius(20f)
    )

    drawRoundRect(
        brush = Brush.verticalGradient(
            listOf(Color.White.copy(0.4f), Color.Transparent)
        ),
        topLeft = outerRect.topLeft,
        size = Size(outerRect.width, outerRect.height * 0.35f),
        cornerRadius = CornerRadius(20f)
    )
    drawRoundRect(
        brush = woodBrush,
        topLeft = innerRect.topLeft,
        size = innerRect.size,
        cornerRadius = CornerRadius(14f)
    )
    drawRoundRect(
        brush = Brush.verticalGradient(
            listOf(
                Color.Black.copy(alpha = 0.25f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.35f)
            )
        ),
        topLeft = innerRect.topLeft,
        size = innerRect.size,
        cornerRadius = CornerRadius(14f)
    )

    drawWoodGrain(innerRect, intensity = 1.4f)

    drawRect(
        brush = Brush.verticalGradient(
            listOf(Color.Black.copy(0.35f), Color.Transparent)
        ),
        topLeft = innerRect.topLeft,
        size = Size(innerRect.width, 28f)
    )

    drawRoundRect(
        color = outline,
        topLeft = outerRect.topLeft,
        size = outerRect.size,
        cornerRadius = CornerRadius(20f),
        style = Stroke(4f)
    )

    /* =========================
       🔒 CERRADURA ALTA Y REDONDA
       ========================= */

    val lockWidth = metalThickness * 2.4f
    val lockHeight = metalThickness * 4.4f   // ⬆️ MÁS ALTA
    val lockTop = y - metalThickness * 0.4f

    val lockPath = Path().apply {

        // Parte superior
        moveTo(x - lockWidth, lockTop)
        quadraticBezierTo(
            x,
            lockTop - lockHeight * 0.25f, // curva superior
            x + lockWidth,
            lockTop
        )

        // Lados
        lineTo(x + lockWidth * 0.9f, lockTop + lockHeight * 0.55f)

        // Parte inferior redonda (gota)
        quadraticBezierTo(
            x,
            lockTop + lockHeight,
            x - lockWidth * 0.9f,
            lockTop + lockHeight * 0.55f
        )

        close()
    }

    // 🟡 CUERPO
    drawPath(path = lockPath, brush = goldBrush)

    // ✨ BRILLO SUAVE
    drawPath(
        path = lockPath,
        brush = Brush.verticalGradient(
            listOf(Color.White.copy(0.45f), Color.Transparent)
        )
    )

    // ✏️ CONTORNO
    drawPath(
        path = lockPath,
        color = outline,
        style = Stroke(3.5f)
    )

    /* =========================
       🗝️ RANURA DE LLAVE
       ========================= */

    val keyCenter = Offset(x, lockTop + lockHeight * 0.52f)
    val keyRadius = metalThickness * 0.5f

    drawCircle(
        color = outline,
        radius = keyRadius,
        center = keyCenter
    )

    drawRoundRect(
        color = outline,
        topLeft = Offset(
            keyCenter.x - keyRadius * 0.35f,
            keyCenter.y
        ),
        size = Size(
            keyRadius * 0.7f,
            keyRadius * 1.6f
        ),
        cornerRadius = CornerRadius(10f)
    )
}


private fun DrawScope.drawWoodGrain(
    rect: Rect,
    intensity: Float = 1.4f
) {
    val grainColor = Color(0xFF3E1F10).copy(alpha = 0.18f * intensity)

    val spacing = 16f
    val lines = (rect.height / spacing).toInt()

    for (i in 0..lines) {
        val y = rect.top + i * spacing
        val wave = sin(i * 0.9f) * 8f

        drawLine(
            color = grainColor,
            start = Offset(rect.left + 10f, y + wave),
            end = Offset(rect.right - 10f, y - wave),
            strokeWidth = 2.4f
        )
    }
}
private fun DrawScope.drawCartoonSeaweed(
    baseX: Float,
    baseY: Float,
    scale: Float = 1f,
    color: Color = Color(0xFF8BAA4A)
) {
    val path = Path().apply {

        // TALLO PRINCIPAL
        moveTo(baseX, baseY)

        cubicTo(
            baseX - 10f * scale,
            baseY - 40f * scale,
            baseX + 5f * scale,
            baseY - 90f * scale,
            baseX,
            baseY - 140f * scale
        )

        // RAMA SUPERIOR IZQUIERDA
        cubicTo(
            baseX - 25f * scale,
            baseY - 160f * scale,
            baseX - 45f * scale,
            baseY - 140f * scale,
            baseX - 35f * scale,
            baseY - 115f * scale
        )

        // REGRESO AL TALLO
        cubicTo(
            baseX - 15f * scale,
            baseY - 95f * scale,
            baseX - 10f * scale,
            baseY - 75f * scale,
            baseX,
            baseY - 65f * scale
        )

        // RAMA DERECHA
        cubicTo(
            baseX + 35f * scale,
            baseY - 85f * scale,
            baseX + 40f * scale,
            baseY - 115f * scale,
            baseX + 20f * scale,
            baseY - 135f * scale
        )

        // REGRESO
        cubicTo(
            baseX + 5f * scale,
            baseY - 120f * scale,
            baseX + 5f * scale,
            baseY - 90f * scale,
            baseX,
            baseY - 70f * scale
        )

        // RAMA MEDIA IZQUIERDA
        cubicTo(
            baseX - 30f * scale,
            baseY - 80f * scale,
            baseX - 30f * scale,
            baseY - 45f * scale,
            baseX - 10f * scale,
            baseY - 30f * scale
        )

        close()
    }

    drawPath(
        path = path,
        color = color
    )
}


private fun DrawScope.drawDetailedLid(
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    outline: Color
) {
    val lidHeight = h * 0.65f
    val metalThickness = (w * 0.08f) * 0.8f

    val outerRect = Rect(
        x - w / 2,
        y - lidHeight,
        x + w / 2,
        y
    )

    val innerRect = Rect(
        outerRect.left + metalThickness,
        outerRect.top + metalThickness,
        outerRect.right - metalThickness,
        outerRect.bottom - metalThickness
    )

    /* =========================
       🟡 ORO METÁLICO
       ========================= */
    val goldBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color(0xFFFFF8C4),
            0.25f to Color(0xFFFFE082),
            0.50f to Color(0xFFFFC107),
            0.75f to Color(0xFFFFB300),
            1.00f to Color(0xFFCC8F00)
        )
    )

    /* =========================
       🪵 MADERA RICA / DESGASTADA
       ========================= */
    val woodBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color(0xFF6E3F25),
            0.45f to Color(0xFF7A452C),
            0.75f to Color(0xFF5C311D),
            1.00f to Color(0xFF4A2716)
        )
    )

    // 🔶 MARCO METÁLICO
    drawRoundRect(
        brush = goldBrush,
        topLeft = outerRect.topLeft,
        size = outerRect.size,
        cornerRadius = CornerRadius(18f)
    )

    // ✨ BRILLO METÁLICO SUPERIOR
    drawRoundRect(
        brush = Brush.verticalGradient(
            listOf(Color.White.copy(alpha = 0.35f), Color.Transparent)
        ),
        topLeft = outerRect.topLeft,
        size = Size(outerRect.width, outerRect.height * 0.35f),
        cornerRadius = CornerRadius(18f)
    )

    // 🔷 MADERA INTERIOR
    drawRoundRect(
        brush = woodBrush,
        topLeft = innerRect.topLeft,
        size = innerRect.size,
        cornerRadius = CornerRadius(14f)
    )

    // 🌑 DESGASTE EN BORDES (CLAVE PARA LOOK REALISTA)
    drawRoundRect(
        brush = Brush.verticalGradient(
            listOf(
                Color.Black.copy(alpha = 0.25f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.35f)
            )
        ),
        topLeft = innerRect.topLeft,
        size = innerRect.size,
        cornerRadius = CornerRadius(14f)
    )

    // 🌳 VETAS DE MADERA
    drawWoodGrain(innerRect, intensity = 1.4f)

    // 🌑 SOMBRA SUPERIOR (PROFUNDIDAD)
    drawRect(
        brush = Brush.verticalGradient(
            listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent)
        ),
        topLeft = Offset(innerRect.left, innerRect.top),
        size = Size(innerRect.width, 24f)
    )

    // ✨ LUZ DORADA AMBIENTAL (rebote del oro)
    drawRoundRect(
        brush = Brush.verticalGradient(
            listOf(
                Color(0xFFFFC107).copy(alpha = 0.18f),
                Color.Transparent
            )
        ),
        topLeft = innerRect.topLeft,
        size = Size(innerRect.width, innerRect.height * 0.5f),
        cornerRadius = CornerRadius(14f)
    )

    // ✏️ CONTORNO FINAL
    drawRoundRect(
        color = outline,
        topLeft = outerRect.topLeft,
        size = outerRect.size,
        cornerRadius = CornerRadius(18f),
        style = Stroke(4f)
    )
}


private fun DrawScope.drawPearl(x: Float, y: Float, progress: Float) {
    val alpha = progress.coerceIn(0f, 1f)
    // Glow de la perla
    drawCircle(
        brush = Brush.radialGradient(listOf(Color.White.copy(0.8f * alpha), Color.Transparent)),
        radius = 50f,
        center = Offset(x, y)
    )
    // Cuerpo de la perla
    drawCircle(
        brush = Brush.linearGradient(listOf(Color.White, Color(0xFFE0E0E0))),
        radius = 18f,
        center = Offset(x, y)
    )
    // Brillo de la perla
    drawCircle(Color.White, 5f, Offset(x - 6f, y - 6f))
}