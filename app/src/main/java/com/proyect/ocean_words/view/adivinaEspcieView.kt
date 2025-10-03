package com.proyect.ocean_words.view

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.proyect.ocean_words.R // Asegúrate de cambiar esto a tu paquete
import com.proyect.ocean_words.ui.theme.Blue
import com.proyect.ocean_words.ui.theme.Orange
import com.proyect.ocean_words.ui.theme.OrangeDeep
import com.proyect.ocean_words.ui.theme.Purple40

// Colores aproximados de la imagen
val OceanBackground = Color(0xFF0077B6)
val LightBlue = Color(0xFFE1F5FE)
val IndicatorBackgroundColor = Color(0xFFB3E5FC).copy(alpha = 0.65f)

@Composable
fun OceanWordsGameUI(
    score: Int = 1250,
    time: String = "0:45",
    animal: String ="pez lampara",
    dificultad:String="dificil",
    animalQuestion: String = "¿QUÉ ANIMAL ES ESTE?",
    currentLetters: String = "AD CO FR CLWN FSH ARNDIBPK x cv b n n",
    // Esta es solo una representación simplificada de las letras
) {
    // Para el fondo y las decoraciones se usaría un Box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OceanBackground)
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.fondo_juego), // tu imagen en res/drawable
                contentDescription = null,
                contentScale = ContentScale.Crop, // Ajusta para cubrir toda la pantalla
                modifier = Modifier.matchParentSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                // 1. Encabezado (Logo, Score, Time)
                HeaderSection(score, time)

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Sección de Pregunta e Imagen
                QuestionAndImageSection(animalQuestion,animal)

                Column(modifier = Modifier.fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp,5.dp)){
                    tecladoInteractivo(animal,dificultad)

                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .align(Alignment.BottomCenter)
                    .background(OrangeDeep) // Fondo semitransparente para la barra
                    .padding(bottom = 38.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                accionesEspecíficas()
            }
        }
    }
}

