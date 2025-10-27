package com.proyect.ocean_words.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.ocean_words.R
import com.proyect.ocean_words.view.theme.Blue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButton
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.proyect.ocean_words.view.screens.configuracionView


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
) {
    val listState = rememberLazyListState()
    val density = LocalDensity.current
    var showConfigDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "general_animations")

    val fish1XOffset by infiniteTransition.animateFloat(
        initialValue = -500f, targetValue = 1500f,
        animationSpec = infiniteRepeatable(animation = tween(20000, easing = LinearEasing), repeatMode = RepeatMode.Restart), label = "fish1_offset_x"
    )
    val fish1YOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 25f,
        animationSpec = infiniteRepeatable(animation = tween(4000, easing = EaseInOut), repeatMode = RepeatMode.Reverse), label = "fish1_offset_y"
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
                        onLevelClick = { id ->
                            onStartTransitionAndNavigate(id)
                        },
                        animatedScale = levelNodeScale
                    )
                }
            }

            // 3. Peces Animados
            Image(

                painter = painterResource(id = R.drawable.grupopeces),
                contentDescription = "Pez 1",
                modifier = Modifier
                    .size(width = 120.dp, height = 60.dp)
                    .offset(
                        x = with(density) { fish1XOffset.toDp() },
                        y = 500.dp + with(density) { fish1YOffset.toDp() }
                    ),
                contentScale = ContentScale.Fit
            )

            Image(
                painter = painterResource(id = R.drawable.grupopeces),
                contentDescription = "Pez 2",
                modifier = Modifier
                    .size(width = 200.dp, height = 100.dp)
                    .offset(
                        x = with(density) { fish2XOffset.toDp() },
                        y = 300.dp + with(density) { fish2YOffset.toDp() }
                    )
                    .graphicsLayer {
                        scaleX = -1f // Voltea la imagen para que mire a la izquierda
                    },
                contentScale = ContentScale.Fit

            )

            Image(
                painter = painterResource(id = R.drawable.grupopeces),
                contentDescription = "Pez 3",
                modifier = Modifier
                    .size(width = 150.dp, height = 75.dp)
                    .offset(
                        x = with(density) { fish3XOffset.toDp() },
                        y = 100.dp + with(density) { fish3YOffset.toDp() }
                    ),
                contentScale = ContentScale.Fit
            )


            // 5. Monedas
            GameIndicator(
                value = "1500",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(
                        x = (-16).dp,
                        y = 45.dp
                    )
            )

            IconButton(
                onClick = { showConfigDialog = true },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(
                        x = 10.dp,
                        y = 40.dp
                    )

            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Ajustes",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
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
                            onBack = { showConfigDialog = false }
                        )
                    }
                }
            }

        }

    }
}


@Composable
fun GameIndicator(
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .wrapContentWidth(Alignment.Start)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFB3E5FC).copy(alpha = 0.65f))
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(30.dp)
            )
            .height(40.dp)
            .padding(start = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(start = 45.dp, end = 12.dp)
                .width(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$value",

                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(imageVector = Icons.Filled.AddCircle, contentDescription = null, tint = Blue, modifier = Modifier.size(32.dp))

        }

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Blue)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dolar),
                contentDescription = "Monedas",
                modifier = Modifier.size(24.dp)
            )
        }
    }

}

@Composable
fun LevelNode(
    level: Level,
    index: Int,
    onLevelClick: (levelId: Int) -> Unit,
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
            text = if (level.isUnlocked) "⭐⭐⭐" else "🔒",
            fontSize = 20.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 5.dp)
        )

        Text(
            text = "${level.id}",
            color = Color.Black,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
