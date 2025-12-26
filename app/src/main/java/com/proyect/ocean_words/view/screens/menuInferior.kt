package com.proyect.ocean_words.view.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseInOut
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.NavItemEstado
import com.proyect.ocean_words.view.theme.whiteBoxColor
import com.proyect.ocean_words.view.rutas.Rutas
import com.proyect.ocean_words.viewmodels.UsuariosViewModel

@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        NavItemEstado(
            label = "Tienda",
            drawableResId = R.drawable.tienda_pez,
            route = Rutas.TIENDA
        ),
        NavItemEstado(
            label = "Inicio",
            drawableResId = R.drawable.concha,
            route = Rutas.CAMINO_NIVELES
        ),
        NavItemEstado(
            label = "Acuario",
            drawableResId = R.drawable.alimento,
            route = Rutas.ACUARIO
        )
    )

    NavigationBar(
        containerColor = Color(0xFF2EB2DA),
        tonalElevation = 10.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.5f else 1.0f,
                animationSpec = tween(durationMillis = 300, easing = EaseInOut),
                label = "icon_scale_animation"
            )

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onItemClick()
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Box(
                        modifier = Modifier.size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = item.drawableResId),
                            contentDescription = item.label,
                            modifier = Modifier
                                .size(45.dp)
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                        )
                    }
                },
                label = { Text(item.label, style = MaterialTheme.typography.labelMedium, color = whiteBoxColor) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF076C8C),
                    unselectedIconColor = Color.White,
                    indicatorColor = Color(0xFF2EB2DA)
                )
            )
        }
    }
}

