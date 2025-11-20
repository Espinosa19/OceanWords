package com.proyect.ocean_words.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

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

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Aviso") },
            text = { Text(dialogMessage ?: "") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF0A2342), Color(0xFF065A82)))),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondoinicio1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

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

            // Texto mejorado de bienvenida
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)) {
                        append("¡Bienvenido a\n")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF00B0FF), fontWeight = FontWeight.ExtraBold)) {
                        append("Ocean Words!")
                    }
                },
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            Spacer(Modifier.height(24.dp))

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
                    val icon = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painterResource(id = icon), contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
            )

            Spacer(Modifier.height(28.dp))

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
}
