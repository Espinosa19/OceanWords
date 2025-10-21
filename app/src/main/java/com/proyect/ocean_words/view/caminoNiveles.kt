package com.proyect.ocean_words.view

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.ocean_words.R
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.screens.BottomNavBar
import com.proyect.ocean_words.view.screens.GameIndicator
import com.proyect.ocean_words.view.screens.configuracionView
import com.proyect.ocean_words.viewmodels.NivelViewModel


val LevelSpacing = 40.dp
val PaddingVertical = 50.dp
val TotalLevels = 5

data class Level(val id: Int, val isUnlocked: Boolean)

val niveles = List(TotalLevels) { index ->
    Level(id = index + 1, isUnlocked = index < 2)
}


@Composable
fun caminoNiveles(
    onStartTransitionAndNavigate: (levelId: Int) -> Unit,
    navController: NavHostController,
    musicManager: MusicManager,
    onMusicToggle: (Boolean) -> Unit,
    isMusicEnabled: Boolean
) {


    val listState = rememberLazyListState()
    val density = LocalDensity.current
    var showConfigDialog by remember { mutableStateOf(false) }

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            //1. Fondo
            Image(
                painter = painterResource(id = R.drawable.fondo_o),
                contentDescription = "Fondo submarino",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 2. Nodos de Nivel
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
                itemsIndexed(niveles) { index, level ->
                    LevelNode(
                        level = level,
                        index = index,
                        onLevelClick = { levelId ->
                            onStartTransitionAndNavigate(levelId) // 👈 Aquí sí puedes hacerlo
                        },
                        animatedScale = levelNodeScale
                    )
                }
            }

            // 3. Peces Animados
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
                        scaleX = -1f // Voltea la imagen para que mire a la izquierda
                    },
                contentScale = ContentScale.Fit

            )

            Image(
                painter = painterResource(id = R.drawable.pez_payaso),
                contentDescription = "Pez 4",
                modifier = Modifier
                    .size(width = 75.dp, height = 37.5.dp)
                    .offset(
                        x = with(density) { fish3XOffset.toDp() },
                        y = 80.dp + with(density) { fish3YOffset.toDp() }
                    ),
                contentScale = ContentScale.Fit
            )


            Row(
                modifier = Modifier
                    .fillMaxWidth() // Ocupa todo el ancho de la pantalla/contenedor
                    .height(60.dp) // Mantiene la altura que definiste
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .offset(y = 24.dp), // Padding para no pegar a los bordes

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showConfigDialog = true },

                    ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Ajustes",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                    GameIndicator(
                        value = "1500",
                        redireccionarClick = { navController.navigate("game_shop") },
                        true,

                        )


                }



            if (showConfigDialog) {
                Dialog(
                    onDismissRequest = { showConfigDialog = false },
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
}



@Composable
fun LevelNode(
    level: Level,
    index: Int,
    onLevelClick: (levelId: Int) -> Unit, // 👈 Así es correcto
    animatedScale: Float
) {
    val horizontalOffset = if (index % 2 == 0) (-50).dp else 50.dp

    val unlockedLevelImages = remember { listOf(
        R.drawable.iconos, R.drawable.icono2, R.drawable.icono1
    )}
    val lockedLevelImages = remember { listOf(
        R.drawable.lock3, R.drawable.lock1, R.drawable.lock2
    )}

    val imageResId = if (level.isUnlocked) {
        unlockedLevelImages[index % unlockedLevelImages.size]
    } else {
        lockedLevelImages[index % lockedLevelImages.size]
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val pressAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.5f else 0f,
        animationSpec = tween(durationMillis = 100), label = "press_alpha_animation"
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
                    onClick = {
                        if (level.isUnlocked) {
                            onLevelClick(level.id)
                        }
                    },
                    indication = null,
                    interactionSource = interactionSource
                ),
            contentScale = ContentScale.Crop
        )

        Text(
            text = if (level.isUnlocked) "" else "🔒",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 5.dp)
        )

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
    viewModel: NivelViewModel
) {
    val isSplashShown by viewModel.isSplashShown.collectAsState()
    val niveles by viewModel.niveles.collectAsState(initial = emptyList())
    Log.i("Niveles","$niveles")
    var targetLevelId by remember { mutableStateOf<Int?>(null) }
    var isMusicGloballyEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(isMusicGloballyEnabled, isAppInForeground) {
        if (isMusicGloballyEnabled && isAppInForeground) {
            musicManager.playMenuMusic()
        } else {
            musicManager.stopAllMusic()
        }
    }

    if (!isSplashShown) {
        InicioJuegoView(
            onLoadingComplete = {
                viewModel.markSplashAsShown()
            }
        )
    }else {
    Scaffold (
        containerColor = Color.Transparent,
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            caminoNiveles(
                navController = navController,
//        niveles = niveles,
                onStartTransitionAndNavigate = { levelId ->
                    navController.navigate("nivel/$levelId")
                },
                musicManager = musicManager,
                onMusicToggle = { isEnabled ->
                    // Asumiendo que 'isMusicGloballyEnabled' está definido como un MutableState
                    // en el ámbito superior.
                    isMusicGloballyEnabled = isEnabled
                    // La lógica de play/stop se manejará automáticamente
                    // en el LaunchedEffect de arriba (punto 2)
                },
                isMusicEnabled = isMusicGloballyEnabled
            )
        }
    }
    }
}
