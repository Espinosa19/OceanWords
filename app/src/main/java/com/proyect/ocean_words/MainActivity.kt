package com.proyect.ocean_words

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.jakewharton.threetenabp.AndroidThreeTen
import com.proyect.ocean_words.utils.MusicManager
import com.proyect.ocean_words.view.theme.Ocean_wordsTheme
import com.proyect.ocean_words.view.rutas.NavManager
import com.proyect.ocean_words.viewmodels.EspecieViewModel
import com.proyect.ocean_words.viewmodels.NivelViewModel
import com.proyect.ocean_words.viewmodels.ProgresoViewModel
import com.proyect.ocean_words.viewmodels.UsuariosViewModel

class MainActivity : ComponentActivity() {

    private lateinit var musicManager: MusicManager
    var isAppInForeground by mutableStateOf(true)
    private val nivelViewModel: NivelViewModel by viewModels()
    private val especieViewModel: EspecieViewModel by viewModels()
    private val usuarioViwModel: UsuariosViewModel by viewModels()
    private val progresoViewModel: ProgresoViewModel by viewModels()
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidThreeTen.init(this)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        musicManager = MusicManager(this)

        setContent {
            Ocean_wordsTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavManager(musicManager, isAppInForeground,nivelViewModel,progresoViewModel,googleSignInClient)
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