package com.proyect.ocean_words.view.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox // Se mantiene para el tipo
import androidx.compose.material.icons.filled.Star // Se mantiene para el tipo
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.ui.theme.Blue // Asumo que son colores definidos en tu tema
// NOTA: Tu código original usaba IndicatorBackgroundColor, Orange, OrangeDeep, y Purple40
// pero no estaban definidos en el código que enviaste. Asumo que se definen en el archivo theme.

@Composable
fun HeaderSection(score: Int, time: String,navController: NavController) {
    var statusMenu by remember { mutableStateOf(false) }

    // CAMBIO CLAVE 1: Usamos Box con wrapContentHeight. El Box contendrá el logo, el menú y los indicadores.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top) // Ajusta su altura al contenido, alineado arriba
    ) {

        // --- 1. Logo (Capa Principal) ---
        // Usamos un Column para centrar el logo y darle un poco de altura base, eliminando alturas fijas
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ocean_words),
                contentDescription = "Título del juego",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight(),
                contentScale = ContentScale.FillWidth
            )
            // Espacio fijo entre el logo y los indicadores
            Spacer(modifier = Modifier.height(10.dp))

            // 2. Indicadores (Debajo del logo)
            HeaderIndicatorRow(score, time)
        }

        // --- 3. Botón de Menú (Capa Superior Derecha) ---
        IconButton(
            onClick = { statusMenu = !statusMenu },

            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 25.dp,end = 8.dp) // Margen a la derecha
        ) {
            // El icono del tesoro (o menú) se coloca en la parte superior derecha, flotando.
            Image(
                painter = painterResource(id = R.drawable.tesoro),
                contentDescription = "menu",
                modifier = Modifier
                    .size(54.dp), // Tamaño fijo pero razonable para el icono/botón
                contentScale = ContentScale.Fit
            )
        }
        if (statusMenu) {
            // CORRECCIÓN CLAVE: Pasar el parámetro onMenuItemClick
            NavegacionDrawerMenu(navController,onCloseMenu = { statusMenu = false }    )
        }
    }
}

// NO NECESITA CAMBIOS MAYORES
@Composable
fun HeaderIndicatorRow(score: Int, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // Padding ajustado para separarlo de los bordes laterales y centrarlo
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly, // Uso SpaceEvenly para mejor distribución
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameIndicator(
            icon = Icons.Default.Star,
            tipo="Icono",
            label = "SCORE",
            value = score.toString(),
            iconColor = Color(0xFFFCCB06),
            iconBackgroundColor = Color.White
        )

        GameIndicator(
            icon = Icons.Default.AccountBox, // Esto es solo un placeholder para el tipo
            tipo= "Imagen",
            label = "TIME",
            value = time,
            iconColor = Color.White,
            iconBackgroundColor = Color(0xFF0077B6)
        )
    }
}

// NO NECESITA CAMBIOS MAYORES
@Composable
fun GameIndicator(
    icon: ImageVector,
    tipo: String,
    label: String,
    value: String,
    iconColor: Color,
    iconBackgroundColor: Color
) {
    // CAMBIO CLAVE 2: Usamos wrapContentWidth(unconstrained) para que el indicador crezca solo lo necesario.
    Box(
        modifier = Modifier
            .wrapContentWidth(Alignment.Start)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFB3E5FC).copy(alpha = 0.65f)) // Asumo el color IndicatorBackgroundColor
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(30.dp)
            )
            .height(40.dp) // Altura fija de la cápsula (se mantiene porque es pequeña)
            .padding(start = 2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            // El padding del Row interno asegura que el texto no se superponga con el círculo
            modifier = Modifier.padding(start = 45.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$label: $value",
                color = Color.White,
                fontSize = 16.sp, // Tamaño de texto adaptable
                fontWeight = FontWeight.Bold
            )
        }

        // Icono dentro de un círculo
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Blue)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            when (tipo) {
                "Imagen" -> Image(
                    painter = painterResource(id = R.drawable.reloj_de_arena),
                    contentDescription = "Reloj de arena",
                    modifier = Modifier.size(24.dp)
                        .padding(start = 5.dp)

                )
                "Icono" -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}