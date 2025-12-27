package com.proyect.ocean_words.view
import RealisticStylizedChest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.proyect.ocean_words.model.CustomDialogConfig
import com.proyect.ocean_words.viewmodels.UserSession
import com.proyect.ocean_words.viewmodels.AdivinaEspecieViewModelFactory
import com.proyect.ocean_words.model.SlotEstado
import com.proyect.ocean_words.model.UsuariosEstado

import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.screens.CustomDialog
import com.proyect.ocean_words.view.screens.FishShape
import com.proyect.ocean_words.view.theme.LightBlue
import com.proyect.ocean_words.view.theme.OceanBackground
import com.proyect.ocean_words.view.theme.Orange
import com.proyect.ocean_words.view.screens.HeaderSection
import com.proyect.ocean_words.view.screens.NavegacionDrawerMenu
import com.proyect.ocean_words.view.theme.Boogaloo
import com.proyect.ocean_words.view.theme.BricolageGrotesque
import com.proyect.ocean_words.view.theme.Delius
import com.proyect.ocean_words.view.theme.MomoTrustDisplay
import com.proyect.ocean_words.view.theme.VerdeClaro
import com.proyect.ocean_words.viewmodels.EspecieViewModel
import com.proyect.ocean_words.viewmodels.NivelViewModel
import com.proyect.ocean_words.viewmodels.ProgresoViewModel
import com.proyect.ocean_words.viewmodels.UsuariosViewModel
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
    imagen: String?,
    progresoViewModel: ProgresoViewModel,
    usuarioViwModel: UsuariosViewModel,
    tipo_especie: String?,
) {
    val viewModel: EspecieViewModel = viewModel(
        factory = AdivinaEspecieViewModelFactory(levelId,especieId,animal, dificultad,usuarioViwModel)
    )

    val usuarioDatos: UsuariosEstado? = UserSession.currentUser
    val userId : String= (usuarioDatos?.id).toString()
    val estadoNivel by viewModel.estadoNivel.collectAsState()
    var mostrarDialog = remember { mutableStateOf(false) }

    // Cada vez que cambie estadoNivel, revisamos si está completado
    LaunchedEffect(Unit) {
        viewModel.calcularEstadoNivel()
    }

    LaunchedEffect(estadoNivel) {
        mostrarDialog.value = estadoNivel == "completado"
    }

    var mostrarDial by rememberSaveable { mutableStateOf(false) }

    // Mostrar diálogo
    if (mostrarDialog.value) {
        CustomDialog(
            showDialog = mostrarDialog.value,
            config = CustomDialogConfig(
                title = "¡FELICIDADES!",
                imageRes = R.drawable.trofeo,
                headline = "Nivel completado",
                message = "Excelente trabajo",
                leftButtonText = "Continuar"
            ),
            onDismiss = { mostrarDialog.value = false },
            onLeftClick = {
                mostrarDialog.value = false
                viewModel.volverJugar()
            }
        )
    }




    LaunchedEffect(Unit) {
        usuarioViwModel.checkAndRegenerateLife(userId)
    }

    LaunchedEffect(userId) {
        usuarioViwModel.observarMonedasVidasUsuario(userId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanBackground)
    ) {
        val imageResource = if (tipo_especie.equals("NORMAL")) {
            R.drawable.fondo_juego // Asumo que 'vidas' es el corazón lleno
        } else {
            R.drawable.fondo_mitico // <--- ¡DEBES USAR UN RECURSO PARA EL CORAZÓN VACÍO AQUÍ!
        }

        Image(
            painter = painterResource(id = imageResource),
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
            HeaderSection(
                navController,
                nivelViewModel = nivelViewModel,
                usuariosViewModel = usuarioViwModel,
                mostrarDialog=mostrarDialog
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Aquí se llama al componente principal del juego con toda la lógica de estado
            JuegoAnimal(
                animal,
                dificultad,
                animalQuestion,
                navController,
                musicManager,
                onMusicToggle,
                isMusicEnabled,
                especieId,
                nivelViewModel,
                imagen,
                progresoViewModel,
                levelId,
                viewModel,
                usuarioViwModel,
                tipo_especie,
                onMostrarDialChange = { mostrarDial = it },
                mostrarDial = mostrarDial
            )
        }

        if (mostrarDial) {
            Log.i("MostrarDia","Holsa")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f)), // 🖤 fondo oscuro translúcido
                contentAlignment = Alignment.Center
            ) {
                RealisticStylizedChest()
            }
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
    imagen: String?,
    progresoViewModel: ProgresoViewModel,
    levelId: Int,
    viewModel: EspecieViewModel,
    usuarioViwModel: UsuariosViewModel,
    tipo_especie: String?,
    mostrarDial: Boolean,
    onMostrarDialChange: (Boolean) -> Unit
) {


    val animalRandom = viewModel.animalRandom
    val letrasPorFila = 7 // Asumiendo que esta es una constante de la UI
    val vidas by usuarioViwModel.vidas.collectAsState()
    val allLivesLost = vidas.all { !it }
    val isGameEnabled = !allLivesLost
    val visible = viewModel.visible
    val respuestaJugador = viewModel.respuestaJugador
    val navegarAExito by viewModel.navegarAExito.observeAsState(initial = false)
    val onLetterSelected: (Char, Int) -> Unit = viewModel::selectLetter
    val onLetterRemoved: (Int) -> Unit = viewModel::removeLetter
    val onResetGame: () -> Unit = viewModel::resetGame
    val onGoBackGame: () -> Unit = viewModel::goBackGame
    val usuario: UsuariosEstado? = UserSession.currentUser
    val obtenerPista : () -> Unit = viewModel::obtenerPista

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val pistaUsada by viewModel.pistaUsada.collectAsState()
    LaunchedEffect(navegarAExito) {
        if (navegarAExito) {

            val imagenEncoded = imagen?.let {
                URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
            } ?: ""

            if (tipo_especie == "MITICA") {
                onMostrarDialChange(true) // ✅ AHORA SÍ
            } else {
                navController.navigate(
                    "caracteristicas/$especieId/$imagenEncoded/$levelId"
                )
            }
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
            tecladoInteractivo(animalRandom, letrasPorFila, enabled = isGameEnabled, musicManager = musicManager,viewModel)
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
            accionesEspecíficas(onResetGame,onGoBackGame,obtenerPista, enabled = isGameEnabled, pistaUsada = pistaUsada, musicManager = musicManager,usuarioViwModel,navController)
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
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
                ResponseArea(animal, respuestaJugador, onLetterRemoved, musicManager)
            }
            IconButton(
                onClick = {
                    musicManager.playClickSound()
                    statusMenu = !statusMenu },

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
                    onCloseMenu = {
                        musicManager.playClickSound()
                        statusMenu = false },
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
    onSlotClicked: (Int) -> Unit,
    musicManager: MusicManager
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
                            else -> LightBlue.copy(alpha = 0.8f)
                        }

                        Button(
                            onClick = {
                                if (responseChar != null) {
                                    musicManager.playClickSound()
                                    onSlotClicked(indexForCallback)
                                }
                            },
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
    letrasPorFila: Int,
    enabled: Boolean,
    musicManager: MusicManager,
    viewModel: EspecieViewModel
) {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    val usoLetras by viewModel.usoLetras.collectAsState()
    val respuestaJugador = viewModel.respuestaJugador

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
                    val letra = animalRandom[j]

                    // 🔑 Visibilidad DERIVADA
                    val botonUsado = respuestaJugador.any { it?.botonIndex == j }
                    val usosActuales = usoLetras[letra] ?: 0
                    val maxUsos = animalRandom.count { it == letra }

                    AnimatedVisibility(
                        visible = !botonUsado && usosActuales < maxUsos,
                        exit = fadeOut(animationSpec = tween(300))
                    ) {

                        Button(
                            onClick = {
                                if (!enabled) return@Button

                                musicManager.playClickSound()


                                if (usosActuales < maxUsos) {
                                    viewModel.selectLetter(letra, j)
                                }
                            },
                            modifier = Modifier.size(38.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                        ) {
                            Text(
                                text = letra.toString(),
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
fun accionesEspecíficas(
    onResetGame: () -> Unit,
    onGoBackGame: () -> Unit,
    obtenerPista: () -> Unit,
    enabled: Boolean,
    pistaUsada: Boolean,
    musicManager: MusicManager,
    usuarioViwModel: UsuariosViewModel,
    navController: NavController
) {
    val dividerColor = Color(0xFFE98516)
    val dividerWidth = 2.dp
    val imageSize = 40.dp
    val usoPista = usuarioViwModel.pistasUsuario.collectAsState()
    val usoPistaString = usoPista.value.toString()
    val pistasUsadas = usoPistaString.toIntOrNull() ?: 0


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
                .clickable(onClick = {
                    musicManager.playClickSound()
                    onResetGame()
                })
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
                .clickable(onClick = {
                    musicManager.playClickSound()
                    onGoBackGame()
                })
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
                .clickable(onClick = {
                    musicManager.playClickSound()
                    if (pistasUsadas > 0) {
                        obtenerPista()
                    }else{
                        navController.navigate("game_shop") // Usa una ruta clara, por ejemplo, "game_shop"
                    }

                })
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
                if (usoPistaString.equals("0")) {
                    Log.i("Decision","$usoPistaString")
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (30).dp, y = 22.dp) // mejor control
                            .size(width = 200.dp, height = 40.dp)
                            .background(Color.Blue, FishShape())
                            .border(3.dp, Color(0xFFFFD700), FishShape())
                            .shadow(16.dp, FishShape()),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.dolar),
                                contentDescription = "Monedas",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(Modifier.width(3.dp))
                            Text(
                                text = "50",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }else{
                    Log.i("Decision","Hola")
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .size(20.dp)
                            .background(Color.Blue, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = usoPistaString,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // Este es el Badge con el número '1'

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
    imagen: String?,
    progresoViewModel: ProgresoViewModel,
    usuarioViwModel: UsuariosViewModel,
    tipo_especie: String?,
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
        nivelViewModel = nivelViewModel,
        progresoViewModel =progresoViewModel,
        usuarioViwModel= usuarioViwModel,
        tipo_especie = tipo_especie
    )
}

