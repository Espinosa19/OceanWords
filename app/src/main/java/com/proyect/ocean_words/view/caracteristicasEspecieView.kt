package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.ui.theme.OceanBackground
import com.proyect.ocean_words.ui.theme.whiteBoxColor

val CardBackgroundColor = Color(0xFFFFFFFF)
val RibbonColor = Color(0xFF66BB6A) // Verde para la cinta
val StarColor = Color(0xFFFFCC00)
@Composable
fun caracteristicasEspecieView(
    navController: NavController,
    score: Int = 1250,
    time: String = "0:45",
    animal: String ="pez lampara",
    dificultad:String="dificil",
    animalQuestion: String = "¿QUÉ ANIMAL ES ESTE?",
    // Esta es solo una representación simplificada de las letras
) {
    // Para el fondo y las decoraciones se usaría un Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanBackground)
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_juego), // tu imagen en res/drawable
                contentDescription = null,
                contentScale = ContentScale.Crop, // Ajusta para cubrir toda la pantalla
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                // 1. Encabezado (Logo, Score, Time)
                HeaderSection(score, time,navController)

                Spacer(modifier = Modifier.height(20.dp))
                WhaleInfoCard(whaleImageRes = R.drawable.ballena)
            }
        }
    }
}

@Composable
fun WhaleInfoCard(
    whaleImageRes: Int // Resource ID for the whale image (e.g., R.drawable.ballena)
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .height(460.dp),
        horizontalAlignment = Alignment.CenterHorizontally // ¡Esta línea es clave!
    ) {    Card (
            modifier = Modifier
                .fillMaxWidth(0.9f) // Usa fillMaxWidth y un factor para controlar el ancho
                .padding(14.dp,vertical = 16.dp), // Solo padding vertical
            shape = RoundedCornerShape(24.dp), // Esquinas muy redondeadas
            colors = CardDefaults.cardColors(
                containerColor = whiteBoxColor // Fondo amarillo pálido
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Cinta de "¡CORRECTO!" (Simulada con un Box y Text)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f) // Ocupa la mayor parte del ancho
                    .offset(y = (-10).dp) // Superpuesto en la parte superior
                    .clip(
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 4.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .background(RibbonColor)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡CORRECTO",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            // Pequeña corrección para hacer la cinta más parecida

            // 2. Imagen de la ballena
            Image(
                painter = painterResource(id = whaleImageRes),
                contentDescription = "Ballena",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 10.dp)
            )

            // 3. Nombre del animal
            Text(
                text = "BALLENA",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
            )

            // 4. Título "DATOS CURIOSOS:"
            Text(
                text = "DATOS CURIOSOS:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            // 5. Lista de Datos Curiosos (usando Column para la lista y Text para los puntos)
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Función auxiliar para un elemento de lista
                @Composable
                fun CuriousFact(text: String) {
                    Text(
                        text = "• $text",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }

                CuriousFact("Son los animales más grandes del planeta.")
                CuriousFact("Se comunican a través de cantos.")

            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. Sección de puntos ganados con la estrella (usando Row)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Icono de estrella (Necesitarás un ícono/imagen de estrella)
                // Se utiliza un Box simple con color para simular la estrella.
                // En una app real, usarías Icon(Icons.Filled.Star, ...) o una imagen/SVG.
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(StarColor)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "★",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Texto de puntos ganados
                Text(
                    text = "¡HAS GANADO 100 PUNTOS!",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Black // O el color de fuente deseado
                )
            }
        }
    }
    }
}