@Composable
fun HeaderIndicatorRow(score: Int, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 27.dp)
            .fillMaxWidth()
            .padding(horizontal = 5.dp), // Sin padding top, ya que se manejaría en el Box principal
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameIndicator(
            icon = Icons.Default.Star,
            tipo="Icono",
            label = "SCORE",
            value = score.toString(),
            iconColor = Color(0xFFFCCB06), // Amarillo para el trofeo
            iconBackgroundColor = Color.White
        )

        GameIndicator(
            icon = Icons.Default.AccountBox,
            tipo= "Imagen",
            label = "TIME",
            value = time,
            iconColor = Color.White, // Blanco para el reloj
            iconBackgroundColor = Color(0xFF0077B6) // Azul oscuro para el fondo del reloj
        )
    }
}
@Composable
fun HeaderSection(score: Int, time: String) {
    // El Box permite apilar la imagen de fondo, el logo, los íconos de score/time y el botón de menú.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp) // Altura aproximada del área del encabezado
    ) {

        Image(
            painter = painterResource(id = R.drawable.ocean_words), // Reemplaza con tu ID correcto
            contentDescription = "Fondo de océano y título del juego",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .height(150.dp) // Ajusta esta altura para que solo se vea el logo
                .align(Alignment.TopCenter), // Lo centra arriba
            contentScale = ContentScale.FillWidth // Ajusta el ancho y mantiene la proporción
        )
        Spacer(modifier = Modifier.height(80.dp).align(Alignment.TopCenter)) // Espacio entre logo y indicadores

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 120.dp) // Ajusta este padding para colocarlo justo debajo del logo
        ) {
            HeaderIndicatorRow(score, time)
        }

        IconButton(
            onClick = { /* Lógica de click para el menú */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp) // Ajusta la posición
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
@Composable
fun QuestionAndImageSection(question: String,animal: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(20.dp,10.dp,20.dp,10.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)) // Aplica el redondeo de las esquinas
                .background(LightBlue) // Aplica el color de fondo
                .padding(vertical = 12.dp, horizontal = 15.dp), // Padding interno para centrar el texto
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question, // "¿QUÉ ANIMAL ES ESTE?"
                fontSize = 24.sp, // Aumentamos el tamaño para que destaque
                fontWeight = FontWeight.Black, // Usamos Black o ExtraBold para el impacto
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),
            contentAlignment = Alignment.Center


        ){
            Image(
                painter = painterResource(id = R.drawable.ballena),
                contentDescription = "Pez Payaso",
                modifier = Modifier.size(200.dp)
                    .offset(y = (-25).dp),

                )

            ResponseArea(animal)

        }


    }
}
@Composable
fun GameIndicator(
    icon: ImageVector,
    tipo: String,
    label: String,
    value: String,
    iconColor: Color,
    iconBackgroundColor: Color
) {
    // Usamos Box para superponer y alinear el icono circular dentro de la "cápsula"
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp)) // Borde redondeado para toda la "cápsula"
            .background(IndicatorBackgroundColor)
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.5f), // Borde blanco suave
                shape = RoundedCornerShape(30.dp)
            )
            .height(40.dp) // Altura fija para la cápsula
            .padding(start = 2.dp), // Pequeño padding a la izquierda para el efecto de sombra/espacio
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(start = 50.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // El texto va en blanco dentro de la cápsula
            Text(
                text = "$label: $value",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Icono dentro de un círculo sólido (se alinea automáticamente al Box)
        Box(
            modifier = Modifier
                .size(38.dp) // Tamaño del círculo
                .clip(CircleShape)
                .background(Blue)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            when (tipo) {
                "Imagen" -> Image(
                    painter = painterResource(id = R.drawable.reloj_de_arena),
                    contentDescription = "Fondo de océano",
                    modifier = Modifier.size(24.dp)
                        .padding(start = 5.dp)
                )
                "Icono" -> Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    }
}
@Composable
fun ResponseArea(animal: String) {
    // Usamos 'animal.trim().replace(" ", "")' para contar solo las letras visibles,
    // pero para la estructura de las cajas es mejor usar la longitud total.
    val tamano = animal.length
    val letrasPorFila = 8

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 115.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Creamos filas dinámicamente según el tamaño
        for (i in 0 until tamano step letrasPorFila) {

            Row(
                modifier = if (tamano > 8 && i == 0) {
                    Modifier.padding(top = 24.dp)
                }else if (tamano > 16 && i == 0) {
                    Modifier.padding(top = 30.dp)
                } else {
                    Modifier
                },
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fin = (i + letrasPorFila).coerceAtMost(tamano)
                for (j in i until fin) {
                    val caracter = animal[j]

                    if (caracter == ' ') {
                        Box(
                            modifier = Modifier
                                .width(15.dp)
                                .height(30.dp)
                        ) {
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    LightBlue.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = " ")
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun accionesEspecíficas() {
    Row(

        modifier = Modifier.fillMaxWidth().height(100.dp),

        horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterHorizontally),

        verticalAlignment = Alignment.CenterVertically
    ) {

        BotonDeInterfaz(
            icon = Icons.Filled.Close,
            onClick = { }
        )

        BotonDeInterfaz(
            icon = Icons.Filled.Refresh,
            onClick = { /* Acción para deshacer o recargar */ }
        )

        Button(
            onClick = {},
            modifier = Modifier
                .size(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange),
            contentPadding = PaddingValues(0.dp)
        ) {
            // ✅ USAMOS ICON: Muestra el icono en lugar del Text
            Image(
                painter = painterResource(id = R.drawable.pista), // Reemplaza con tu ID correcto
                contentDescription = "Fondo de océano y título del juego",
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}
@Composable
fun tecladoInteractivo(animal: String,dificultad: String) {
    val animalSinEspacios = animal.trim().replace(" ", "")
    var animalRandom="";

    val letrasPorFila = 10
    if(dificultad != "dificil"){
        animalRandom = shuffleText(animalSinEspacios)
    }else{
        val letrasRandom = getTwoRandomLetters()
        animalRandom = shuffleText(animalSinEspacios + letrasRandom.joinToString(""))
    }
    val tamano = animalRandom.length

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Creamos filas dinámicamente según el tamaño
        for (i in 0 until tamano step letrasPorFila) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val fin = (i + letrasPorFila).coerceAtMost(tamano)
                for (j in i until fin) {

                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .background(
                                LightBlue.copy(alpha = 0.8f),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = animalRandom[j].toString(),
                            color = Color.Black,                     // Color de la letra
                            fontSize = 20.sp,                        // Tamaño de fuente
                            fontWeight = FontWeight.Bold,            // Negrita
                            textAlign = TextAlign.Center             // Centrado
                        )                    }

                }
            }
        }
    }

}@Composable
fun BotonDeInterfaz(
    // 💡 CAMBIO: Usamos ImageVector para el icono en lugar de String para el texto
    icon: ImageVector,
    colorFondo: Color = Orange,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorFondo),
                contentPadding = PaddingValues(0.dp)
    ) {
        // ✅ USAMOS ICON: Muestra el icono en lugar del Text
        Icon(
            imageVector = icon,
            contentDescription = null, // Se puede dejar nulo si el contexto es obvio (como en HUDs)
            tint = Color.White, // Color del símbolo dentro del botón
            modifier = Modifier.size(32.dp)
        )
    }
}
fun getTwoRandomLetters(): List<Char> {
    val abecedario =('a'..'z')
    return List(2) { abecedario.random() }
}

fun shuffleText(animal: String): String {
    return animal.toList().shuffled().joinToString("")
}
@Preview(showBackground = true)
@Composable
fun adivinaEspecieView() {
    OceanWordsGameUI()
}