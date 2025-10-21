package com.proyect.ocean_words.view.rutas

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyect.ocean_words.model.PistaEstado
import com.proyect.ocean_words.model.sampleShopItems
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.CaminoNivelesRoute
import com.proyect.ocean_words.view.OceanWordsGameRoute
import com.proyect.ocean_words.view.screens.BottomNavBar
import com.proyect.ocean_words.view.screens.GameShopScreen
import com.proyect.ocean_words.view.screens.caracteristicasEspecieView
import com.proyect.ocean_words.viewmodels.EspecieViewModel
import com.proyect.ocean_words.viewmodels.NivelViewModel

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

private const val ANTES_TRANSCION = 800L
private const val TRANSICION_BURBUJAS = 1500L
fun createAdivinaEspecieRoute(levelId: Int): String {
    return "juego_principal/$levelId"
}
@Composable
fun NavManager(
    musicManager: MusicManager,
    isAppInForeground: Boolean,
    nivelViewModel: NivelViewModel,
    especieViewModel: EspecieViewModel
) {
    val navController = rememberNavController()
    var targetLevelId by remember { mutableStateOf<Int?>(null) }
    var isMusicGloballyEnabled by remember { mutableStateOf(true) }

    NavHost(
        navController = navController,
        startDestination = Rutas.CAMINO_NIVELES // La aplicación comienza en la pantalla de carga
    ) {
        // DESTINO: Pantalla de Carga (Splash)
//        composable(route = Rutas.JUEGO_PRINCIPAL,
//            arguments = listOf(
//                navArgument ("levelId") {
//                    type = NavType.IntType
//                    defaultValue = 0
//                }
//            )
//        ) { backStackEntry ->
//            // Controla la reproducción/reanudación al navegar o volver de segundo plano
//
//        }

        composable(Rutas.CAMINO_NIVELES) {


            CaminoNivelesRoute(navController,musicManager,isAppInForeground,nivelViewModel)

        }
        composable(
            route = "nivel/{id}", // <- La clave es la ruta con el argumento
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val levelId = backStackEntry.arguments?.getInt("id") ?: 1
            OceanWordsGameRoute(
                navController = navController,
                levelId = levelId,
                musicManager = musicManager,
                isAppInForeground = isAppInForeground,
                viewModel = especieViewModel
            )
        }


        composable(Rutas.CONFIGURACION) {

            com.proyect.ocean_words.view.screens.configuracionView(
                onBack = {
                    navController.popBackStack()

                    if (isMusicGloballyEnabled) {
                        musicManager.playMenuMusic()
                    }
                },
                musicManager = musicManager, // **Pasamos el MusicManager**
                onMusicToggle = { isEnabled ->
                    isMusicGloballyEnabled = isEnabled
                    if (!isEnabled) {
                        musicManager.stopAllMusic()
                    }else {
                        musicManager.playMenuMusic()
                    }
                },
                isMusicEnabled = isMusicGloballyEnabled
            )
        }
        composable(Rutas.CARACTERISTICAS) {
            // Controla la reproducción/reanudación al navegar o volver de segundo plano
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playLevelMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
            caracteristicasEspecieView(navController)
        }
        composable(Rutas.TIENDA) {
            // Controla la reproducción/reanudación al navegar o volver de segundo plano
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                Log.d("MusicDebug", "Navigating to TIENDA. isMusicGloballyEnabled: $isMusicGloballyEnabled") // <-- Añade esto
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playMenuMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
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
                        onBuy = onBuyAction,
                        false,
                        navController
                    )
                }
            }
        }

        composable(Rutas.ACUARIO) {
            // Controla la reproducción/reanudación al navegar o volver de segundo plano
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playMenuMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
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
                onBuy = onBuyAction,
                true,
                navController
            )
        }

    }

}