package com.proyect.ocean_words.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.proyect.ocean_words.R
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.rutas.Rutas

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    musicManager: MusicManager,
    isMusicGloballyEnabled: Boolean,
    isAppInForeground: Boolean,
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showEmailLogin by remember { mutableStateOf(false) }

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate(Rutas.CAMINO_NIVELES) {
                popUpTo(Rutas.LOGIN) { inclusive = true }
            }
        }
    }


    LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
        if (isMusicGloballyEnabled && isAppInForeground) {
            musicManager.playLevelMusic()
        } else {
            musicManager.stopAllMusic()
        }
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Iniciar Sesión",
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
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña") },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        passwordVisible = !passwordVisible
                    }) {
                        Icon(
                            painterResource(
                                if (passwordVisible)
                                    R.drawable.ic_visibility
                                else
                                    R.drawable.ic_visibility_off
                            ),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    authViewModel.loginUser(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                ,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6CBBD7)
                )
            ) {
                Text("INICIAR SESIÓN", color = Color.White)
            }

            Spacer(Modifier.height(12.dp))
            TextButton(onClick = {
                musicManager.playClickSound()
                navController.navigate(Rutas.REGISTRO) }) {
                Text("¿No tienes una cuenta? Crear cuenta", color = Color.Black)
            }
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = {
                musicManager.playClickSound()
                navController.navigate(Rutas.PROCESOACCESO)
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver")
            }

        }
        }

}