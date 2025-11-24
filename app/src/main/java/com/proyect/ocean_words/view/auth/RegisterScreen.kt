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
import androidx.compose.ui.geometry.Offset
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.withStyle
import com.proyect.ocean_words.R

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {

    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val authMessage by authViewModel.authState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val fullnameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val confirmEmailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }

    // Animación inicial
    val scaleAnim = remember { Animatable(0.8f) }
    LaunchedEffect(true) { scaleAnim.animateTo(1f, tween(800)) }

    // Estado de autenticación
    LaunchedEffect(authMessage) {
        when (authMessage) {
            "Registro exitoso" -> showSuccessDialog = true
            else -> if (!authMessage.isNullOrEmpty()) {
                errorMessage = authMessage
                showErrorDialog = true
            }
        }
    }

    // Ventana de error
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Aviso") },
            text = { Text(errorMessage ?: "") },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF065A82)),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondoinicio2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(24.dp)
                .graphicsLayer { scaleX = scaleAnim.value; scaleY = scaleAnim.value },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)) {
                        append("¡Bienvenido, \n")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF00B0FF), fontWeight = FontWeight.ExtraBold)) {
                        append("Nuevo Explorador!\n")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF137BAB), fontWeight = FontWeight.ExtraBold)) {
                        append("Regístrate para esta aventura")
                    }
                },
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = fullname,
                onValueChange = { fullname = it },
                placeholder = { Text("Nombre completo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(fullnameFocus),
                shape = RoundedCornerShape(12.dp),
                isError = fullname.isNotEmpty() && !isValidName(fullname),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { emailFocus.requestFocus() }
                )
            )
            if (fullname.isNotEmpty() && !isValidName(fullname)) {
                Text("El nombre solo puede contener letras", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(10.dp))

            // ------ EMAIL ------
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocus),
                shape = RoundedCornerShape(12.dp),
                isError = email.isNotEmpty() && !isValidEmailR(email),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { confirmEmailFocus.requestFocus() }
                )
            )
            if (email.isNotEmpty() && !isValidEmailR(email)) {
                Text("Correo inválido", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(10.dp))

            // ------ CONFIRMAR EMAIL ------
            OutlinedTextField(
                value = confirmEmail,
                onValueChange = { confirmEmail = it },
                placeholder = { Text("Confirmar correo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(confirmEmailFocus),
                shape = RoundedCornerShape(12.dp),
                isError = confirmEmail.isNotEmpty() && confirmEmail != email,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocus.requestFocus() }
                )
            )
            if (confirmEmail.isNotEmpty() && confirmEmail != email) {
                Text("Los correos no coinciden", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(Modifier.height(10.dp))

            // ------ PASSWORD ------
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
                    .focusRequester(passwordFocus),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { confirmPasswordFocus.requestFocus() }
                )
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirmar contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(painterResource(id = icon), contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(confirmPasswordFocus),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        when {
                            fullname.isBlank() -> { errorMessage = "Ingrese su nombre completo"; showErrorDialog = true }
                            !isValidName(fullname) -> { errorMessage = "El nombre solo puede contener letras"; showErrorDialog = true }
                            email.isBlank() -> { errorMessage = "Ingrese correo electrónico"; showErrorDialog = true }
                            !isValidEmailR(email) -> { errorMessage = "Correo inválido"; showErrorDialog = true }
                            confirmEmail.isBlank() -> { errorMessage = "Confirme su correo"; showErrorDialog = true }
                            email != confirmEmail -> { errorMessage = "Los correos no coinciden"; showErrorDialog = true }
                            password.isBlank() -> { errorMessage = "Ingrese contraseña"; showErrorDialog = true }
                            confirmPassword.isBlank() -> { errorMessage = "Confirme su contraseña"; showErrorDialog = true }
                            password != confirmPassword -> { errorMessage = "Las contraseñas no coinciden"; showErrorDialog = true }
                            else -> authViewModel.registerUser(fullname,email,password)
                        }
                    }
                )
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    when {
                        fullname.isBlank() -> { errorMessage = "Ingrese su nombre completo"; showErrorDialog = true }
                        !isValidName(fullname) -> { errorMessage = "El nombre solo puede contener letras"; showErrorDialog = true }
                        email.isBlank() -> { errorMessage = "Ingrese correo electrónico"; showErrorDialog = true }
                        !isValidEmailR(email) -> { errorMessage = "Correo inválido"; showErrorDialog = true }
                        confirmEmail.isBlank() -> { errorMessage = "Confirme su correo"; showErrorDialog = true }
                        email != confirmEmail -> { errorMessage = "Los correos no coinciden"; showErrorDialog = true }
                        password.isBlank() -> { errorMessage = "Ingrese contraseña"; showErrorDialog = true }
                        confirmPassword.isBlank() -> { errorMessage = "Confirme su contraseña"; showErrorDialog = true }
                        password != confirmPassword -> { errorMessage = "Las contraseñas no coinciden"; showErrorDialog = true }
                        else -> authViewModel.registerUser(fullname,email,password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B0FF))
            ) {
                Text("REGISTRAR", color = Color.White)
            }

            Spacer(Modifier.height(10.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("¿Ya tienes cuenta? Iniciar sesión", color = Color.Black)
            }
        }
    }

    if (showSuccessDialog) {
        val scale by animateFloatAsState(targetValue = 1f, animationSpec = tween(500), label = "")
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val bubbleOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 20f,
            animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
            label = ""
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(25.dp),
            contentAlignment = Alignment.Center
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                repeat(6) { i ->
                    Box(
                        modifier = Modifier
                            .offset(x = (50 * i).dp, y = (bubbleOffset * (i + 1)).dp)
                            .size((12 + i * 4).dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.25f))
                    )
                }
            }

            Box(
                modifier = Modifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFBEEAFF))
                    .padding(25.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.logo_ocean),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .background(Color(0xFF4CAF50), RoundedCornerShape(50))
                            .padding(horizontal = 30.dp, vertical = 10.dp)
                    ) {
                        Text("¡Registro Exitoso!", color = Color.White, fontSize = 18.sp)
                    }

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "¡Bienvenido, Nuevo Explorador!",
                        color = Color(0xFF003B73),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(20.dp))

                    Image(
                        painter = painterResource(id = R.drawable.pescado),
                        contentDescription = null,
                        modifier = Modifier.size(130.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "¡Tu cuenta ha sido creada con éxito!",
                        color = Color(0xFF003B73),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(25.dp))

                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(55.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6CBBD7))
                    ) {
                        Text("¡Comenzar Aventura!", color = Color.White)
                    }
                }
            }
        }
    }
}

fun isValidEmailR(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    return Regex(emailPattern).matches(email)
}

fun isValidName(name: String): Boolean {
    val namePattern = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$"
    return Regex(namePattern).matches(name)
}
