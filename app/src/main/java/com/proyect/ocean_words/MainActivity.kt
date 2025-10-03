package com.proyect.ocean_words

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.proyect.ocean_words.ui.theme.Ocean_wordsTheme
import com.proyect.ocean_words.view.OceanWordsGameUI
import com.proyect.ocean_words.view.adivinaEspecieView
import com.proyect.ocean_words.view.inicioJuegoView

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ocean_wordsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    adivinaEspecieView();
                }
            }
        }
    }
}
