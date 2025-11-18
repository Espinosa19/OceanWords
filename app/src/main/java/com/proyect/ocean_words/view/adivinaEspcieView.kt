package com.proyect.ocean_words.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue // Esta es la importación clave que faltaimport androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import coil.compose.AsyncImage

import com.proyect.ocean_words.R
import com.proyect.ocean_words.viewmodels.AdivinaEspecieViewModelFactory
import com.proyect.ocean_words.model.SlotEstado

import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.theme.LightBlue
import com.proyect.ocean_words.view.theme.OceanBackground
import com.proyect.ocean_words.view.theme.Orange
//import com.proyect.ocean_words.view.theme.OrangeDeep
import com.proyect.ocean_words.view.screens.HeaderSection
import com.proyect.ocean_words.view.screens.NavegacionDrawerMenu
import com.proyect.ocean_words.view.theme.Boogaloo
import com.proyect.ocean_words.view.theme.BricolageGrotesque
import com.proyect.ocean_words.view.theme.Delius
import com.proyect.ocean_words.view.theme.MomoTrustDisplay
import com.proyect.ocean_words.view.theme.VerdeClaro
import com.proyect.ocean_words.viewmodels.EspecieViewModel
import com.proyect.ocean_words.viewmodels.NivelViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun OceanWordsGameUI(
    navController: NavController,
    levelId: Int = 1,
    musicManager: MusicManager,
    isAppInForeground: Boolean,
    animal: String = "ballena",
    dificultad: String = "normal",
    animalQuestion: String = "¿QUÉ ANIMAL ES ESTE?",
    onMusicToggle: (Boolean) -> Unit,
    isMusicEnabled: Boolean,
    especieId: String,
    nivelViewModel: NivelViewModel,
    imagen: String?
) {
    val vidas by nivelViewModel.vidas.collectAsState()
    val timeToNextLife by nivelViewModel.timeToNextLife.collectAsState()

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
            HeaderSection(animal,dificultad,navController,vidas = vidas, timeToNextLife = timeToNextLife, nivelViewModel = nivelViewModel)

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Aquí se llama al componente principal del juego con toda la lógica de estado
            JuegoAnimal(animal, dificultad, animalQuestion, navController, musicManager, onMusicToggle, isMusicEnabled,especieId, nivelViewModel,imagen)
        }


    }
}

@Composable

