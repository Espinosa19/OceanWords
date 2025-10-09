package com.proyect.ocean_words.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Composable
fun BotonesDesaparecenSecuencialmente() {
    val n = 6
    val visible = remember { List(n) { true }.toMutableStateList() }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Botones visibles
        visible.forEachIndexed { index, isVisible ->
            AnimatedVisibility(
                visible = isVisible,
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Button(
                    onClick = { visible[index] = false }, // desaparece solo el botón seleccionado
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text("Botón ${index + 1}")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón que inicia la desaparición secuencial
        Button(onClick = {
            scope.launch {
                visible.forEachIndexed { i, _ ->
                    visible[i] = false
                    delay(400)
                }
            }
        }) {
            Text("Desaparecer en secuencia")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nuevo botón que hace reaparecer todos los botones
        Button(onClick = {
            visible.forEachIndexed { i, _ ->
                visible[i] = true
            }
        }) {
            Text("Volver a aparecer")
        }
    }
}

