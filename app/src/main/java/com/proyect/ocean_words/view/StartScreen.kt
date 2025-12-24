package com.proyect.ocean_words.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.proyect.ocean_words.core.security.JwtManager
import com.proyect.ocean_words.core.security.SecurePreferences
import com.proyect.ocean_words.view.rutas.Rutas
import com.proyect.ocean_words.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        authViewModel.checkSession()
    }
    // 🔐 JWT Manager
    val jwtManager = remember {
        JwtManager(SecurePreferences(context))
    }

    // 🔄 Estado de carga real
    var isCheckingSession by remember { mutableStateOf(true) }

    // 🔐 Verificación de sesión vinculada a la animación
    LaunchedEffect(Unit) {
        // Tiempo mínimo visual (opcional, mejora UX)
        delay(1200)

        val token = jwtManager.get()

        isCheckingSession = false

        if (!token.isNullOrBlank()) {
            navController.navigate(Rutas.CAMINO_NIVELES) {
                popUpTo(0)
            }
        } else {
            navController.navigate(Rutas.PROCESOACCESO) {
                popUpTo(0)
            }
        }
    }

    // 🔹 UI mientras se valida la sesión
    if (isCheckingSession) {
        LoadingScreenOceanWordsAnimated(navController)
    }
}
