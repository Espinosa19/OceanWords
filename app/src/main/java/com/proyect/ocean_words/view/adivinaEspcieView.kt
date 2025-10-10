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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// NOTA: Reemplaza estos imports con tus clases de verdad
import com.proyect.ocean_words.R
import com.proyect.ocean_words.ui.theme.LightBlue
import com.proyect.ocean_words.ui.theme.OceanBackground
import com.proyect.ocean_words.ui.theme.Orange
import com.proyect.ocean_words.ui.theme.OrangeDeep
import com.proyect.ocean_words.view.screens.HeaderSection


@Composable
fun OceanWordsGameUI(
    navController: NavController,
    score: Int = 1250,
    time: String = "0:45",
    animal: String ="pez lampara",
    dificultad:String="dificil",
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
            HeaderSection(score, time,navController)

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Aquí se llama al componente principal del juego con toda la lógica de estado
            JuegoAnimal(animal, dificultad, animalQuestion)
        }

    }
}

// =========================================================================================
//                             COMPONENTE PRINCIPAL DEL JUEGO
// =========================================================================================

@Composable
fun JuegoAnimal(animal: String, dificultad: String, animalQuestion: String) {
    // 1. LÓGICA DE PREPARACIÓN DE LETRAS
    val animalSinEspacios = animal.trim().replace(" ", "")
    val letrasPorFila = 7
    val animalRandom = if (dificultad != "dificil") {
        shuffleText(animalSinEspacios)
    } else {
        val letrasRandom = getTwoRandomLetters()
        shuffleText(animalSinEspacios + letrasRandom.joinToString(""))
    }
    val tamano = animalRandom.length
    // 2. ESTADOS
    val visible = remember { List(tamano) { true }.toMutableStateList() } // Visibilidad de botones
    val respuestaJugador = remember {
        mutableStateListOf<Char?>().apply {
            animalSinEspacios.forEach { char -> add(if (char == ' ') null else null) }
        }
    }
    val botonADondeFue = remember { // Mapeo de botón (teclado) a slot (respuesta)
        mutableStateListOf<Int>().apply { animalRandom.forEach { _ -> add(-1) } }
    }

    // 3. CALLBACKS (Eventos)
    val onLetterSelected: (Char, Int) -> Unit = { char, originalIndex ->
        var posicionAsignada = -1 // Índice del slot de respuesta que se llenó

        animalSinEspacios.forEachIndexed { correctIndex, correctChar ->


            if (correctChar.equals(char, ignoreCase = true)) {

                if (respuestaJugador[correctIndex] == null) {

                    if (posicionAsignada == -1) {
                        respuestaJugador[correctIndex] = correctChar // Asigna la letra
                        posicionAsignada = correctIndex

                        return@forEachIndexed // Salir del forEach
                    }
                }
            }
        }
        if (posicionAsignada != -1) {
            // Si es la PRIMERA VEZ que se usa (visible[originalIndex] es true), la ocultamos.
            if (visible[originalIndex]) {
                visible[originalIndex] = false // Oculta el botón después del primer uso exitoso
            }
            // Mapeamos el botón de teclado (originalIndex) al slot de respuesta (posicionAsignada)
            botonADondeFue[originalIndex] = posicionAsignada
        }


    }

    val onLetterRemoved: (Int) -> Unit = { responseSlotIndex ->
        respuestaJugador[responseSlotIndex] = null
        val originalButtonIndex = botonADondeFue.indexOf(responseSlotIndex)
        if (originalButtonIndex != -1) {
            botonADondeFue[originalButtonIndex] = -1
            visible[originalButtonIndex] = true
        }
    }

    val onResetGame: () -> Unit = {
        visible.forEachIndexed { i, _ -> visible[i] = true }
        botonADondeFue.fill(-1)
        for (i in respuestaJugador.indices) {
            respuestaJugador[i] = null
        }
    }

    // 4. LAYOUT
    Box(modifier = Modifier.fillMaxSize()) {
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        var screenHeightDp =configuration.screenHeightDp
        val bottomPadding = if (screenWidthDp > 420.dp) { 180.dp } else { 150.dp }
        QuestionAndImageSection(animalQuestion, animal, respuestaJugador, onLetterRemoved)


        Column(
            modifier = Modifier.fillMaxWidth()
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
                // 💡 CAMBIO: Quitamos el padding horizontal extra del padding(bottom = 38.dp)
                // y solo aplicamos el padding interior para centrar los botones.
                .background(OrangeDeep)
                .padding(bottom = 38.dp), // Aplicamos solo el padding vertical
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            accionesEspecíficas(onResetGame)
        }
    }
}

// =========================================================================================
//                             COMPONENTES MODIFICADOS
// =========================================================================================

@Composable
fun QuestionAndImageSection(
    question: String,
    animal: String,
    // 💡 NUEVOS ARGUMENTOS
    respuestaJugador: MutableList<Char?>,
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
            .padding(20.dp,10.dp,20.dp,10.dp) ,
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
                modifier = Modifier.size(200.dp).offset(y = offsetPadding),
            )
            // 💡 LLAMADA A RESPONSEAREA CON EL ESTADO Y CALLBACK
            ResponseArea(animal, respuestaJugador, onLetterRemoved)
        }
    }
}

@Composable
fun ResponseArea(
    animal: String,
    respuestaJugador: MutableList<Char?>,
    onSlotClicked: (Int) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val topPadding = if (screenWidthDp > 420.dp) { 125.dp } else { 148.dp }
    val sizeComp = if (screenWidthDp > 420.dp) { 28.dp } else { 35.dp }
    val tamano = animal.length
    val letrasPorFila = 8

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = topPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var currentIndex = 0
        for (i in 0 until tamano step letrasPorFila) {
            Row(
                modifier = if (tamano > 8 && i == 0) {
                    Modifier.padding(top = 24.dp)
                }else if (tamano > 16 && i == 0) {
                    Modifier.padding(top = 30.dp)
                } else {
                    Modifier
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fin = (i + letrasPorFila).coerceAtMost(tamano)
                for (j in i until fin) {
                    val caracter = animal[j]

                    if (caracter == ' ') {
                        Box(modifier = Modifier.width(15.dp).height(38.dp)) { } // Ajustado a 38.dp
                    } else {
                        print(caracter)
                        val responseChar = respuestaJugador[currentIndex]
                        val indexForCallback = currentIndex

                        Button(
                            onClick = { if (responseChar != null) onSlotClicked(indexForCallback) },
                            modifier = Modifier.size(sizeComp).clip(RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = LightBlue.copy(alpha = 0.8f)
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
                            shape = RoundedCornerShape(10.dp),
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
fun accionesEspecíficas(onResetGame: () -> Unit) { // 💡 FIRMA CORREGIDA
    Row(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(22.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BotonDeInterfaz(icon = Icons.Filled.ArrowBack, onClick = { })
        BotonDeInterfaz(icon = Icons.Filled.Close, onClick = { })

        BotonDeInterfaz(
            icon = Icons.Filled.Refresh,
            onClick = onResetGame // 💡 Usa el callback de reset completo
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

// =========================================================================================
//                             FUNCIONES DE UTILIDAD
// =========================================================================================

fun getTwoRandomLetters(): List<Char> {
    val abecedario =('a'..'z')
    return List(2) { abecedario.random() }
}

fun shuffleText(animal: String): String {
    return animal.toList().shuffled().joinToString("")
}

