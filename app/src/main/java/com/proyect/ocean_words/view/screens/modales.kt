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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.PistaEstado
import com.proyect.ocean_words.model.sampleShopItems
import com.proyect.ocean_words.ui.theme.Blue
import com.proyect.ocean_words.ui.theme.LightOlive
import com.proyect.ocean_words.ui.theme.azulCeleste
@Composable
fun ShopItemCard(item: PistaEstado, onBuyClicked: (PistaEstado) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp)) // 🔹 Sombra suave para resaltar
            .border(1.5.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .background(
                color = Color.White.copy(alpha = 0.85f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onBuyClicked(item) }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagen del ícono
        Image(
            painter = painterResource(id = item.iconResId),
            contentDescription = item.type.name,
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Cantidad de monedas
        Text(
            text = "+${item.quantity}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00796B)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Botón de compra
        Button(
            onClick = { onBuyClicked(item) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(38.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightOlive
            )
        ) {
            Text(
                text = "$${item.price}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// --- Pantalla principal de tienda ---
@Composable
fun GameShopScreen(items: List<PistaEstado>, onBuy: (PistaEstado) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
           ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_juego),
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
                .background(
                    color = Color.Black.copy(alpha = 0.4f), // 🔹 Fondo negro semitransparente (ajusta el alpha)
                    shape = RoundedCornerShape(12.dp)
                )    ,
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
                    Image(
                        painter = painterResource(id = R.drawable.cancelar),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = "Tienda",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    GameIndicator(value = "500")
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
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Cuadrícula de productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items) { item ->
                    ShopItemCard(item = item, onBuyClicked = onBuy)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameShopScreen() {
    GameShopScreen(items = sampleShopItems, onBuy = {})
}