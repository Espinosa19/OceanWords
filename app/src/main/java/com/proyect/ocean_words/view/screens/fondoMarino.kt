package com.proyect.ocean_words.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.proyect.ocean_words.R

@Composable
fun fondoSubmarino() {
    Image(
        painter = painterResource(id = R.drawable.fondoazul),
        contentDescription = "Fondo submarino",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}
