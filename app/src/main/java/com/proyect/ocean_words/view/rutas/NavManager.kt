package com.proyect.ocean_words.view.rutas

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyect.ocean_words.model.PistaEstado
import com.proyect.ocean_words.model.sampleShopItems
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.InicioJuegoView // Importa tu vista de Splash
import com.proyect.ocean_words.view.OceanWordsGameUI
import com.proyect.ocean_words.view.caminoNiveles
import com.proyect.ocean_words.view.screens.BottomNavBar
import com.proyect.ocean_words.view.screens.GameShopScreen
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
fun NavManager(musicManager: MusicManager, isAppInForeground: Boolean) {
    val navController = rememberNavController()
    var targetLevelId by remember { mutableStateOf<Int?>(null) }
    var isMusicGloballyEnabled by remember { mutableStateOf(true) }

    NavHost(
        navController = navController,
        startDestination = Rutas.CAMINO_NIVELES // La aplicación comienza en la pantalla de carga
    ) {
        // DESTINO: Pantalla de Carga (Splash)
        composable(route = Rutas.JUEGO_PRINCIPAL,
            arguments = listOf(
                navArgument ("levelId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            // Controla la reproducción/reanudación al navegar o volver de segundo plano
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playLevelMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
            val levelId = backStackEntry.arguments?.getInt("levelId") ?: 1
            OceanWordsGameUI(
                navController,
                levelId = levelId,
                musicManager = musicManager,
                onMusicToggle = { isEnabled ->
                    isMusicGloballyEnabled = isEnabled
                    if (!isEnabled) {
                        musicManager.stopAllMusic()
                    } else {
                        musicManager.playLevelMusic()
                    }
                },
                isMusicEnabled = isMusicGloballyEnabled
            )
        }

        composable(Rutas.CAMINO_NIVELES) {

            // 1. ESTADO CLAVE: Controla si la pantalla de inicio debe mostrarse.
            var showSplashScreen by remember { mutableStateOf(true) }

            // 2. CONTROL DE MÚSICA UNIFICADO: Este LaunchedEffect gestiona la reproducción
            // inicial y los cambios de estado (si la app pasa a segundo plano o si el usuario
            // activa/desactiva la música). Se ejecuta al inicio de la composición.
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playMenuMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }

            // --- ESTRUCTURA DE VISTAS ---
            if (showSplashScreen) {
                // 3. Muestra la pantalla de inicio (splash/carga)
                InicioJuegoView(
                    onLoadingComplete = {
                        // 4. Cuando la carga interna termine, oculta la vista.
                        showSplashScreen = false
                    }
                )
            } else {
                // 5. Muestra el contenido principal de la ruta (Scaffold)
                Scaffold (
                    containerColor = Color.Transparent,
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        caminoNiveles (
                            onStartTransitionAndNavigate = { levelId ->
                                // Asumiendo que 'targetLevelId' está definido como un MutableState
                                // en el ámbito superior (ej: el NavHost).
                                targetLevelId = levelId
                            },
                            navController = navController,
                            musicManager = musicManager,
                            onMusicToggle = { isEnabled ->
                                // Asumiendo que 'isMusicGloballyEnabled' está definido como un MutableState
                                // en el ámbito superior.
                                isMusicGloballyEnabled = isEnabled
                                // La lógica de play/stop se manejará automáticamente
                                // en el LaunchedEffect de arriba (punto 2)
                            },
                            isMusicEnabled = isMusicGloballyEnabled
                        )
                    }
                } // Fin del Scaffold
            } // Fin del else
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
    if (targetLevelId != null) {
        //burbujas(modifier = Modifier.fillMaxSize())

        val currentLevelId = targetLevelId!!
        LaunchedEffect(currentLevelId) {
            //kotlinx.coroutines.delay(ANTES_TRANSCION)
            navController.navigate(createAdivinaEspecieRoute(currentLevelId))
            //kotlinx.coroutines.delay(TRANSICION_BURBUJAS - ANTES_TRANSCION)
            targetLevelId = null
        }
    }
}