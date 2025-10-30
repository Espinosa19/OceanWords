package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.ocean_words.view.theme.Blue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.proyect.ocean_words.R
import com.proyect.ocean_words.view.theme.azulRey

@Composable
fun configuracionView(
    onBack: () -> Unit
) {

    var musicaActivada by remember { mutableStateOf(true) }
    var sonidoActivado by remember { mutableStateOf(true) }
    var notificacionesActivadas by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Surface (
            modifier = Modifier
                .width(300.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .border(2.dp, Blue, RoundedCornerShape(30.dp)),
            shape = RoundedCornerShape(30.dp),
            color = Color(0xFFBFE0FA)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 30.dp, horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "OPCIONES",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = azulRey,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                ConfigRow(label = "MÚSICA", isChecked = musicaActivada) { musicaActivada = it }
                Divider(color = Color(0xFF003D69).copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 5.dp))

                ConfigRow(label = "SONIDO", isChecked = sonidoActivado) { sonidoActivado = it }
                Divider(color = Color(0xFF003D69).copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 5.dp))

                ConfigRow(label = "NOTIFICACIONES", isChecked = notificacionesActivadas) { notificacionesActivadas = it }
                Divider(color = Color(0xFF003D69).copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 5.dp))

                LanguageRow()

                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ConfigTextButton(text = "IDIOMA", onClick = { /* Acción de Idioma */ })

                    ConfigTextButton(text = "CRÉDITOS", onClick = { /* Acción de Créditos */ })
                }
            }
        }

        IconButton (
            onClick = onBack,
            modifier = Modifier
                .size(60.dp)
                .offset(y = 280.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.8f))
                .border(2.dp, azulRey, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Volver",
                tint = azulRey,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun ConfigRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = azulRey
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Blue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray
            )
        )
    }
}

@Composable
fun LanguageRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.lenguaje),
                contentDescription = "men",
                modifier = Modifier
                    .size(30.dp), // Tamaño fijo pero razonable para el icono/botón
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "IDIOMA",
                style = MaterialTheme.typography.labelMedium,
                color = azulRey
            )
        }

    }
}

@Composable
fun ConfigTextButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFBFE0FA)
        ),
        border = BorderStroke(2.dp, Color(0xFF003D69)),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,

            color = azulRey
        )
    }
}
