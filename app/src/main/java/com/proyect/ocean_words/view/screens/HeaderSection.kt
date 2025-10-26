package com.proyect.ocean_words.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.AdivinaEspecieViewModelFactory
import com.proyect.ocean_words.view.theme.Blue // Asumo que son colores definidos en tu tema
import com.proyect.ocean_words.viewmodels.AdivinaEspecieViewModel

// NOTA: Tu código original usaba IndicatorBackgroundColor, Orange, OrangeDeep, y Purple40
// pero no estaban definidos en el código que enviaste. Asumo que se definen en el archivo theme.

@Composable
fun HeaderSection(animal : String,dificultad : String,navController: NavController) {
    val viewModel: AdivinaEspecieViewModel = viewModel(
        factory = AdivinaEspecieViewModelFactory(animal, dificultad)
    )
    val vidas by viewModel.vidas.collectAsState()

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

            HeaderIndicatorRow(vidas,
                onBackClick = {
                    navController.popBackStack()
                },
                redireccionarClick={
                    navController.navigate("game_shop") // Usa una ruta clara, por ejemplo, "game_shop"
            })
        }


    }
}@Composable
fun HeaderIndicatorRow(
    vidas: List<Boolean>,
    onBackClick: () -> Unit,
    redireccionarClick: () -> Unit
) {
    val InformaTemporizador = vidas.indexOfLast { !it }
    val rechargeTime = "04:59"
    // Altura aproximada de la burbuja (ajustar si es necesario)
    val bubbleHeight = 35.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center, // Centra el grupo de 3 elementos
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Row que agrupa Back, Vidas, y Monedas
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Elemento 1: Botón de Retroceso
            CustomBackButton(onClick = onBackClick)

            // Elemento 2: Contenedor de Vidas con Burbuja de Recarga (Clave)
            Box(
                // Alineamos el contenido de este Box al Centro
                contentAlignment = Alignment.Center
            ) {
                // Indicador de Vidas (CentralIndicatorBox) - El contenido principal
                CentralIndicatorBox(vidas = vidas)

                // Burbuja de Tiempo: Posicionada CONDICIONALMENTE
                if (InformaTemporizador !=-1 ) {
                    LifeRechargeBubble(
                        timeRemaining = rechargeTime,
                        modifier = Modifier
                            // Alinea la burbuja en la esquina superior izquierda del Box
                            .align(Alignment.TopStart)
                            // Aplica un offset para moverla ligeramente fuera del borde

                            .offset(x = (-2).dp, y = (-bubbleHeight / 2) + 60.dp) // Ajustar valores para centrado visual
                    )
                }
            }

            // Elemento 3: Indicador de Monedas
            GameIndicator(value = "500",redireccionarClick)
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
 * Componente Indicador Central (Corregido para mostrar corazones llenos/vacíos)
 */
@Composable
fun CentralIndicatorBox(vidas: List<Boolean>) {
    // ... [Variables de ancho, alto y colores, etc. - Mantenemos esto igual] ...
    val indicatorWidth = 100.dp
    val indicatorHeight = 40.dp

    Box(
        modifier = Modifier
            .width(indicatorWidth)
            .height(indicatorHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFB3E5FC).copy(alpha = 0.65f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ITERACIÓN CORREGIDA: Iteramos sobre cada 'vida'
            vidas.forEach { isLifeActive ->
                val imageResource = if (isLifeActive) {
                    R.drawable.vidas // Asumo que 'vidas' es el corazón lleno
                } else {
                    R.drawable.nome_gusta // <--- ¡DEBES USAR UN RECURSO PARA EL CORAZÓN VACÍO AQUÍ!
                }

                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = if (isLifeActive) "Corazón lleno" else "Corazón perdido",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}@Composable
fun LifeRechargeBubble(timeRemaining: String, modifier: Modifier = Modifier) {
    // Definición de colores (ejemplo)
    val BubbleBackgroundColor = Color(0xFF4C86E3)
    val TextColor = Color.White

    // El componente que quieres colocar:
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)) // Forma de burbuja redondeada
            .background(BubbleBackgroundColor)

            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        // Icono y Texto de la burbuja (por ejemplo)
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icono de reloj o similar
            Text(
                text = "Recarga en $timeRemaining",
                color = TextColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
// NO NECESITA CAMBIOS MAYORES
@Composable
fun GameIndicator(
    value: String,
    redireccionarClick: () -> Unit
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
            Button(
                onClick =redireccionarClick,
                modifier = Modifier.size(40.dp), // Tamaño del botón
                shape = RoundedCornerShape(50), // Circular
                contentPadding = PaddingValues(0.dp), // Sin padding interno
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent, // Fondo transparente
                    contentColor = Color.Unspecified // Mantiene el color de la imagen
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp) // Sin sombra
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = null,
                    tint = Blue,
                    modifier = Modifier.size(32.dp)
                )
            }

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
                    contentDescription = "Moneda",
                    modifier = Modifier.size(24.dp)
                )

        }
    }
}