package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items   // ✅ ESTE ES EL CLAVE
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color // Usar la clase Color de Compose
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.PaqueteType
import com.proyect.ocean_words.model.PistaEstado

import com.proyect.ocean_words.view.theme.LightOlive
import com.proyect.ocean_words.view.theme.MomoTrustDisplay
import com.proyect.ocean_words.view.theme.azulCeleste
import com.proyect.ocean_words.utils.MusicManager
@Composable
fun ShopItemCard(
    item: PistaEstado,
    onBuyClicked: (PistaEstado) -> Unit,
    musicManager: MusicManager,
    modifier: Modifier = Modifier // ✅ OBLIGATORIO
) {
    Column(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .border(
                1.5.dp,
                Color.LightGray.copy(alpha = 0.5f),
                RoundedCornerShape(16.dp)
            )
            .background(
                color = Color.White.copy(alpha = 0.85f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onBuyClicked(item)
                musicManager.playClickSound()
            }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = item.iconResId),
            contentDescription = item.type.name,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "+${item.quantity}",
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xFF00796B)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                onBuyClicked(item)
                musicManager.playClickSound()
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(38.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightOlive
            )
        ) {
            Text(
                text = if (item.type == PaqueteType.MONEDAS) {
                    "$${item.price}"
                } else {
                    "${item.price}"
                },
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }
}

// --- Pantalla principal de tienda ---
@Composable
fun GameShopScreen(
    itemsMonedas: List<PistaEstado>,
    itemsVidas: List<PistaEstado>,
    onBuy: (PistaEstado) -> Unit,
    visible: Boolean,
    navController: NavController,
    musicManager: MusicManager
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
           ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_tienda),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier.fillMaxSize()
                .border(
                    width = 2.dp,
                    color = Color.Gray.copy(alpha = 0.5f), // 🔹 Borde con transparencia suave
                    shape = RoundedCornerShape(12.dp)
                )
                ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Barra superior de título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(azulCeleste)
                    .windowInsetsPadding(WindowInsets.statusBars) // ✅ Deja espacio arriba
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (visible){
                        Button(
                            onClick = {
                                navController.popBackStack()
                                musicManager.playClickSound() },
                            modifier = Modifier.size(40.dp), // Tamaño del botón
                            shape = RoundedCornerShape(50), // Circular
                            contentPadding = PaddingValues(0.dp), // Sin padding interno
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, // Fondo transparente
                                contentColor = Color.Unspecified // Mantiene el color de la imagen
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp) // Sin sombra
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.eliminar),
                                contentDescription = "Cerrar",
                                contentScale = ContentScale.Crop,

                                modifier = Modifier.size(40.dp)
                            )
                        }


                    }


                    Text(
                        text = "Tienda",
                        fontSize = 32.sp,
                        fontFamily = MomoTrustDisplay,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    GameIndicator(value = "500",
                        redireccionarClick={
                            navController.navigate("game_shop")
                            musicManager.playClickSound()},
                        false)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Título principal
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COMPRAR",
                    fontSize = 24.sp,
                    fontFamily = MomoTrustDisplay,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Cuadrícula de productos
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                items(itemsMonedas.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        rowItems.forEach { item ->
                            ShopItemCard(
                                item = item,
                                onBuyClicked = onBuy,
                                musicManager = musicManager,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .height(12.dp)

                            .background(azulCeleste)
                    )
                    Spacer(Modifier.height(12.dp))
                }

                items(itemsVidas.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        rowItems.forEach { item ->
                            ShopItemCard(
                                item = item,
                                onBuyClicked = onBuy,
                                musicManager = musicManager,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }



        }
    }
}
