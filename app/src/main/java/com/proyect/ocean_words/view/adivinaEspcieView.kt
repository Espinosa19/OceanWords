package com.proyect.ocean_words.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue // Esta es la importación clave que faltaimport androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.AdivinaEspecieViewModelFactory
import com.proyect.ocean_words.model.SlotEstado
import com.proyect.ocean_words.ui.theme.LightBlue
import com.proyect.ocean_words.ui.theme.OceanBackground
import com.proyect.ocean_words.ui.theme.Orange
import com.proyect.ocean_words.ui.theme.OrangeDeep
import com.proyect.ocean_words.view.screens.HeaderSection
import com.proyect.ocean_words.viewmodels.AdivinaEspecieViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


@Composable
fun OceanWordsGameUI(
    navController: NavController,
    score: Int = 1250,
    animal: String ="pez lampara",
    dificultad:String="normal",
    animalQuestion: String = "¿QUÉ ANIMAL ES ESTE?",
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanBackground)
    ) {
        // Fondo de imagen
        Image(
            painter = painterResource(id = R.drawable.fondo_juego),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 1. Encabezado (Score, Time)
            HeaderSection(score,navController)

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Aquí se llama al componente principal del juego con toda la lógica de estado
            JuegoAnimal(animal, dificultad, animalQuestion,navController)
        }

    }
}

@Composable
fun JuegoAnimal(animal: String, dificultad: String, animalQuestion: String,navController:NavController) {
        val viewModel: AdivinaEspecieViewModel = viewModel(
            factory = AdivinaEspecieViewModelFactory(animal, dificultad)
        )

        val animalRandom = viewModel.animalRandom
        val letrasPorFila = 7 // Asumiendo que esta es una constante de la UI

        val visible = viewModel.visible
        val respuestaJugador = viewModel.respuestaJugador
        val navegarAExito by viewModel.navegarAExito.observeAsState(initial = false)
        val onLetterSelected: (Char, Int) -> Unit = viewModel::selectLetter
        val onLetterRemoved: (Int) -> Unit = viewModel::removeLetter
        val onResetGame: () -> Unit = viewModel::resetGame
        val onGoBackGame: () -> Unit = viewModel::goBackGame
        LaunchedEffect (navegarAExito) {
        if (navegarAExito) {
            // 1. Navegar a la pantalla de éxito
            navController.navigate("caracteristicas") {
                popUpTo("ruta_del_juego") { inclusive = true } // O la lógica de back stack que necesites
            }

            // 2. Consumir el evento de navegación reseteando la LiveData
            viewModel.navegacionAExitoCompleta() // Debes implementar esta función en el VM
        }
    }

    // 4. LAYOUT
    Box(modifier = Modifier.fillMaxSize()) {
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val bottomPadding = if (screenWidthDp > 420.dp) { 180.dp } else { 150.dp }

        QuestionAndImageSection(animalQuestion, animal, respuestaJugador, onLetterRemoved)


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomPadding, start = 10.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            tecladoInteractivo(animalRandom, visible, letrasPorFila, onLetterSelected)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .align(Alignment.BottomCenter)
                .background(OrangeDeep)
                .padding(bottom = 38.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            accionesEspecíficas(onResetGame,onGoBackGame)
        }
    }
}
@Composable
fun QuestionAndImageSection(
    question: String,
    animal: String,
    // 💡 NUEVOS ARGUMENTOS
    respuestaJugador: MutableList<SlotEstado?>,
    onLetterRemoved: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val horizontalPadding = if (screenWidthDp > 420.dp) { 25.dp } else { 60.dp }
    val offsetPadding = if (screenWidthDp > 420.dp) { (-10).dp } else { (-20).dp }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(20.dp, 10.dp, 20.dp, 10.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Box de la Pregunta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(LightBlue)
                .padding(vertical = 12.dp, horizontal = horizontalPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = question, fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.Black, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }

        // Box de la Imagen y la Respuesta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.ballena),
                contentDescription = "Pez Payaso",
                modifier = Modifier
                    .size(200.dp)
                    .offset(y = offsetPadding),
            )
            // 💡 LLAMADA A RESPONSEAREA CON EL ESTADO Y CALLBACK
            ResponseArea(animal, respuestaJugador, onLetterRemoved)
        }
    }
}@Composable
fun ResponseArea(
    animal: String,
    respuestaJugador: MutableList<SlotEstado?>,
    onSlotClicked: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val topPadding = if (screenWidthDp > 420.dp) 125.dp else 148.dp
    val sizeCard = if (screenWidthDp < 720.dp) 32.dp else 38.dp

    val letrasPorFila = 8
    val palabras = animal.split(" ")

    // 👉 Une las palabras de forma que no rompan la palabra en filas distintas
    val filas = mutableListOf<String>()
    var filaActual = ""

    for (palabra in palabras) {
        if ((filaActual.length + palabra.length + 1) <= letrasPorFila) {
            // Cabe en la fila actual
            if (filaActual.isNotEmpty()) filaActual += " "
            filaActual += palabra
        } else {
            // No cabe, crear nueva fila
            filas.add(filaActual)
            filaActual = palabra
        }
    }
    if (filaActual.isNotEmpty()) filas.add(filaActual)

    var currentIndex = 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = topPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        filas.forEach { fila ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (caracter in fila) {
                    if (caracter == ' ') {
                        Box(
                            modifier = Modifier
                                .width(15.dp)
                                .height(38.dp)
                        )
                    } else {
                        val slotEstado = respuestaJugador.getOrNull(currentIndex)
                        val responseChar = slotEstado?.char
                        val indexForCallback = currentIndex

                        val colorDeFondo = when (slotEstado?.esCorrecto) {
                            true -> Color.Green.copy(alpha = 0.8f)
                            false -> Color.Red.copy(alpha = 0.8f)
                            else -> LightBlue.copy(alpha = 0.8f)
                        }

                        Button(
                            onClick = { if (responseChar != null) onSlotClicked(indexForCallback) },
                            modifier = Modifier
                                .size(sizeCard)
                                .clip(RoundedCornerShape(15.dp)),
                            shape = RoundedCornerShape(15.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorDeFondo
                            )
                        ) {
                            Text(
                                text = responseChar?.toString() ?: "",
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                        currentIndex++
                    }
                }
            }
        }
    }
}