fun JuegoAnimal(
    animal: String,
    dificultad: String,
    animalQuestion: String,
    navController: NavController,
    musicManager: MusicManager,
    onMusicToggle: (Boolean) -> Unit,
    isMusicEnabled: Boolean,
    especieId: String,
    nivelViewModel: NivelViewModel,
    imagen: String?
) {
    val viewModel: EspecieViewModel = viewModel(
        factory = AdivinaEspecieViewModelFactory(animal, dificultad, nivelViewModel)
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
    val obtenerPista : () -> Unit = viewModel::obtenerPista
    LaunchedEffect (navegarAExito) {
        if (navegarAExito) {

            val imagen = URLEncoder.encode(imagen, StandardCharsets.UTF_8.toString()) // 👈 Codificar                            // Navegar pasando los parámetros

            // 2. Consumir el evento de navegación reseteando la LiveData
            navController.navigate("caracteristicas/$especieId/$imagen")
        }
    }

    // 4. LAYOUT
    Box(modifier = Modifier.fillMaxSize()) {
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val bottomPadding = if (screenWidthDp > 420.dp) { 180.dp } else { 150.dp }

        QuestionAndImageSection( navController,animalQuestion, animal, respuestaJugador, onLetterRemoved, musicManager, onMusicToggle, isMusicEnabled,imagen)


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
                .background(Color(0xFFE98516))
                .padding(bottom = 38.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            accionesEspecíficas(onResetGame,onGoBackGame,obtenerPista)
        }
    }
}
@Composable
fun QuestionAndImageSection(
    navController: NavController,
    question: String,
    animal: String,
    // 💡 NUEVOS ARGUMENTOS
    respuestaJugador: MutableList<SlotEstado?>,
    onLetterRemoved: (Int) -> Unit,
    musicManager: MusicManager,
    onMusicToggle: (Boolean) -> Unit,
    isMusicEnabled: Boolean,
    imagen: String?
) {
    var statusMenu by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val horizontalPadding = if (screenWidthDp > 420.dp) { 25.dp } else { 60.dp }
    val offsetPadding = if (screenWidthDp < 720.dp) { (-10).dp } else { (-20).dp }

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
            Text(text = question, fontSize = 24.sp, fontFamily = Boogaloo, fontWeight = FontWeight.Black, color = Color.Black, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
        Box(modifier = Modifier.fillMaxWidth()){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    // 👈 Reemplaza 'painterResource' con la URL
                    model = imagen,
                    contentDescription = "", // O Ballena, según la imagen

                    // **NOTA:** Mantén los modificadores y el filtro de color
                    modifier = Modifier
                        .size(200.dp)
                        .offset(y = offsetPadding),
                    colorFilter = ColorFilter.tint(
                        color = Color.Black.copy(alpha = 1f),
                        blendMode = BlendMode.SrcAtop
                    )
                )
                // 💡 LLAMADA A RESPONSEAREA CON EL ESTADO Y CALLBACK
                ResponseArea(animal, respuestaJugador, onLetterRemoved)
            }
            IconButton(
                onClick = { statusMenu = !statusMenu },

                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 25.dp, end = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tesoro),
                    contentDescription = "men",
                    modifier = Modifier
                        .size(54.dp), // Tamaño fijo pero razonable para el icono/botón
                    contentScale = ContentScale.Fit
                )
            }
            if (statusMenu) {
                NavegacionDrawerMenu(
                    navController = navController,
                    onCloseMenu = { statusMenu = false },
                    musicManager = musicManager,
                    onMusicToggle = onMusicToggle,
                    isMusicEnabled = isMusicEnabled
                )
            }
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
    val topPadding = if (screenWidthDp > 420.dp) 128.dp else 158.dp
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
                            true -> VerdeClaro.copy(alpha = 0.8f)
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
                                fontFamily = Delius,
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
                                fontFamily = MomoTrustDisplay,
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
fun accionesEspecíficas(onResetGame: () -> Unit, onGoBackGame: () -> Unit, obtenerPista: () -> Unit) {
    val dividerColor = Color(0xFFE98516)
    val dividerWidth = 2.dp
    val imageSize = 40.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color(0xFFE98516)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onResetGame)
                .clip(RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.cancelar),
                contentDescription = "Reiniciar",
                modifier = Modifier.size(imageSize)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Reiniciar",
                fontFamily = BricolageGrotesque,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        // Divisor
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(dividerWidth)
                .background(dividerColor)
        )

        // --- 2. Botón de Retroceder (Deshacer) ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onGoBackGame) // <--- Funcionalidad click
                .clip(RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                // Reemplaza 'R.drawable.deshacer_icon' con el ID de tu imagen
                painter = painterResource(id = R.drawable.recargar),
                contentDescription = "Deshacer",
                modifier = Modifier.size(imageSize)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Deshacer",
                fontFamily = BricolageGrotesque,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        // Divisor
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(dividerWidth)
                .background(dividerColor)
        )

        // --- 3. Botón de Pista ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = obtenerPista)
                .clip(RoundedCornerShape(8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.idea),
                    contentDescription = "Pista",
                    modifier = Modifier.size(imageSize)
                )

                // Este es el Badge con el número '1'
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                        .size(20.dp)
                        .background(Color.Blue, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pista",
                fontFamily = BricolageGrotesque,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
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
fun OceanWordsGameRoute(
    navController: NavController,
    levelId: Int,
    isAppInForeground: Boolean,
    musicManager: MusicManager,
    isMusicGloballyEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,

    nombre: String,
    dificultad: String,
    especieId: String,
    nivelViewModel: NivelViewModel = viewModel(),
    imagen: String?
) {

    LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
        if (isMusicGloballyEnabled && isAppInForeground) {
            musicManager.playLevelMusic() // Toca la música de nivel
        } else {
            musicManager.stopAllMusic()
        }
    }
    val defaultQuestion = "¿QUÉ ANIMAL ES ESTE?"

    OceanWordsGameUI(
        navController = navController,
        levelId = levelId,
        musicManager = musicManager,
        isAppInForeground = isAppInForeground, // ✅ Pasamos el valor que recibimos

        animal = nombre,
        dificultad = dificultad,
        imagen=imagen,
        animalQuestion = defaultQuestion,

        onMusicToggle = onMusicToggle,

        isMusicEnabled = isMusicGloballyEnabled,
        especieId = especieId,
        nivelViewModel = nivelViewModel
    )
}

