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
import androidx.navigation.compose.rememberNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import com.proyect.ocean_words.ui.theme.Ocean_wordsTheme
import com.proyect.ocean_words.view.InicioJuegoView
import com.proyect.ocean_words.view.OceanWordsGameUI
import com.proyect.ocean_words.view.rutas.NavManager
import com.proyect.ocean_words.view.rutas.Rutas
import com.proyect.ocean_words.view.screens.caracteristicasEspecieView

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidThreeTen.init(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Ocean_wordsTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavManager()
                }
            }
        }
    }
}
