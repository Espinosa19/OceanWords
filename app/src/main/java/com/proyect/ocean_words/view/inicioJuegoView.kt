package com.proyect.ocean_words.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.ocean_words.R // Asegúrate de que esta ruta sea correcta

@Composable
fun inicioJuegoView() {
    // Colores simulados basados en la imagen
    val blueBackground = Color(0xFF6DE1E7) // Un azul/verde claro para el fondo principal
    val whiteBoxColor = Color.White.copy(alpha = 0.9f) // Un blanco semitransparente para el contenedor principal
    val textColor = Color(0xFF284C6A) // Azul oscuro para el texto
    val progressBarColor = Color(0xFF4AC2F6) // Color de la barra de carga

    // 1. Contenedor principal para el fondo (como si fuera el fondo de la pantalla)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blueBackground) // Fondo principal
    ) {
        // 1.1. Imagen de fondo del océano (si tienes un drawable)
        Image(
            painter = painterResource(id = R.mipmap.ic_logo_ocean), // Reemplaza con tu drawable de fondo
            contentDescription = "Fondo de océano",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Columna principal que contiene la tarjeta blanca y el copyright.
        // Se centra en la pantalla.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp, vertical = 20.dp), // Padding exterior
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 3. El Box/Tarjeta Blanca Central
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp)) // Bordes redondeados
                    .background(whiteBoxColor) // Fondo blanco semitransparente
                    .border( // Borde suave alrededor de la caja
                        width = 2.dp,
                        color = Color.White.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(30.dp), // Padding interno para los elementos
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3.1. Logo
                Image(
                    painter = painterResource(id = R.mipmap.ic_logo_ocean),
                    contentDescription = "Logo Ocean Words",
                    modifier = Modifier.size(230.dp), // Tamaño del logo
                    contentScale = ContentScale.Fit
                )

                // 3.2. Espacio
                Spacer(modifier = Modifier.height(20.dp))

                // 3.3. Texto de Bienvenida
                Text(
                    text = "En **Ocean Words**, aprenderás datos asombrosos sobre las criaturas marinas mientras te diviertes adivinando sus nombres. El conocimiento es un tesoro escondido en las profundidades. ¡Estamos a punto de desenterrarlo!",
                    textAlign = TextAlign.Center,
                    color = textColor,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )

                // 3.4. Espacio
                Spacer(modifier = Modifier.height(40.dp))

                // 3.5. Barra de Carga / Botón (simulado como barra de carga)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFF86D5FC).copy(alpha = 0.8f)) // Fondo base para la barra (como el botón)
                        .border(
                            width = 2.dp,
                            color = Color.White.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Simulación de la barra de progreso (el relleno azul)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // 80% del ancho para el progreso
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(progressBarColor)
                            .align(Alignment.Start) // Alineado a la izquierda
                    )

                    // Texto "Cargando..."
                    Text(
                        text = "Cargando...",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 15.dp) // Alineado bajo la barra
                    )
                }
            }

            // 4. Espacio para empujar el Copyright hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // 5. Copyright (Infinity Deve)
            Text(
                text = "© INFINITY DEVE",
                color = Color.White.copy(alpha = 0.8f), // Blanco semitransparente
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 10.dp) // Un pequeño padding abajo
            )
        }
    }
}