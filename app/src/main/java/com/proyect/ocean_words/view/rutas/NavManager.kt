package com.proyect.ocean_words.view.rutas

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyect.ocean_words.auth.AuthViewModel

import com.proyect.ocean_words.auth.LoginScreen
import com.proyect.ocean_words.auth.RegisterScreen
import com.proyect.ocean_words.model.PistaEstado
import com.proyect.ocean_words.model.sampleShopItems
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.CaminoNivelesRoute
import com.proyect.ocean_words.view.LoadingScreenOceanWords
import com.proyect.ocean_words.view.LoadingScreenOceanWordsAnimated
import com.proyect.ocean_words.view.OceanWordsGameRoute
import com.proyect.ocean_words.view.screens.BottomNavBar
import com.proyect.ocean_words.view.screens.GameShopScreen
import com.proyect.ocean_words.view.screens.caracteristicasEspecieView
import com.proyect.ocean_words.viewmodels.NivelViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// --- Rutas de la Aplicación ---
object Rutas {
    const val SPLASH = "splash"
    const val JUEGO_PRINCIPAL = "juego_principal/{levelId}"
    const val CARACTERISTICAS = "caracteristicas"

    const val CONFIGURACION = "configuracion" // Nueva ruta
    const val CAMINO_NIVELES = "camino_niveles"
    const val ACUARIO = "acuario"
    const val LOADING = "loading_screen"
    //const val LOADING = "loading_screen/{nivel}"
    const val LOADING_ANIMADO = "loading_animado"
    const val TIENDA = "tienda"

    const val ACERCA_DE = "acerca_de"       // Nueva ruta
    const val GAME_SHOP="game_shop"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
}

private const val ANTES_TRANSCION = 800L
private const val TRANSICION_BURBUJAS = 1500L

@Composable
fun NavManager(
    musicManager: MusicManager,
    isAppInForeground: Boolean,
    nivelViewModel: NivelViewModel,
) {
    val navController = rememberNavController()
    var targetLevelId by remember { mutableStateOf<Int?>(null) }
    var isMusicGloballyEnabled by remember { mutableStateOf(true) }
    val vidas by nivelViewModel.vidas.collectAsState()
    val timeToNextLife by nivelViewModel.timeToNextLife.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Rutas.LOADING_ANIMADO // La aplicación comienza en la pantalla de carga
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
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playMenuMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }

            CaminoNivelesRoute(
                navController = navController,
                musicManager = musicManager,
                isAppInForeground = isAppInForeground,
                viewModel = nivelViewModel,
                isMusicGloballyEnabled = isMusicGloballyEnabled,
                onMusicToggle = { isEnabled ->
                    isMusicGloballyEnabled = isEnabled
                },
                vidas = vidas,
                timeToNextLife = timeToNextLife
            )
        }
        composable(
            route = "nivel/{id}/{especie_id}/{nombre}/{dificultad}/{imagen}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("especie_id") { type = NavType.StringType },
                navArgument("nombre") { type = NavType.StringType },
                navArgument("dificultad") { type = NavType.StringType },
                navArgument("imagen") { type = NavType.StringType }

            )
        ) { backStackEntry ->

            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playLevelMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
            val levelId = backStackEntry.arguments?.getInt("id")
            val especie_id =backStackEntry.arguments?.getString("especie_id")
            val nombre = backStackEntry.arguments?.getString("nombre")
            val dificultad = backStackEntry.arguments?.getString("dificultad")
            val imagen: String? = backStackEntry.arguments?.getString("imagen")
            val nombreLimpio = URLDecoder.decode(nombre, StandardCharsets.UTF_8.toString())
            if (levelId != null) {
                if (nombre != null) {
                    if (dificultad != null) {
                        if (especie_id != null) {
                            OceanWordsGameRoute(
                                navController = navController,
                                levelId = levelId,
                                isAppInForeground = isAppInForeground,
                                musicManager = musicManager,
                                nombre =nombreLimpio,
                                dificultad =dificultad,
                                especieId = especie_id,
                                imagen=imagen,
                                isMusicGloballyEnabled = isMusicGloballyEnabled,
                                onMusicToggle = { isEnabled ->
                                    isMusicGloballyEnabled = isEnabled
                                },
                                nivelViewModel = nivelViewModel
                            )
                        }
                    }
                }
            }
        }
        composable(
            route = "caracteristicas/{especie_id}/{imagen}",
            arguments = listOf(
                navArgument("especie_id") { type = NavType.StringType },
                navArgument("imagen") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val especie_id =backStackEntry.arguments?.getString("especie_id")
            val imagen: String? = backStackEntry.arguments?.getString("imagen")

            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playLevelMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
            if (especie_id != null) {
                caracteristicasEspecieView(navController,especie_id,imagen)
            }
        }



        composable(Rutas.CONFIGURACION) {

            com.proyect.ocean_words.view.screens.configuracionView(
                onBack = {
                    musicManager.playClickSound()
                    navController.popBackStack()
                    if (isMusicGloballyEnabled) {
                        musicManager.playMenuMusic()
                    }
                },
                musicManager = musicManager,
                onMusicToggle = { isEnabled ->
                    musicManager.playClickSound()
                    isMusicGloballyEnabled = isEnabled
                    if (!isEnabled) {
                        musicManager.stopAllMusic()
                    }else {
                        musicManager.playMenuMusic()
                    }
                },
                isMusicEnabled = isMusicGloballyEnabled,
                //onButtonClick = musicManager::playClickSound
            )
        }

        composable(Rutas.TIENDA) {
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                Log.d("MusicDebug", "Navigating to TIENDA. isMusicGloballyEnabled: $isMusicGloballyEnabled") // <-- Añade esto
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playMenuMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
            Scaffold(
                bottomBar = { BottomNavBar(navController, onItemClick = musicManager::playClickSound) }
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
                        navController,
                        musicManager
                    )
                }
            }
        }

        composable(Rutas.ACUARIO) {
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playMenuMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
            Scaffold(
                bottomBar = { BottomNavBar(navController, onItemClick = musicManager::playClickSound) }
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
                navController,
                musicManager
            )
        }

        composable(Rutas.LOADING) {
            val niveles = nivelViewModel.niveles.collectAsState().value // si tienes Flow o LiveData
            val levelId = targetLevelId ?: 2 // reemplaza con tu nivel actual
            LoadingScreenOceanWords(
                currentLevelId = levelId,
                niveles = niveles,
                navController = navController
            )
        }
        composable(Rutas.LOGIN) {
            val authViewModel: AuthViewModel = viewModel()
            LoginScreen(navController, authViewModel)
        }

        composable(Rutas.REGISTRO) {
            val authViewModel: AuthViewModel = viewModel()
            RegisterScreen(navController, authViewModel)
        }
        composable(Rutas.LOADING_ANIMADO) {
            LoadingScreenOceanWordsAnimated(navController)
        }





    }

}