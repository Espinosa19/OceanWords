package com.proyect.ocean_words.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.UsuariosEstado
import com.proyect.ocean_words.view.rutas.Rutas
import com.proyect.ocean_words.view.theme.Blue // Asumo que son colores definidos en tu tema
import com.proyect.ocean_words.view.theme.LightOlive
import com.proyect.ocean_words.view.theme.azulCeleste
import com.proyect.ocean_words.viewmodels.EspecieViewModel
import com.proyect.ocean_words.viewmodels.NivelViewModel
import com.proyect.ocean_words.viewmodels.UserSession
import com.proyect.ocean_words.viewmodels.UsuariosViewModel

// NOTA: Tu código original usaba IndicatorBackgroundColor, Orange, OrangeDeep, y Purple40
// pero no estaban definidos en el código que enviaste. Asumo que se definen en el archivo theme.

@Composable
fun HeaderSection(
    navController: NavController,
    nivelViewModel: NivelViewModel,
    usuariosViewModel: UsuariosViewModel
) {

    val usuarioDatos: UsuariosEstado? = UserSession.currentUser
    val userId : String= (usuarioDatos?.id).toString()
    LaunchedEffect(Unit) {
        usuariosViewModel.checkAndRegenerateLife(userId)
    }

    LaunchedEffect(userId) {
        usuariosViewModel.observarMonedasVidasUsuario(userId)
    }
    val monedas by usuariosViewModel.monedasUsuario.collectAsState()
    val vidas by usuariosViewModel.vidas.collectAsState()
    val timeToNextLife by usuariosViewModel.timeToNextLife.collectAsState()
    val showNoLivesDialog = remember { mutableStateOf(false) }
    val allLivesLost = vidas.all { !it }

    LaunchedEffect(allLivesLost) {
        if (allLivesLost == true) {
            showNoLivesDialog.value = true
        }
    }
    CustomNoLivesDialog(
        showDialog = showNoLivesDialog.value,
        onDismiss = { showNoLivesDialog.value = false },
        onBuyClick = {
            showNoLivesDialog.value = false
            navController.navigate("game_shop")
        },
        onWaitClick = {
            showNoLivesDialog.value = false
        }
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top)
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ocean_words),
                contentDescription = "Título del juego",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight(),
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(10.dp))

            HeaderIndicatorRow(
                vidas = vidas,
                monedas = monedas,
                timeToNextLife = timeToNextLife,
                onBackClick = {
                    navController.popBackStack(
                        route = Rutas.CAMINO_NIVELES,
                        inclusive = false
                    )
                },
                redireccionarClick ={
                    navController.navigate("game_shop")
                })
        }


    }
}@Composable
fun HeaderIndicatorRow(
    vidas: List<Boolean>?,
    timeToNextLife: String,
    onBackClick: () -> Unit,
    redireccionarClick: () -> Unit,
    monedas: Int?
) {
    val isRechargeNeeded = vidas?.any { !it }
    val isTimerRunning = timeToNextLife.isNotEmpty()

    val bubbleHeight = 35.dp // Altura aproximada de la burbuja

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomBackButton(onClick = onBackClick)

            Box(
                contentAlignment = Alignment.Center
            ) {
                CentralIndicatorBox(vidas = vidas)

                if (isRechargeNeeded == true && isTimerRunning) {
                    LifeRechargeBubble(
                        timeRemaining = timeToNextLife,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = (-2).dp, y = (-bubbleHeight / 2) + 60.dp)
                    )
                }
            }

            GameIndicator(value = monedas.toString(),redireccionarClick,true)
        }
    }
}
@Composable
fun CustomNoLivesDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onBuyClick: () -> Unit,
    onWaitClick: () -> Unit
) {
    AnimatedVisibility(
        visible = showDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = androidx.compose.ui.window.DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(
                        width = 4.dp,
                        color = Color(0xFFFED128),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    // Título superior con fondo azul celeste
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                            .background(azulCeleste)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "¡OH NO!",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Contenido principal del diálogo
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nome_gusta),
                            contentDescription = "Sin Vidas",
                            modifier = Modifier.size(90.dp),
                            contentScale = ContentScale.Fit
                        )

                        Text(
                            text = "¡Te has quedado sin vidas!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Necesitas vidas para continuar jugando. Puedes esperar a la recarga automática o conseguir más en la tienda.",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onWaitClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = LightOlive),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Text(
                                text = "Esperar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Button(
                            onClick = onBuyClick,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Blue),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Text(
                                text = "Comprar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
// -------------------------------------------------------------------------------------------------

/**
 * Componente Botón de Retroceso (Separado para limpieza)
 */
@Composable
fun CustomBackButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(40.dp), // Ligeramente más grande y visible
        shape = RoundedCornerShape(percent = 50), // Forma perfectamente circular
        colors = ButtonDefaults.buttonColors(containerColor = Blue),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Volver atrás",
            tint = Color.White,
            modifier = Modifier.size(24.dp) // Ícono de tamaño estándar
        )
    }
}
/**
 * Componente Indicador Central (Corregido para mostrar corazones llenos/vacíos)
 */
