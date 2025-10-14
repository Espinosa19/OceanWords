package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.ui.theme.OceanBackground
import com.proyect.ocean_words.ui.theme.arena
import com.proyect.ocean_words.ui.theme.azulCeleste
import com.proyect.ocean_words.ui.theme.whiteBoxColor

val StarColor = Color(0xFFFFCC00)
@Composable
fun caracteristicasEspecieView(
    navController: NavController,
    score: Int = 1250,
    time: String = "0:45",
    animal: String ="pez lampara",
    dificultad:String="dificil",
    animalQuestion: String = "¿QUÉ ANIMAL ES ESTE?",
) {
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
            ) {
                HeaderSection(score, navController)

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
            .height(520.dp)
            ,
        horizontalAlignment = Alignment.CenterHorizontally // ¡Esta línea es clave!
    ) {    Card (
            modifier = Modifier
                .fillMaxWidth(0.95f) // Usa fillMaxWidth y un factor para controlar el ancho
                .padding(24.dp,vertical = 16.dp)
                .border( // AHORA pasamos la forma (shape)
                    width = 13.dp,
                    color = arena,
                    shape = RoundedCornerShape(24.dp) // <-- ¡Esta es la clave!
                ),
            shape = RoundedCornerShape(24.dp), // Esquinas muy redondeadas
            colors = CardDefaults.cardColors(
                containerColor = whiteBoxColor // Fondo amarillo pálido
            )
        ,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 12.dp)

                    .background(azulCeleste)
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡CORRECTO",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Image(
                painter = painterResource(id = whaleImageRes),
                contentDescription = "Ballena",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 10.dp)
            )

            Text(
                text = "BALLENA",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
            )

            Text(
                text = "DATOS CURIOSOS:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
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
