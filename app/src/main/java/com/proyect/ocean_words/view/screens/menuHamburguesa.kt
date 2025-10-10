package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.* // Importa todos los componentes de Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.proyect.ocean_words.ui.theme.grey10
@Composable
fun NavegacionDrawerMenu(
    navController: NavController,
    onCloseMenu: () -> Unit // 🔹 Nuevo parámetro
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val menuWidth = screenWidth * 0.65f // 65% del ancho

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable { onCloseMenu() }
            .wrapContentHeight(Alignment.Top) // Ajusta su altura al contenido, alineado arriba

    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .width(menuWidth)
                .align(Alignment.CenterStart) // 🔹 Asegura que el menú quede pegado a la izquierda
                .clickable(enabled = false) {}, // Evita que clics fuera lo cierren
            color = LightOlive
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(LightOlive)
            ) {
                DrawerHeader()

                Spacer(modifier = Modifier.height(24.dp))

                MenuItem(
                    icon = Icons.Filled.Home,
                    title = "Inicio",
                    onClick = {
                        onCloseMenu()
                    }
                )

                MenuItem(
                    icon = Icons.Filled.Settings,
                    title = "Configuraciones",
                    onClick = {
                        onCloseMenu()
                        navController.navigate("settings")
                    }
                )

                MenuItemWithImage(
                    resourceId = R.drawable.idea,
                    title = "Pista",
                    onClick = {
                        onCloseMenu()
                        navController.navigate("pista")
                    }
                )

                MenuItem(
                    icon = Icons.Filled.Info,
                    title = "Acerca de",
                    onClick = {
                        onCloseMenu()
                        navController.navigate("info")
                    }
                )
            }
        }
    }

}

// --- Componente para la Cabecera del Menú ---
@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Podrías poner aquí un logo o un avatar
        Image(
            painter = painterResource(id = R.mipmap.ic_logo_ocean),
            contentDescription = "Logo Ocean Words",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ocean Words",
            color = Color.Black,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

// --- Componente para un Ítem Estándar (con Icono) ---
@Composable
fun MenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    // NavigationDrawerItem es el componente recomendado de M3 para esto
    NavigationDrawerItem(
        label = { Text(text = title, color = Color.Black) },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Black
            )
        },
        selected = false, // Podrías cambiar esto para resaltar el ítem actual
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = Color.Transparent, // Fondo transparente
            unselectedIconColor = Color.White,
            unselectedTextColor = Color.White,
            // Agrega un color de fondo al presionar para feedback visual
            selectedContainerColor = LightOlive.copy(alpha = 0.2f)
        )
    )
}

// --- Componente para un Ítem con Imagen Local ---
@Composable
fun MenuItemWithImage(resourceId: Int, title: String, onClick: () -> Unit) {
    // Usamos Row con un modificador 'clickable' para mantener la estructura visual
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 28.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = title,
            modifier = Modifier.size(24.dp) // Iconos e imágenes deben tener tamaño consistente
        )
        Spacer(modifier = Modifier.width(28.dp))
        Text(
            text = title,
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}