package com.proyect.ocean_words.view.rutas

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.proyect.ocean_words.viewmodels.AuthViewModel

import com.proyect.ocean_words.view.auth.LoginScreen
import com.proyect.ocean_words.view.auth.RegisterScreen
import com.proyect.ocean_words.model.PistaEstado
import com.proyect.ocean_words.model.sampleShopItems
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.CaminoNivelesRoute
import com.proyect.ocean_words.view.LoadingScreenOceanWords
import com.proyect.ocean_words.view.OceanWordsGameRoute
import com.proyect.ocean_words.view.StartScreen
import com.proyect.ocean_words.view.auth.ProcesoAccesoScreen
import com.proyect.ocean_words.view.screens.BottomNavBar
import com.proyect.ocean_words.view.screens.GameShopScreen
import com.proyect.ocean_words.view.screens.acuario
import com.proyect.ocean_words.view.caracteristicasEspecieView
import com.proyect.ocean_words.view.screens.configuracionView
import com.proyect.ocean_words.viewmodels.AuthViewModelFactory
import com.proyect.ocean_words.viewmodels.NivelViewModel
import com.proyect.ocean_words.viewmodels.ProgresoViewModel
import com.proyect.ocean_words.viewmodels.UsuariosViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// --- Rutas de la Aplicación ---
object Rutas {
    const val SPLASH = "splash"
    const val JUEGO_PRINCIPAL = "juego_principal/{levelId}"
    const val CARACTERISTICAS = "caracteristicas"

    const val CONFIGURACION = "configuracion"
    const val CAMINO_NIVELES = "camino_niveles"
    const val ACUARIO = "acuario"

    //const val LOADING = "loading_screen/{nivel}"
    const val LOADING_ANIMADO = "loading_animado"
    const val TIENDA = "tienda"

    const val ACERCA_DE = "acerca_de"
    const val GAME_SHOP="game_shop"
    const val LOGIN = "login"
    const val PROCESOACCESO="proceso_acceso"
    const val REGISTRO = "registro"
}

private const val ANTES_TRANSCION = 800L
private const val TRANSICION_BURBUJAS = 1500L

@Composable
fun NavManager(
    musicManager: MusicManager,
    isAppInForeground: Boolean,
    nivelViewModel: NivelViewModel,
    progresoViewModel: ProgresoViewModel,
    googleSignInClient: GoogleSignInClient,
    usuarioViwModel: UsuariosViewModel,
) {
    val navController = rememberNavController()
    var targetLevelId by remember { mutableStateOf<Int?>(null) }
    var isMusicGloballyEnabled by remember { mutableStateOf(true) }


    NavHost(
        navController = navController,
        startDestination = Rutas.LOADING_ANIMADO
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
                usuarioViwModel = usuarioViwModel
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
                                nivelViewModel = nivelViewModel,
                                progresoViewModel =progresoViewModel,
                                usuarioViwModel= usuarioViwModel
                            )
                        }
                    }
                }
            }
        }
        composable(
            route = "caracteristicas/{especie_id}/{imagen}/{levelId}",
            arguments = listOf(
                navArgument("especie_id") { type = NavType.StringType },
                navArgument("imagen") { type = NavType.StringType },
                navArgument("levelId") { type = NavType.IntType }

            )
        ) { backStackEntry ->
            val especie_id =backStackEntry.arguments?.getString("especie_id")
            val imagen: String? = backStackEntry.arguments?.getString("imagen")
            val levelId = backStackEntry.arguments?.getInt("levelId")
            LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
                if (isMusicGloballyEnabled && isAppInForeground) {
                    musicManager.playLevelMusic()
                } else {
                    musicManager.stopAllMusic()
                }
            }
            if (especie_id != null) {
                caracteristicasEspecieView(navController,especie_id,imagen,levelId, musicManager, nivelViewModel)
            }
        }



        composable(Rutas.CONFIGURACION) {

            configuracionView(
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
                Log.d("MusicDebug", "Navigating to TIENDA. isMusicGloballyEnabled: $isMusicGloballyEnabled")
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
                containerColor = Color.Transparent,
                bottomBar = { BottomNavBar(navController, onItemClick = musicManager::playClickSound) }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .background(Color.Transparent)
                ) {
                    acuario()
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

        composable(
            route = "loading_screen/{levelId}",
            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
        ) { backStackEntry ->

            val niveles by nivelViewModel.niveles.collectAsState()
            val totalLevels = niveles.size

            val nextLevelId = backStackEntry.arguments?.getInt("levelId")

            val isBeyondLastLevel = nextLevelId != null && nextLevelId > totalLevels

            LaunchedEffect(isBeyondLastLevel) {
                if (isBeyondLastLevel) {
                    val caminoNivelesEntry = navController.getBackStackEntry(Rutas.CAMINO_NIVELES)
                    caminoNivelesEntry.savedStateHandle["GAME_COMPLETED_KEY"] = true
                    navController.popBackStack(
                        route = Rutas.CAMINO_NIVELES,
                        inclusive = false
                    )
                }
            }

            if (nextLevelId != null && !isBeyondLastLevel) {
                LoadingScreenOceanWords(
                    currentLevelId = nextLevelId,
                    niveles = niveles,
                    navController = navController
                )
            } else if (isBeyondLastLevel) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
        composable(Rutas.PROCESOACCESO) {
            val context = LocalContext.current

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(context)
            )
            ProcesoAccesoScreen(navController, authViewModel, musicManager = musicManager, isMusicGloballyEnabled = isMusicGloballyEnabled, isAppInForeground = isAppInForeground,googleSignInClient)
        }
        composable(Rutas.LOGIN) {
            val context = LocalContext.current

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(context)
            )
            LoginScreen(navController, authViewModel, musicManager = musicManager, isMusicGloballyEnabled = isMusicGloballyEnabled, isAppInForeground = isAppInForeground)
        }

        composable(Rutas.REGISTRO) {
            val context = LocalContext.current

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(context)
            )
            RegisterScreen(navController, authViewModel, musicManager = musicManager, isMusicGloballyEnabled = isMusicGloballyEnabled, isAppInForeground = isAppInForeground)
        }
        composable(Rutas.LOADING_ANIMADO) {
            val context = LocalContext.current

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(context)
            )
            StartScreen(navController,authViewModel)
        }

    }

}