package com.proyect.ocean_words.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.view.rutas.Rutas

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val authMessage by authViewModel.authState.collectAsState()
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    val scaleAnim = remember { Animatable(0.8f) }
    LaunchedEffect(true) { scaleAnim.animateTo(1f, tween(800)) }

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate(Rutas.CAMINO_NIVELES) {
                popUpTo(Rutas.LOGIN) { inclusive = true }
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf<String?>(null) }

    fun showError(message: String) {
        dialogMessage = message
        showDialog = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF0A2342), Color(0xFF065A82)))),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(RoundedCornerShape(40.dp))
                .shadow(10.dp, RoundedCornerShape(40.dp))
                .background(Color.White)
                .padding(32.dp)
                .graphicsLayer { scaleX = scaleAnim.value; scaleY = scaleAnim.value },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "¡Bienvenido a\nOcean Words!",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color(0xFF00B0FF),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.Black) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val iconRes = if (passwordVisible) com.proyect.ocean_words.R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painterResource(id = iconRes), contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
            )

            Spacer(Modifier.height(12.dp))

            // Recuperación de contraseña
            TextButton(
                onClick = {
                    if (email.isBlank()) {
                        showError("Ingrese su correo para recuperar la contraseña")
                    } else {
                        authViewModel.resetPassword(email)
                        showError("Se ha enviado un enlace de recuperación a su correo")
                    }
                }
            ) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    color = Color(0xFF00B0FF),
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        email.isBlank() && password.isBlank() -> showError("Ingrese correo y contraseña")
                        email.isBlank() -> showError("Ingrese correo electrónico")
                        password.isBlank() -> showError("Ingrese contraseña")
                        else -> authViewModel.loginUser(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF))
            ) { Text("INICIAR SESIÓN", color = Color.White) }

            Spacer(Modifier.height(16.dp))
            Text("¿No tienes cuenta?", color = Color.Black)
            Spacer(Modifier.height(6.dp))
            Button(
                onClick = { navController.navigate(Rutas.REGISTRO) },
                modifier = Modifier.fillMaxWidth().height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                shape = RoundedCornerShape(20.dp)
            ) { Text("REGISTRAR CUENTA") }
        }
    }

    if (showDialog) {
        // Diálogo estilizado como un cuadro azul (similar al correo)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF007ACC))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Hola!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = dialogMessage ?: "",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Cerrar", color = Color(0xFF007ACC))
                }
            }
        }
    }
}
