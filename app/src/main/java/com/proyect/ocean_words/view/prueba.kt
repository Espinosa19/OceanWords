package com.proyect.ocean_words.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun AcuarioView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF80DEEA)),
        contentAlignment = Alignment.Center
    ) {
        Text("PROXIMAMENTE... :)", fontSize = 24.sp, color = Color.Black)
    }
}