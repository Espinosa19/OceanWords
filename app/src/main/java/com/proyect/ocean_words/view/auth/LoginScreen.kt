package com.proyect.ocean_words.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.view.rutas.Rutas
import java.util.regex.Pattern

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

    val focusEmail = remember { FocusRequester() }
    val focusPassword = remember { FocusRequester() }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedBorderColor = Color(0xFF00B0FF),
        unfocusedBorderColor = Color.Gray,
        cursorColor = Color(0xFF00B0FF),
        focusedLabelColor = Color(0xFF00B0FF),
        unfocusedLabelColor = Color.Black,
        focusedPlaceholderColor = Color.Gray,
        unfocusedPlaceholderColor = Color.Gray
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0A2342), Color(0xFF065A82)))),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondoinicio1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.32f
        )

        val infinite = rememberInfiniteTransition(label = "")
        val bubbleMovement by infinite.animateFloat(
            initialValue = 0f,
            targetValue = 25f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = ""
        )

        Box(modifier = Modifier.fillMaxSize()) {
            repeat(7) { i ->
                Box(
                    modifier = Modifier
                        .offset(
                            x = (30 * i).dp,
                            y = (bubbleMovement * (i + 1)).dp
                        )
                        .size((10 + i * 5).dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.25f))
                )
            }
        }

        // ⭐⭐ RECUADRO ARREGLADO AQUÍ ⭐⭐
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(22.dp),
                    clip = false
                )
                .background(Color.White, shape = RoundedCornerShape(22.dp))
                .padding(32.dp)
                .graphicsLayer {
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF478DC4), fontWeight = FontWeight.Bold)) {
                        append("¡Bienvenido, \n")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF00B0FF), fontWeight = FontWeight.ExtraBold)) {
                        append("Ocean Words!\n")
                    }
                },
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusEmail),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(
                    onNext = { focusPassword.requestFocus() }
                )
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painterResource(id = icon), contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusPassword),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        when {
                            email.isBlank() -> showError("Ingrese su correo electrónico")
                            !isValidEmail(email) -> showError("Ingrese un correo válido")
                            password.isBlank() -> showError("Ingrese su contraseña")
                            password.length < 6 -> showError("La contraseña debe tener al menos 6 caracteres")
                            else -> authViewModel.loginUser(email, password)
                        }
                    }
                )
            )

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = {
                    if (email.isBlank()) showError("Ingrese su correo para recuperar la contraseña")
                    else if (!isValidEmail(email)) showError("Ingrese un correo válido")
                    else {
                        authViewModel.resetPassword(email)
                        showError("Se ha enviado un enlace de recuperación a su correo")
                    }
                }
            ) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    color = Color(0xFF163C75),
                    fontWeight = FontWeight.Medium,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        email.isBlank() -> showError("Ingrese su correo electrónico")
                        !isValidEmail(email) -> showError("Ingrese un correo válido")
                        password.isBlank() -> showError("Ingrese su contraseña")
                        password.length < 6 -> showError("La contraseña debe tener al menos 6 caracteres")
                        else -> authViewModel.loginUser(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6CBBD7))
            ) {
                Text("INICIAR SESIÓN", color = Color.White)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate(Rutas.REGISTRO) }) {
                Text(
                    "¿No tienes cuenta? Registrarse",
                    color = Color(0xFF163C75),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showDialog) {
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

fun isValidEmail(email: String): Boolean {
    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    )
    return emailPattern.matcher(email).matches()
}
