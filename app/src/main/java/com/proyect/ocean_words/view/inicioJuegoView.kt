package com.proyect.ocean_words.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.ocean_words.R
import kotlinx.coroutines.delay

// Define una función de navegación simulada.
// En una app real, aquí llamarías a un navController.navigate("otra_ruta")
private fun navigateToNextScreen(onNavigation: () -> Unit) {
    onNavigation()
}

@Composable
fun InicioJuegoView(onNavigationToGame: () -> Unit) { // 1. Recibe la función de navegación

    // --- ESTADO DE CARGA ---
    // 2. Estado para el porcentaje de la barra de progreso (de 0.0f a 1.0f)
    var progressTarget by remember { mutableFloatStateOf(0.0f) }
    // 3. Estado para saber si la carga ha finalizado (para mostrar el texto "¡Listo!" o similar)
    var isLoadingComplete by remember { mutableStateOf(false) }

    // 4. Efecto para iniciar la carga y la navegación
    LaunchedEffect(Unit) {
        // Simular la carga: Subir el progreso al 100%
        progressTarget = 1.0f

        // Esperar un tiempo extra después de que la animación termine (aprox 2 segundos)
        delay(2000)

        // Marcar la carga como completa y navegar
        isLoadingComplete = true
        navigateToNextScreen(onNavigationToGame)
    }

    // 5. Animación para la barra de progreso
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 1500), // La barra tarda 1.5s en llenarse
        label = "ProgressAnimation"
    )
    // ----------------------

    // Colores (se mantienen para la vista)
    val blueBackground = Color(0xFF6DE1E7)
    val whiteBoxColor = Color.White.copy(alpha = 0.9f)
    val textColor = Color(0xFF284C6A)
    val progressBarColor = Color(0xFF4AC2F6)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blueBackground)
    ) {
        // ... (Tu fondo de imagen)
        Image(
            painter = painterResource(id = R.drawable.fondo_juego),
            contentDescription = "Fondo de océano",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp, vertical = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ... (Tu Tarjeta Blanca Central)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(whiteBoxColor)
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... (Logo, Espacio y Texto de Bienvenida)
                Image(
                    painter = painterResource(id = R.mipmap.ic_logo_ocean),
                    contentDescription = "Logo Ocean Words",
                    modifier = Modifier.size(230.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "En **Ocean Words**, aprenderás datos asombrosos sobre las criaturas marinas mientras te diviertes adivinando sus nombres. El conocimiento es un tesoro escondido en las profundidades. ¡Estamos a punto de desenterrarlo!",
                    textAlign = TextAlign.Center,
                    color = textColor,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(40.dp))

                // --- NUEVA BARRA DE CARGA DINÁMICA ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFF86D5FC).copy(alpha = 0.8f))
                        .border(
                            width = 2.dp,
                            color = Color.White.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // La barra de progreso usa el estado animado
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress) // Usa el valor animado (0.0f a 1.0f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(progressBarColor)
                            .align(Alignment.Start)
                    )

                    // Texto que cambia al finalizar la carga
                    Text(
                        text = if (isLoadingComplete) "¡Listo para Jugar!" else "Cargando...",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
                // ----------------------------------------
            }

            // ... (Copyright)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "© INFINITY DEVE",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}