@Composable
fun tecladoInteractivo(
    animalRandom: String,
    visible: MutableList<Boolean>,
    letrasPorFila: Int,
    onLetterSelected: (Char, Int) -> Unit // 💡 NUEVO ARGUMENTO
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val topPadding = if (screenWidthDp > 420.dp) { 125.dp } else { 148.dp }
    val tamano = animalRandom.length
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0 until tamano step letrasPorFila) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fin = (i + letrasPorFila).coerceAtMost(tamano)
                for (j in i until fin) {
                    AnimatedVisibility(
                        visible = visible[j],
                        exit = fadeOut(animationSpec = tween(500))
                    ) {
                        Button(
                            onClick = { onLetterSelected(animalRandom[j], j) }, // 💡 Usa el callback
                            modifier = Modifier.size(38.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                        ) {
                            Text(
                                text = animalRandom[j].toString(),
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun accionesEspecíficas(onResetGame: () -> Unit,onGoBackGame: () -> Unit) { // 💡 FIRMA CORREGIDA
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BotonDeInterfaz(icon = Icons.Filled.Close, onClick = onResetGame)

        BotonDeInterfaz(
            icon = Icons.Filled.Refresh,
            onClick = onGoBackGame
        )

        Button(
            onClick = {},
            modifier = Modifier.size(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pista),
                contentDescription = "Pista",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun BotonDeInterfaz(
    icon: ImageVector,
    colorFondo: Color = Orange,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorFondo),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
    }
}
@Composable
fun TemporizadorRegresivo(
    tiempoInicialSegundos: Long = 600 // 10 minutos = 600 segundos
): String {
    var tiempoRestante by remember { mutableStateOf(tiempoInicialSegundos) }

    LaunchedEffect(Unit) {
        while (tiempoRestante > 0) {
            delay(1000L)
            tiempoRestante -= 1
        }
    }

    // Convertimos segundos a hh:mm:ss
    val minutos = TimeUnit.SECONDS.toMinutes(tiempoRestante) % 60
    val segundos = tiempoRestante % 60

    return String.format("%02d:%02d", minutos, segundos)
}


