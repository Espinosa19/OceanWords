package com.proyect.ocean_words.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import com.proyect.ocean_words.view.theme.whiteBoxColor
import kotlinx.coroutines.delay

// Define una función de navegación simulada.
private fun navigateToNextScreen(onNavigation: () -> Unit) {
    onNavigation()
}

@Composable
fun InicioJuegoView(onNavigationToGame: () -> Unit) {

    // --- ESTADO DE CARGA y Animación (se mantienen) ---
    var progressTarget by remember { mutableFloatStateOf(0.0f) }
    var isLoadingComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        progressTarget = 1.0f
        delay(2000)
        isLoadingComplete = true
        navigateToNextScreen(onNavigationToGame)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 1500),
        label = "ProgressAnimation"
    )
    // ----------------------

    // Colores (se mantienen)
    val blueBackground = Color(0xFF6DE1E7)
    val textColor = Color(0xFF284C6A)
    val progressBarColor = Color(0xFF4AC2F6)
    val progressBackgroundColor = Color(0xFF86D5FC).copy(alpha = 0.8f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blueBackground)
    ) {
        // Fondo de imagen
        Image(
            painter = painterResource(id = R.drawable.fondo_juego),
            contentDescription = "Fondo de océano",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                // 1. **CAMBIO CLAVE A**: Reducir el padding vertical para que el contenido ocupe más alto.
                .padding(horizontal = 24.dp, vertical = 62.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            // 2. **CAMBIO CLAVE B**: Eliminar o NO usar Arrangement.Center, ya que queremos que el contenido superior se quede arriba.
            // Usamos un Spacer(weight) al final del contenido para empujar el Copyright.
        ) {
            // ... (Tu Tarjeta Blanca Central)
            Column(
                modifier = Modifier

                    .weight(1f) // Ocupa todo el espacio vertical disponible (menos el copyright)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(whiteBoxColor)
                    .border(
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    // Puedes reducir el padding interno si quieres que el contenido se acerque más a los bordes
                    .padding(horizontal = 30.dp, vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                // Alineamos el contenido de la tarjeta en la parte superior.
                verticalArrangement = Arrangement.Center
            ) {
                // ... (Logo)
                Image(
                    painter = painterResource(id = R.mipmap.ic_logo_ocean),
                    contentDescription = "Logo Ocean Words",
                    modifier = Modifier.size(200.dp),
                    contentScale = ContentScale.Fit
                )

                // 3. **CAMBIO CLAVE D**: Usamos un Spacer con peso para empujar el texto y la barra de carga hacia el fondo de la tarjeta.
                // Esto crea un espacio grande entre el logo y el texto.
                Spacer(modifier = Modifier.height(80.dp))

                // Texto de Bienvenida
                Text(
                    text = "En Ocean Words, aprenderás datos asombrosos sobre las criaturas marinas mientras te diviertes adivinando sus nombres. El conocimiento es un tesoro escondido en las profundidades. ¡Estamos a punto de desenterrarlo!",
                    textAlign = TextAlign.Center,
                    color = textColor,
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.height(32.dp)) // Espacio estándar entre el texto y la barra de carga

                // --- BARRA DE CARGA DINÁMICA (se mantiene la lógica corregida) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(progressBackgroundColor)
                        .border(
                            width = 2.dp,
                            color = Color.White.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(30.dp))
                            .background(progressBarColor)
                            .align(Alignment.CenterStart)
                    )

                    Text(
                        text = if (isLoadingComplete) "¡Listo para Jugar!" else "Cargando...",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
                // ------------------------------------------------

                // Este spacer empuja el contenido hacia arriba, pero como usamos weight(0.5f) arriba
                // y Arrangement.Top, este ya no es tan necesario. Lo quitamos para que la barra quede cerca del fondo.
                // Spacer(modifier = Modifier.height(40.dp))
            }

            // Copyright
            Spacer(modifier = Modifier.height(16.dp)) // Pequeño espacio entre la tarjeta y el copyright
            Text(
                text = "© INFINITY DEVS",
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}