@Composable
fun CentralIndicatorBox(vidas: List<Boolean>?) {
    // ... [Variables de ancho, alto y colores, etc. - Mantenemos esto igual] ...
    val indicatorWidth = 100.dp
    val indicatorHeight = 40.dp

    Box(
        modifier = Modifier
            .width(indicatorWidth)
            .height(indicatorHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFB3E5FC).copy(alpha = 0.65f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ITERACIÓN CORREGIDA: Iteramos sobre cada 'vida'
            vidas?.forEach { isLifeActive ->
                val imageResource = if (isLifeActive) {
                    R.drawable.vidas // Asumo que 'vidas' es el corazón lleno
                } else {
                    R.drawable.nome_gusta // <--- ¡DEBES USAR UN RECURSO PARA EL CORAZÓN VACÍO AQUÍ!
                }

                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = if (isLifeActive) "Corazón lleno" else "Corazón perdido",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}@Composable
fun LifeRechargeBubble(timeRemaining: String, modifier: Modifier = Modifier) {
    // Definición de colores (ejemplo)
    val TextColor = Color.White

    // El componente que quieres colocar:
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)) // Forma de burbuja redondeada
            .background(LightOlive)

            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        // Icono y Texto de la burbuja (por ejemplo)
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icono de reloj o similar
            Text(
                text = "Recarga en $timeRemaining",
                color = TextColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
// NO NECESITA CAMBIOS MAYORES
@Composable
fun GameIndicator(
    value: String,
    redireccionarClick: () -> Unit,
    visible: Boolean
) {
    // CAMBIO CLAVE 2: Usamos wrapContentWidth(unconstrained) para que el indicador crezca solo lo necesario.
    Box(
        modifier = Modifier
            .wrapContentWidth(Alignment.Start)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFB3E5FC).copy(alpha = 0.65f)) // Asumo el color IndicatorBackgroundColor
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(30.dp)
            )
            .height(40.dp) // Altura fija de la cápsula (se mantiene porque es pequeña)
            .padding(start = 2.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            // El padding del Row interno asegura que el texto no se superponga con el círculo
            modifier = Modifier.padding(start = 45.dp, end = 12.dp)
                    .width(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

            verticalAlignment = Alignment.CenterVertically
        ) {
            when (value) {
                null -> {
                    CircularProgressIndicator()
                }
                else -> {
                    Text(
                        text = "$value",

                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            if(visible) {

                Button(
                    onClick = redireccionarClick,
                    modifier = Modifier.size(40.dp), // Tamaño del botón
                    shape = RoundedCornerShape(50), // Circular
                    contentPadding = PaddingValues(0.dp), // Sin padding interno
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, // Fondo transparente
                        contentColor = Color.Unspecified // Mantiene el color de la imagen
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp) // Sin sombra
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = null,
                        tint = Blue,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
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
                    contentDescription = "Moneda",
                    modifier = Modifier.size(24.dp)
                )

            }

        // Icono dentro de un círculo

    }
}