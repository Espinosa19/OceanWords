package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items   // ✅ ESTE ES EL CLAVE
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color // Usar la clase Color de Compose
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.proyect.ocean_words.R
import com.proyect.ocean_words.model.PaqueteType
import com.proyect.ocean_words.model.PistaEstado
import com.proyect.ocean_words.model.UsuariosEstado

import com.proyect.ocean_words.view.theme.azulCeleste
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.viewmodels.UserSession
import com.proyect.ocean_words.viewmodels.UsuariosViewModel
@Composable
fun ShopItemCard(
    item: PistaEstado,
    onBuyClicked: (PistaEstado) -> Unit,
    musicManager: MusicManager,
    modifier: Modifier = Modifier,
    usuarioViwModel: UsuariosViewModel,
) {
    val accentColor = if (item.type == PaqueteType.MONEDAS) {
        Color(0xFFFFC107) // Dorado
    } else {
        Color(0xFFE53935) // Rojo vidas
    }
    val usuarioDatos: UsuariosEstado? = UserSession.currentUser
    val userId : String= (usuarioDatos?.id).toString()

    Column(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable {
                onBuyClicked(item)
                musicManager.playClickSound()
            }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(item.iconResId),
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "+${item.quantity}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                if(item.type == PaqueteType.MONEDAS){
                    onBuyClicked(item)
                }else{
                    usuarioViwModel.descontarMonedas(userId,item.quantity)
                }
                musicManager.playClickSound()
            },
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (item.type == PaqueteType.MONEDAS)
                    "Comprar $${item.price}"
                else
                    "Recargar ${item.price}",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GameShopScreen(
    itemsMonedas: List<PistaEstado>,
    itemsVidas: List<PistaEstado>,
    onBuy: (PistaEstado) -> Unit,
    visible: Boolean,
    navController: NavController,
    musicManager: MusicManager,
    usuarioViwModel: UsuariosViewModel
) {
    val usuarioDatos: UsuariosEstado? = UserSession.currentUser
    val userId = usuarioDatos?.id ?: ""

    LaunchedEffect(userId) {
        usuarioViwModel.observarMonedasVidasUsuario(userId)
        usuarioViwModel.initLifeTimerIfNeeded(userId)
    }

    val monedas by usuarioViwModel.monedasUsuario.collectAsState(initial = null)
    val topBarModifier = if (visible) {
        Modifier
            .fillMaxWidth()
            .background(azulCeleste)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(vertical = 12.dp)

    } else {
        Modifier
            .fillMaxWidth()
            .background(azulCeleste)
            .padding(vertical = 18.dp, horizontal = 12.dp)
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo_tienda),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            // 🔹 TOP BAR
            Box(
                modifier = topBarModifier

            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (visible) {
                        IconButton(onClick = {
                            navController.popBackStack()
                            musicManager.playClickSound()
                        }) {
                            Image(
                                painter = painterResource(R.drawable.eliminar),
                                contentDescription = "Cerrar",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Text(
                        text = "Tienda",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                    )

                    GameIndicator(
                        value = monedas,
                        redireccionarClick = {
                            navController.navigate("game_shop")
                            musicManager.playClickSound()
                        },
                        false
                    )
                }
            }

            ShopContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    // 🔹 MONEDAS
                    item {
                        ShopSectionHeader(
                            title = "Monedas",
                            description = "Úsalas para comprar pistas y ayudas",
                            iconRes = R.drawable.dolar
                        )
                    }

                    items(itemsMonedas.chunked(2)) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            row.forEach {
                                ShopItemCard(
                                    item = it,
                                    onBuyClicked = onBuy,
                                    musicManager = musicManager,
                                    modifier = Modifier.weight(1f),
                                    usuarioViwModel,
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(12.dp))
                    }

                    // Separador
                    item {
                        Divider(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = Color.LightGray.copy(alpha = 0.5f)
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    // ❤️ VIDAS
                    item {
                        ShopSectionHeader(
                            title = "Vidas",
                            description = "Recarga tu energía y sigue jugando",
                            iconRes = R.drawable.vidas
                        )
                    }

                    items(itemsVidas.chunked(2)) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            row.forEach {
                                ShopItemCard(
                                    item = it,
                                    onBuyClicked = onBuy,
                                    musicManager = musicManager,
                                    modifier = Modifier.weight(1f),
                                    usuarioViwModel = usuarioViwModel,
                                )
                            }
                            if (row.size == 1) Spacer(Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }

        }
        }
    }

}
@Composable
fun ShopContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(
                color = Color.White.copy(alpha = 0.88f),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.6f),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        content()
    }
}

@Composable
fun ShopSectionHeader(
    title: String,
    description: String,
    iconRes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


