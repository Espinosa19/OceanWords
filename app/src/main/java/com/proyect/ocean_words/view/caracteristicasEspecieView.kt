package com.proyect.ocean_words.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.view.theme.BricolageGrotesque
import com.proyect.ocean_words.view.theme.Delius
import com.proyect.ocean_words.view.theme.LightOlive
import com.proyect.ocean_words.view.theme.MomoTrustDisplay
import com.proyect.ocean_words.view.theme.OceanBackground
import com.proyect.ocean_words.view.theme.arena
import com.proyect.ocean_words.view.theme.azulCeleste
import com.proyect.ocean_words.view.theme.whiteBoxColor

val StarColor = Color(0xFFFFCC00)
@Composable
fun caracteristicasEspecieView(
    navController: NavController,
    especie_id: String,
animal: String = "Pez lámpara",
dificultad: String = "Difícil",
animalQuestion: String = "¿Qué animal es este?"
) {
    Log.i("Unidos","$especie_id")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanBackground)
    ) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo_juego),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection(animal, dificultad, navController ) // Encabezado superior (corazones, monedas, etc.)
            Spacer(modifier = Modifier.height(20.dp))
            WhaleInfoCard(whaleImageRes = R.drawable.ballena)

        }
        // Barra inferior fija
    }
}

@Composable
fun WhaleInfoCard(whaleImageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.98f)
            .wrapContentHeight() // 👈 importante
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .border(width = 13.dp, color = arena, shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = whiteBoxColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título superior
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(azulCeleste)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡CORRECTO!",
                    fontFamily = MomoTrustDisplay,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Imagen del animal
            Image(
                painter = painterResource(id = whaleImageRes),
                contentDescription = "Ballena",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 10.dp)
            )

            // Título
            Text(
                text = "BALLENA",
                fontFamily = MomoTrustDisplay,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color.Black
            )

            // Subtítulo
            Text(
                text = "DATOS CURIOSOS:",
                fontFamily = Delius,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 16.dp),
                color = Color.Black
            )

            // Lista de datos curiosos
            Column(
                modifier = Modifier
                    .padding(horizontal = 34.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                CuriousFact("Son los animales más grandes del planeta."
                )
                CuriousFact("Se comunican a través de cantos.")

            }

            // Puntos ganados

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 10.dp)
                    .height(100.dp),
                horizontalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BotonInferiorIcon(
                    icon = Icons.Default.Home,
                    texto = "Inicio",
                    onClick = { /* Acción */ }
                )

                BotonInferiorIcon(
                    icon = Icons.Default.ArrowForward,
                    texto = "Siguiente",
                    onClick = { /* Acción */ }
                )
            }
        }
    }
}

@Composable
private fun CuriousFact(text: String) {
    Text(
        text = "• $text",
        fontFamily = Delius,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(vertical = 1.dp),
        color = Color.Black
    )
}


@Composable
fun BotonInferior(
    iconRes: Int,
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(55.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightOlive,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = texto,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = texto,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BotonInferiorIcon(
    icon: ImageVector,
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(130.dp)
            .height(55.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightOlive,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = texto,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = texto,
                fontFamily = BricolageGrotesque,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
