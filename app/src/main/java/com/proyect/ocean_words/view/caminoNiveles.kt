package com.proyect.ocean_words.view

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.LevelEstado
import com.proyect.ocean_words.model.NivelEstado
import com.proyect.ocean_words.model.TipoEspecie
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.model.progreso_Niveles
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.screens.BottomNavBar
import com.proyect.ocean_words.view.screens.CentralIndicatorBox
import com.proyect.ocean_words.view.screens.GameIndicator
import com.proyect.ocean_words.view.screens.LifeRechargeBubble
import com.proyect.ocean_words.view.screens.configuracionView
import com.proyect.ocean_words.viewmodels.NivelViewModel
import com.proyect.ocean_words.viewmodels.ProgresoViewModel
import com.proyect.ocean_words.viewmodels.UserSession
import com.proyect.ocean_words.viewmodels.UsuariosViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


val LevelSpacing = 40.dp
val PaddingVertical = 50.dp
val TotalLevels = 5

@Composable
fun caminoNiveles(
    onStartTransitionAndNavigate: (levelId: Int) -> Unit,
    navController: NavHostController,
    musicManager: MusicManager,
    onMusicToggle: (Boolean) -> Unit,
    isMusicEnabled: Boolean,
    niveles: List<NivelEstado>,
    progreso: List<progreso_Niveles>,
    onItemClick: () -> Unit,
    usuarioViwModel: UsuariosViewModel,

    ) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    var showConfigDialog by remember { mutableStateOf(false) }
    val bubbleHeight = 35.dp
    val usuarioDatos: UsuariosEstado? = UserSession.currentUser
    val userId : String= (usuarioDatos?.id).toString()

    LaunchedEffect(userId) {
        usuarioViwModel.observarMonedasVidasUsuario(userId)
        usuarioViwModel.initLifeTimerIfNeeded(userId)  // 🔥 inicializa el contador al inicio

    }
    val monedas by usuarioViwModel.monedasUsuario.collectAsState(initial = null) // inicializamos como null
    val vidas by usuarioViwModel.vidas.collectAsState()
    val timeToNextLife by usuarioViwModel.timeToNextLife.collectAsState()

    val isRechargeNeeded = vidas.any { !it }
    val isTimerRunning = timeToNextLife.isNotEmpty()
    val infiniteTransition = rememberInfiniteTransition(label = "general_animations")

    val fish1XOffset by infiniteTransition.animateFloat(
        initialValue = 1500f,
        targetValue = -500f,
        animationSpec = infiniteRepeatable(animation = tween(20000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "fish1_offset_x"
    )
    val fish1YOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(animation = tween(4000, easing = EaseInOut), repeatMode = RepeatMode.Reverse),
        label = "fish1_offset_y"
    )
    val fish2XOffset by infiniteTransition.animateFloat(
        initialValue = 1500f, targetValue = -500f,
        animationSpec = infiniteRepeatable(animation = tween(14000, easing = LinearEasing), repeatMode = RepeatMode.Restart), label = "fish2_offset_x"
    )
    val fish2YOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 15f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = EaseInOut), repeatMode = RepeatMode.Reverse), label = "fish2_offset_y"
    )
    val fish3XOffset by infiniteTransition.animateFloat(
        initialValue = -500f, targetValue = 1500f,
        animationSpec = infiniteRepeatable(animation = tween(10000, easing = LinearEasing), repeatMode = RepeatMode.Restart), label = "fish3_offset_x"
    )
    val fish3YOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 10f,
        animationSpec = infiniteRepeatable(animation = tween(2500, easing = EaseInOut), repeatMode = RepeatMode.Reverse), label = "fish3_offset_y"
    )
    val fish4XOffset by infiniteTransition.animateFloat(
        initialValue = -500f, targetValue = 1500f,
        animationSpec = infiniteRepeatable(animation = tween(25000, easing = LinearEasing), repeatMode = RepeatMode.Restart), label = "fish5_offset_x"
    )
    val fish4YOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 30f,
        animationSpec = infiniteRepeatable(animation = tween(5000, easing = EaseInOut), repeatMode = RepeatMode.Reverse), label = "fish5_offset_y"
    )

    val levelNodeScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ), label = "level_node_scale"
    )

    val completadosSet: Set<Int> = progreso
        .filter { it.estado.equals("completado", ignoreCase = true) }
        .mapNotNull { it.nivel }
        .toSet()

    val nivelesFinal = niveles.mapIndexed { index, nivel ->
        val especieAleatoria = nivel.especies_id.randomOrNull()

        val nivelNumero = index + 1

        val isUnlocked = when (nivelNumero) {
            1 -> true
            else -> (completadosSet.contains(nivelNumero - 1))
        }

        LevelEstado(
            id = nivelNumero,
            especie_id = especieAleatoria?.id ?: "0",
            nombreEspecie = especieAleatoria?.nombre ?: "Desconocida",
            dificultad = especieAleatoria?.dificultad ?: "Sin definir",
            imagen = especieAleatoria?.imagen ?: "",
            isUnlocked = isUnlocked,
            tipo_especie = especieAleatoria?.tipo_especie?.name ?: "NORMAL"

        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondo_o),
            contentDescription = "Fondo submarino",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            state = listState,
            contentPadding = PaddingValues(
                top = PaddingVertical,
                bottom = PaddingVertical
            ),
            verticalArrangement = Arrangement.spacedBy(LevelSpacing),
            reverseLayout = true
        ) {
            itemsIndexed(nivelesFinal) { index, level ->
                val isCompleted = completadosSet.contains(level.id)

                LevelNode(
                    level = level,
                    index = index,
                    onLevelClick = { levelId ->
                        val lvl = nivelesFinal.getOrNull(levelId - 1)
                        if (lvl != null && lvl.isUnlocked) {
                            onStartTransitionAndNavigate(levelId)
                        } else {
                            Log.i("CaminoNiveles", "Nivel $levelId bloqueado")
                        }
                    },
                    animatedScale = levelNodeScale,
                    isCompleted = isCompleted
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.pez_azul),
            contentDescription = "Pez 1",
            modifier = Modifier
                .size(width = 90.dp, height = 45.dp)
                .offset(
                    x = with(density) { fish1XOffset.toDp() },
                    y = 600.dp + with(density) { fish1YOffset.toDp() }
                )
                .graphicsLayer {
                    scaleX = -1f
                },
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.pez_triangulo),
            contentDescription = "Pez 2",
            modifier = Modifier
                .size(width = 90.dp, height = 45.dp)
                .offset(
                    x = with(density) { fish4XOffset.toDp() },
                    y = 400.dp + with(density) { fish4YOffset.toDp() }
                ),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.pez_naranja),
            contentDescription = "Pez 3",
            modifier = Modifier
                .size(width = 80.dp, height = 40.dp)
                .offset(
                    x = with(density) { fish2XOffset.toDp() },
                    y = 250.dp + with(density) { fish2YOffset.toDp() }
                )
                .graphicsLayer {
                    scaleX = -1f
                },
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.pez_payaso),
            contentDescription = "Pez 4",
            modifier = Modifier
                .size(75.dp, 37.5.dp)
                .offset(
                    x = with(density) { fish3XOffset.toDp() },
                    y = 80.dp + with(density) { fish3YOffset.toDp() }
                ),
            contentScale = ContentScale.Fit
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .offset(y = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    showConfigDialog = true
                    onItemClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Ajustes",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Box(contentAlignment = Alignment.Center) {
                CentralIndicatorBox(vidas = vidas)
                if (isRechargeNeeded && isTimerRunning) {
                    LifeRechargeBubble(
                        timeRemaining = timeToNextLife,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = (-2).dp, y = (-bubbleHeight / 2) + 60.dp)
                    )
                }
            }

            GameIndicator(
                value = monedas,
                redireccionarClick = {
                    navController.navigate("game_shop")
                    onItemClick()
                },
                true,
            )
        }

        if (showConfigDialog) {
            Dialog(
                onDismissRequest = {
                    showConfigDialog = false
                    onItemClick()
                },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .offset(y = (-20).dp)
                ) {
                    configuracionView(
                        onBack = {
                            showConfigDialog = false
                            onItemClick()
                        },
                        musicManager = musicManager,
                        onMusicToggle = onMusicToggle,
                        isMusicEnabled = isMusicEnabled
                    )
                }
            }
        }
    }
}

