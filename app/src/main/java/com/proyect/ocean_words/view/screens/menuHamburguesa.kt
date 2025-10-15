package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.* // Importa todos los componentes de Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyect.ocean_words.R // Asegúrate de que este ID de recurso exista
import com.proyect.ocean_words.ui.theme.BlueGrayDark
import com.proyect.ocean_words.ui.theme.LightOlive
val MenuBackgroundColor = Color(0xFFF0F0F0) // Un gris claro para el fondo del menú

@Composable
fun NavegacionDrawerMenu(
    navController: NavController,
    onCloseMenu: () -> Unit
) {
    // El Box ahora actúa como un fondo transparente que cierra el menú al hacer clic en cualquier parte.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0f)) // Oscurece el fondo
            .clickable(onClick = onCloseMenu),
    ) {


        Column(
            modifier = Modifier
                .align(Alignment.TopEnd) // Alinea la columna a la esquina superior derecha del Box padre.
                .padding(top = 80.dp, end = 16.dp) // Ajusta 'top' para que quede debajo del icono de tesoro.
                .clip(RoundedCornerShape(15.dp)) // Bordes redondeados para un aspecto moderno.
                .background(Color(0xFFB3E5FC).copy(alpha = 0.65f)) // Asumo el color IndicatorBackgroundColor

                .border(
                    width = 1.dp, // Borde más sutil
                    color = Color.White.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(20.dp)
                )
                .width(IntrinsicSize.Max) // Ajusta el ancho al ítem más ancho.
                .clickable(enabled = false, onClick = {}) // Evita que un clic dentro del menú lo cierre.
        ) {


            MenuButtonOnlyImage(
                resourceId = R.drawable.hogar,
                contentDescription = "Home",
                onClick = {
                    onCloseMenu()
                    navController.navigate("pista")
                }
            )

            // Separador (opcional para un mejor aspecto visual)
            Divider(modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray.copy(alpha = 0.3f))

            // Segundo botón (ejemplo usando un recurso alternativo)
            // Asumo que R.drawable.settings_icon existe para este ejemplo
            MenuButtonOnlyImage(
                resourceId = R.drawable.pista, // Usa tu propio recurso de ícono
                contentDescription = "Pistas",
                onClick = {
                    onCloseMenu()
                    navController.navigate("settings")
                }
            )
            Divider(modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray.copy(alpha = 0.3f))

            // Segundo botón (ejemplo usando un recurso alternativo)
            // Asumo que R.drawable.settings_icon existe para este ejemplo
            MenuButtonOnlyImage(
                resourceId = R.drawable.configuraciones, // Usa tu propio recurso de ícono
                contentDescription = "Configuraciones",
                onClick = {
                    onCloseMenu()
                    navController.navigate("settings")
                }
            )

            // Si necesitas un tercer botón, lo agregas aquí...
        }
    }
}

// -------------------------------------------------------------------------------------------------

/**
 * Componente para un botón en el menú que solo muestra una Imagen.
 */
@Composable
fun MenuButtonOnlyImage(resourceId: Int, contentDescription: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(55.dp) // Tamaño para que sea un botón cuadrado fácil de presionar.
            .padding(12.dp) // Espacio interior
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize()
        )
    }
}
