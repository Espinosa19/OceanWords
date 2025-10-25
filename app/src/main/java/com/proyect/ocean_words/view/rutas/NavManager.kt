package com.proyect.ocean_words.view.rutas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyect.ocean_words.model.PistaEstado
import com.proyect.ocean_words.model.sampleShopItems
import com.proyect.ocean_words.view.InicioJuegoView // Importa tu vista de Splash
import com.proyect.ocean_words.view.OceanWordsGameUI
import com.proyect.ocean_words.view.caminoNiveles
import com.proyect.ocean_words.view.screens.BottomNavBar
import com.proyect.ocean_words.view.screens.GameShopScreen
import com.proyect.ocean_words.view.screens.PreviewGameShopScreen
import com.proyect.ocean_words.view.screens.burbujas
import com.proyect.ocean_words.view.screens.caracteristicasEspecieView

// --- Rutas de la Aplicación ---
object Rutas {
    const val SPLASH = "splash"
    const val JUEGO_PRINCIPAL = "juego_principal/{levelId}"
    const val CARACTERISTICAS = "caracteristicas"

    const val CONFIGURACION = "configuracion" // Nueva ruta
    const val CAMINO_NIVELES = "camino_niveles"
    const val ACUARIO = "acuario"
    const val TIENDA = "tienda"

    const val ACERCA_DE = "acerca_de"       // Nueva ruta
    const val GAME_SHOP="game_shop"
}
fun createAdivinaEspecieRoute(levelId: Int): String {
    return "juego_principal/$levelId"
}
private const val ANTES_TRANSCION = 800L
private const val TRANSICION_BURBUJAS = 1500L
@Composable
fun NavManager() {
    // El NavController siempre se crea dentro del NavManager
    val navController = rememberNavController()
    var targetLevelId by remember { mutableStateOf<Int?>(null) }
    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH // La aplicación comienza en la pantalla de carga
    ) {
        // DESTINO: Pantalla de Carga (Splash)
        composable(Rutas.SPLASH) {
            // Le pasamos la acción de navegación a InicioJuegoView
            InicioJuegoView(
                onNavigationToGame = {
                    navController.navigate(Rutas.CAMINO_NIVELES) {
                        // Importante: Elimina 'splash' de la pila para no poder volver atrás
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Rutas.JUEGO_PRINCIPAL,
            arguments = listOf(
                navArgument ("levelId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            OceanWordsGameUI(navController,levelId = levelId)
        }

        composable(Rutas.CAMINO_NIVELES) {
            Scaffold (
                containerColor = Color.Transparent,
                bottomBar = { BottomNavBar(navController) }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    caminoNiveles (
                        onStartTransitionAndNavigate = { levelId ->
                            targetLevelId = levelId
                        }
                    )
                }
            }
        }

        composable(Rutas.CONFIGURACION) {
            // Llama al Composable de tu vista principal
        }
        composable(Rutas.CARACTERISTICAS) {
            caracteristicasEspecieView(navController)
        }
        composable(Rutas.TIENDA) {
            Scaffold(
                bottomBar = { BottomNavBar(navController) }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    val onBuyAction: (PistaEstado) -> Unit = { itemComprado ->
                        println("¡Compra realizada! Ítem: ${itemComprado.type}")
                        // Aquí va la lógica real de tu juego
                    }

                    GameShopScreen(
                        items = sampleShopItems,
                        onBuy = onBuyAction
                    )
                }
            }
        }

        composable(Rutas.ACUARIO) {
            Scaffold(
                bottomBar = { BottomNavBar(navController) }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {

                }
            }
        }

        composable(Rutas.GAME_SHOP) {
            val onBuyAction: (PistaEstado) -> Unit = { itemComprado ->
                println("¡Compra realizada! Ítem: ${itemComprado.type}")
                // Aquí va la lógica real de tu juego
            }

            GameShopScreen(
                items = sampleShopItems,
                onBuy = onBuyAction
            )
        }

    }
    if (targetLevelId != null) {
        burbujas(modifier = Modifier.fillMaxSize())

        val currentLevelId = targetLevelId!!

        LaunchedEffect(currentLevelId) {
            kotlinx.coroutines.delay(ANTES_TRANSCION)
            navController.navigate(createAdivinaEspecieRoute(currentLevelId))
            kotlinx.coroutines.delay(TRANSICION_BURBUJAS - ANTES_TRANSCION)
            targetLevelId = null
        }
    }
}