@Composable
fun LevelNode(
    level: com.proyect.ocean_words.model.LevelEstado,
    index: Int,
    onLevelClick: (levelId: Int) -> Unit,
    animatedScale: Float,
    isCompleted: Boolean
) {
    val horizontalOffset = if (index % 2 == 0) (-50).dp else 50.dp

    val unlockedLevelImages = remember { listOf(
        R.drawable.iconos, R.drawable.icono2, R.drawable.icono1
    ) }
    val lockedLevelImages = remember { listOf(
        R.drawable.lock3, R.drawable.lock1, R.drawable.lock2
    ) }

    val imageResId = if (level.isUnlocked) {
        unlockedLevelImages.getOrElse(index % unlockedLevelImages.size) { unlockedLevelImages[0] }
    } else {
        lockedLevelImages.getOrElse(index % lockedLevelImages.size) { lockedLevelImages[0] }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.5f else 0f,
        animationSpec = tween(durationMillis = 100),
        label = "press_alpha_animation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = horizontalOffset),
        contentAlignment = Alignment.Center
    ) {
        if (level.isUnlocked) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        alpha = pressAlpha
                    }
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
        }

        Image(
            painter = painterResource(id = imageResId),
            contentDescription = "Nivel ${level.id}",
            modifier = Modifier
                .size(90.dp)
                .graphicsLayer {
                    scaleX = animatedScale
                    scaleY = animatedScale
                }
                .clip(CircleShape)
                .clickable(
                    enabled = level.isUnlocked,
                    onClick = { onLevelClick(level.id) },
                    indication = null,
                    interactionSource = interactionSource
                ),
            contentScale = ContentScale.Crop
        )

        if (isCompleted) {
            Text(
                text = "✅",
                fontSize = 28.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-14).dp)
            )
        }

        if (!level.isUnlocked) {
            Text(
                text = "🔒",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 5.dp)
            )
        }

        Text(
            text = "${level.id}",
            color = Color.Black,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun CaminoNivelesRoute(
    navController: NavHostController,
    musicManager: MusicManager,
    isAppInForeground: Boolean,
    viewModel: NivelViewModel,
    isMusicGloballyEnabled: Boolean,
    onMusicToggle: (Boolean) -> Unit,
    usuarioViwModel: UsuariosViewModel,

    ) {
    val isSplashShown by viewModel.isSplashShown.collectAsState()
    val niveles by viewModel.niveles.collectAsState(initial = emptyList())
    val totalNivelesCargados = niveles.size

    val showCompletionEvent by viewModel.showGameCompletionEvent.collectAsState()
    var showCompletionDialog by remember { mutableStateOf(false) }

    val progresoViewModel: ProgresoViewModel = viewModel()
    val usuarioState by progresoViewModel.usuarioLiveData.observeAsState()
    val progresoList: List<progreso_Niveles> = usuarioState?.progreso_niveles ?: emptyList()

    val isGameFinished = totalNivelesCargados > 0 && progresoList.any {
        it.nivel == totalNivelesCargados && it.estado.equals("completado", ignoreCase = true)
    }

    LaunchedEffect(showCompletionEvent) {
        if (showCompletionEvent) {
            showCompletionDialog = true
        }
    }


    val onStartTransitionAndNavigate: (levelId: Int) -> Unit = { levelId ->

        val nivel = niveles.find { it.numero_nivel == levelId }
        val especie = nivel?.especies_id?.firstOrNull()

        if (especie != null) {
            val especie_id = especie.id
            val nombre = URLEncoder.encode(especie.nombre, StandardCharsets.UTF_8.toString())
            val dificultad = especie.dificultad
            val imagen = URLEncoder.encode(especie.imagen, StandardCharsets.UTF_8.toString())
            musicManager.playClickSound()
            val tipoEspecie = especie.tipo_especie.name

            navController.navigate("nivel/$levelId/$especie_id/$nombre/$dificultad/$imagen/${tipoEspecie}")

        } else {
            Log.e("CaminoNivelesRoute", "No se encontró especie para el nivel $levelId")
        }
    }

    LaunchedEffect(Unit) {
        progresoViewModel.activarEscuchaTiempoReal()
    }

    if (!isSplashShown) {
        InicioJuegoView(
            onLoadingComplete = {
                viewModel.markSplashAsShown()
            }
        )
    } else {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = { BottomNavBar(navController, onItemClick = musicManager::playClickSound) }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                caminoNiveles(
                    navController = navController,
                    niveles = niveles,
                    onStartTransitionAndNavigate = onStartTransitionAndNavigate,
                    musicManager = musicManager,
                    onMusicToggle = onMusicToggle,
                    isMusicEnabled = isMusicGloballyEnabled,
                    onItemClick = musicManager::playClickSound,
                    progreso = progresoList,
                    usuarioViwModel = usuarioViwModel
                )
            }
        }
    }

    if (showCompletionDialog) {
        CompletionDialog(
            onDismiss = {
                showCompletionDialog = false
                viewModel.consumeGameCompletionEvent()
                musicManager.playClickSound()
            },
            onNavigateHome = {
                showCompletionDialog = false
                viewModel.consumeGameCompletionEvent()
                musicManager.playClickSound()
            },
            musicManager = musicManager
        )
    }
}

@Composable
fun CompletionDialog(
    onDismiss: () -> Unit,
    onNavigateHome: () -> Unit,
    musicManager: MusicManager
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.iconos),
                    contentDescription = "Felicidades",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡FELICIDADES!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF007ACC)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Has explorado y completado todos los niveles de Ocean Words. ¡Eres un verdadero experto marino!",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNavigateHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6CBBD7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Volver al Camino de Niveles", color = Color.White)
                }
            }
        }
    }
}