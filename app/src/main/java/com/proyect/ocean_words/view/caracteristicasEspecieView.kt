package com.proyect.ocean_words.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.EspecieEstado
import com.proyect.ocean_words.view.theme.*
import com.proyect.ocean_words.view.rutas.Rutas
import com.proyect.ocean_words.viewmodels.CaracteristicasEspecieViewModels

val StarColor = Color(0xFFFFCC00)

@Composable
fun caracteristicasEspecieView(
    navController: NavController,
    especie_id: String,
    imagen: String?,
    levelId: Int?
) {
    val viewModel: CaracteristicasEspecieViewModels = viewModel()
    val especieState by viewModel.especie.collectAsState()

    LaunchedEffect(especie_id) {
        Log.i("Firebase", "Cargando especie con ID: $especie_id")
        viewModel.getEspeciePorId(especie_id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanBackground)
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_juego),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        when (val especie = especieState) {
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = azulCeleste)
                }
            }

            else -> {
                CaracteristicasModal(
                    navController = navController,
                    especie = especie,
                    onClose = { navController.popBackStack() },
                    imagen = imagen,
                    levelId = levelId
                )
            }
        }
    }
}

@Composable
fun CaracteristicasModal(
    navController: NavController,
    especie: EspecieEstado,
    onClose: () -> Unit,
    imagen: String?,
    levelId: Int?
) {
    val levelId = levelId?.plus(1)
    Dialog(onDismissRequest = { onClose() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .border(8.dp, arena, shape = RoundedCornerShape(25.dp)),
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = whiteBoxColor.copy(alpha = 0.97f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))
                        )
                    )
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onClose() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.Black
                            )
                        }
                    }

                    Text(
                        text = "¡CORRECTO!",
                        fontFamily = MomoTrustDisplay,
                        color = azulCeleste,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = imagen,
                        contentDescription = "",
                        modifier = Modifier
                            .size(160.dp)
                            .padding(6.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = especie.nombre.uppercase(),
                        fontFamily = MomoTrustDisplay,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CaracteristicaTexto("Tipo", especie.tipo)
                    CaracteristicaTexto("Hábitat", especie.habitat)
                    CaracteristicaTexto("Agua", especie.agua)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "DESCRIPCIÓN",
                        fontFamily = Delius,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Text(
                        text = especie.descripcion,
                        fontFamily = Delius,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "DATOS CURIOSOS",
                        fontFamily = Delius,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    especie.Datos.split("*")
                        .filter { it.isNotBlank() }
                        .forEach { dato ->
                            CaracteristicaTexto("•", dato.trim())
                        }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            20.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        BotonInferiorIcon(
                            icon = Icons.Default.Home,
                            texto = "Inicio",
                            onClick = {
                                navController.navigate(Rutas.CAMINO_NIVELES) {
                                    popUpTo(Rutas.CAMINO_NIVELES) { inclusive = true }
                                }
                            }
                        )
                        BotonInferiorIcon(
                            icon = Icons.Default.ArrowForward,
                            texto = "Siguiente",
                            onClick = {
                                navController.navigate("loading_screen/$levelId")

                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CaracteristicaTexto(label: String, value: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE6F1F5), // Fondo azul claro suave
        shadowElevation = 6.dp,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = label,
                fontFamily = Delius,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1B3B4F) // Un azul oscuro suave para el texto del label
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                fontFamily = Delius,
                fontSize = 16.sp,
                color = Color(0xFF1B3B4F) // Mismo azul oscuro para el texto del valor
            )
        }
    }
}
@Composable
fun BotonInferiorIcon(
    icon: ImageVector,
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(130.dp)
            .height(55.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LightOlive,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = texto,
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = texto,
                fontFamily = BricolageGrotesque,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
