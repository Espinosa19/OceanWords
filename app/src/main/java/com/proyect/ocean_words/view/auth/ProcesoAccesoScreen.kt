package com.proyect.ocean_words.view.auth

import com.proyect.ocean_words.viewmodels.AuthViewModel


import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.proyect.ocean_words.R
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.rutas.Rutas
import java.util.regex.Pattern
@Composable
fun ProcesoAccesoScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    musicManager: MusicManager,
    isMusicGloballyEnabled: Boolean,
    isAppInForeground: Boolean,
    googleSignInClient: GoogleSignInClient,
) {
    val context = LocalContext.current
    val activity = context as Activity

    val googleLauncher =
        rememberLauncherForActivityResult (
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account.idToken

                    if (idToken != null) {
                        authViewModel.loginWithGoogle(idToken)
                    }
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "Error Google: ${e.statusCode}")
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

    var showEmailLogin by remember { mutableStateOf(false) }

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate(Rutas.CAMINO_NIVELES) {
                popUpTo(Rutas.PROCESOACCESO) { inclusive = true }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A2342), Color(0xFF065A82))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.imagen_login),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .offset(0.dp,(-25).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ){



            /** 🔹 BOTONES INICIALES **/
            if (!showEmailLogin) {

                /** 🔴 BOTÓN GOOGLE **/
                OutlinedButton(
                    onClick = {
                        musicManager.playClickSound()
                        val signInIntent = googleSignInClient.signInIntent
                        googleLauncher.launch(signInIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(Color.White, RoundedCornerShape(22.dp))
                    ,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Continuar con Google",
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(12.dp))

                /** 🔵 LOGIN CON CORREO **/
                Button(
                    onClick = {
                        musicManager.playClickSound()
                        showEmailLogin = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6CBBD7)
                    )
                ) {
                    Text("INICIAR SESIÓN CON CORREO", color = Color.White)
                }


            }

            /** 🔹 LOGIN POR CORREO **/
            if (showEmailLogin) {
                navController.navigate(Rutas.LOGIN)
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
