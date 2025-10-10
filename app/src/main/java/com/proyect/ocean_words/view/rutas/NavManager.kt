package com.proyect.ocean_words.view.rutas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.proyect.ocean_words.view.InicioJuegoView // Importa tu vista de Splash
import com.proyect.ocean_words.view.OceanWordsGameUI
import com.proyect.ocean_words.view.screens.caracteristicasEspecieView

// --- Rutas de la Aplicación ---
object Rutas {
    const val SPLASH = "splash"
    const val JUEGO_PRINCIPAL = "juego_principal"
    const val CARACTERISTICAS = "caracteristicas"

    const val CONFIGURACION = "configuracion" // Nueva ruta
    const val ACERCA_DE = "acerca_de"       // Nueva ruta
}

@Composable
fun NavManager() {
    // El NavController siempre se crea dentro del NavManager
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.SPLASH // La aplicación comienza en la pantalla de carga
    ) {
        // DESTINO: Pantalla de Carga (Splash)
        composable(Rutas.SPLASH) {
            // Le pasamos la acción de navegación a InicioJuegoView
            InicioJuegoView(
                onNavigationToGame = {
                    navController.navigate(Rutas.JUEGO_PRINCIPAL) {
                        // Importante: Elimina 'splash' de la pila para no poder volver atrás
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // DESTINO: Pantalla Principal del Juego
        composable(Rutas.JUEGO_PRINCIPAL) {
            // Llama al Composable de tu vista principal
            OceanWordsGameUI(navController)
        }
        composable(Rutas.CONFIGURACION) {
            // Llama al Composable de tu vista principal
        }
        composable(Rutas.CARACTERISTICAS) {
            caracteristicasEspecieView(navController)
        }
    }
}
