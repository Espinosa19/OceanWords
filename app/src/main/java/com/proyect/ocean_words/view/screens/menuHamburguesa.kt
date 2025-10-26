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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.proyect.ocean_words.R // Asegúrate de que este ID de recurso exista
import com.proyect.ocean_words.model.sampleShopItems


@Composable
fun NavegacionDrawerMenu(
    navController: NavController,
    onCloseMenu: () -> Unit
) {
    var statusPistas by remember { mutableStateOf(false) }

    var showConfigDialog by remember { mutableStateOf(false) }

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
                    navController.navigate("camino_niveles")
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
                    onCloseMenu() // Cierra el Drawer antes de navegar
                    // 🎯 NAVEGA a la ruta de la tienda.
                    navController.navigate("game_shop") // Usa una ruta clara, por ejemplo, "game_shop"

                }
            )

            Divider(modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray.copy(alpha = 0.3f))

            // Segundo botón (ejemplo usando un recurso alternativo)
            // Asumo que R.drawable.settings_icon existe para este ejemplo
            MenuButtonOnlyImage(
                resourceId = R.drawable.configuraciones, // Usa tu propio recurso de ícono
                contentDescription = "Configuraciones",
                onClick = {
                    showConfigDialog = true
                }
            )

        }
        if (showConfigDialog) {
            Dialog(
                onDismissRequest = { showConfigDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .offset(y = (-20).dp)
                ) {
                    configuracionView(
                        onBack = { showConfigDialog = false }
                    )
                }
            }
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
