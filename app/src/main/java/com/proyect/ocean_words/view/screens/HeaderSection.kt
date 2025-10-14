package com.proyect.ocean_words.view.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox // Se mantiene para el tipo
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star // Se mantiene para el tipo
import androidx.compose.material3.Button
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
import com.proyect.ocean_words.ui.theme.LightOlive
import com.proyect.ocean_words.view.BotonDeInterfaz
import com.proyect.ocean_words.view.TemporizadorRegresivo

// NOTA: Tu código original usaba IndicatorBackgroundColor, Orange, OrangeDeep, y Purple40
// pero no estaban definidos en el código que enviaste. Asumo que se definen en el archivo theme.

@Composable
fun HeaderSection(score: Int, navController: NavController) {
    var statusMenu by remember { mutableStateOf(false) }
    val tiempo = TemporizadorRegresivo()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top)
    ) {


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
            Spacer(modifier = Modifier.height(10.dp))

            HeaderIndicatorRow(tiempo,onBackClick = {})
        }

        IconButton(
            onClick = { statusMenu = !statusMenu },

            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 25.dp,end = 8.dp) // Margen a la derecha
        ) {
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
@Composable
fun HeaderIndicatorRow(
    time: String,
    onBackClick: () -> Unit // Agregamos un callback para manejar el clic
) {
    // Usamos SpaceBetween para dar más énfasis a los extremos
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center, // centra todo el contenido
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomBackButton(onClick = onBackClick)
            CentralIndicatorBox()
            GameIndicator(value = "500")
        }
    }

}

// -------------------------------------------------------------------------------------------------

/**
 * Componente Botón de Retroceso (Separado para limpieza)
 */
@Composable
fun CustomBackButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(40.dp), // Ligeramente más grande y visible
        shape = RoundedCornerShape(percent = 50), // Forma perfectamente circular
        colors = ButtonDefaults.buttonColors(containerColor = Blue),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Volver atrás",
            tint = Color.White,
            modifier = Modifier.size(24.dp) // Ícono de tamaño estándar
        )
    }
}

/**
 * Componente Indicador Central (Simplificado y estéticamente mejorado)
 */
@Composable
fun CentralIndicatorBox() {
    // Usamos Box para el fondo y Row para el contenido, como antes
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp)) // Borde redondeado suave
            .background(Color(0xFFB3E5FC).copy(alpha = 0.65f)) // Asumo el color IndicatorBackgroundColor

            .border(
                width = 1.dp, // Borde más sutil
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp)
            )
            .height(40.dp)
            .padding(horizontal = 8.dp), // Relleno horizontal para que los iconos no estén pegados
        contentAlignment = Alignment.CenterStart,
    ) {
        // Dentro de este Box, distribuimos los elementos.
        Row(
            modifier = Modifier.width(100.dp),

            // El Row no necesita un ancho específico, se adaptará al contenido (wrapContent)
            horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) {
                Image(
                     painter = painterResource(id = R.drawable.vidas),
                     contentDescription = null,
                     modifier = Modifier.size(20.dp)
                )


            }
        }
    }
}


// NO NECESITA CAMBIOS MAYORES
@Composable
fun GameIndicator(
    value: String,

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
            modifier = Modifier.padding(start = 45.dp, end = 12.dp)
                    .width(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$value",

                color = Color.White,
                fontSize = 16.sp, // Tamaño de texto adaptable
                fontWeight = FontWeight.Bold
            )
            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = null, tint = Blue, modifier = Modifier.size(32.dp))

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
            Image(
                painter = painterResource(id = R.drawable.dolar),
                contentDescription = "Reloj de arena",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}