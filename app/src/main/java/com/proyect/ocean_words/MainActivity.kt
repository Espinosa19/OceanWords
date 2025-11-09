package com.proyect.ocean_words

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jakewharton.threetenabp.AndroidThreeTen
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.theme.Ocean_wordsTheme
import com.proyect.ocean_words.view.rutas.NavManager

class MainActivity : ComponentActivity() {

    private lateinit var musicManager: MusicManager
    var isAppInForeground by mutableStateOf(true)

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidThreeTen.init(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        musicManager = MusicManager(this)

        setContent {
            Ocean_wordsTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavManager(musicManager, isAppInForeground)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        musicManager.stopAllMusic()
        isAppInForeground = false
    }

    override fun onResume() {
        super.onResume()
        isAppInForeground = true
    }

    override fun onDestroy() {
        super.onDestroy()
        musicManager.destroy()
    }
}