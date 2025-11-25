package com.proyect.ocean_words.view

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.NivelEstado

import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun LoadingScreenOceanWords(
    currentLevelId: Int?,
    niveles: List<NivelEstado>,
    navController: NavController
) {
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val progressAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    LaunchedEffect(currentLevelId) {
        delay(5000)
        onStartTransitionAndNavigate(currentLevelId, niveles, navController)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo_ocean_words),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        BurbujasAnimadas(R.drawable.burbujas, duration = 9000, alpha = 0.5f, startX = 50, startSize = 100)
        BurbujasAnimadas(R.drawable.burbujas, duration = 12000, alpha = 0.35f, startX = 250, startSize = 130)
        BurbujasAnimadas(R.drawable.burbujas, duration = 15000, alpha = 0.4f, startX = 500, startSize = 160)


        PezAnimado(drawableId = R.drawable.grupopeces, size = 140, duration = 16000, reverse = false, startY = 400)
        PezAnimado(drawableId = R.drawable.pez_izquierdo, size = 70, duration = 10000, reverse = true, startY = 300)
        PezAnimado(drawableId = R.drawable.pescado, size = 100, duration = 22000, reverse = false, startY = 550)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_ocean),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "CARGANDO SIGUIENTE NIVEL...", color = Color.White)

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .width(260.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .background(Color(0x8022CFE6))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressAnim)
                        .clip(RoundedCornerShape(40.dp))
                        .background(Color(0xFF00E5FF))
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "¡Aventúrate en nuevas profundidades!", color = Color.White)
        }

        Text(
            text = "© INFINITY DEVS",
            color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Composable
fun PezAnimado(drawableId: Int, size: Int, duration: Int, reverse: Boolean = false, startY: Int) {
    val infinite = rememberInfiniteTransition(label = "")

    val startX = if (reverse) 1200f else -400f
    val endX = if (reverse) -400f else 1200f

    val offsetX by infinite.animateFloat(
        initialValue = startX,
        targetValue = endX,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val offsetY by infinite.animateFloat(
        initialValue = startY.toFloat(),
        targetValue = startY.toFloat() + 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration / 4, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    Image(
        painter = painterResource(id = drawableId),
        contentDescription = null,
        modifier = Modifier
            .size(size.dp)
            .offset(x = offsetX.dp, y = offsetY.dp)
    )
}

@Composable
fun BurbujasAnimadas(drawableId: Int, duration: Int, alpha: Float, startX: Int, startSize: Int) {
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
        targetValue = startSize.toFloat() * 1.3f,
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
            .offset(x = startX.dp, y = offsetY.dp)
            .alpha(alpha)
    )
}

fun onStartTransitionAndNavigate(
    levelId: Int?,
    niveles: List<NivelEstado>,
    navController: NavController
) {
    val nivel = niveles.find { it.numero_nivel == levelId }
    val especie = nivel?.especies_id?.firstOrNull()

    if (especie != null) {
        val especieId = especie.id
        val nombre = especie.nombre.replace(" ", "%20")
        val dificultad = especie.dificultad

        var imagen = URLEncoder.encode(especie.imagen,StandardCharsets.UTF_8.toString())
        navController.navigate("nivel/$levelId/$especieId/$nombre/$dificultad/$imagen")
    } else {
        Log.e("CaminoNivelesRoute", "No se encontró especie para el nivel $levelId")
    }
}
