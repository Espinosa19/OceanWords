package com.proyect.ocean_words.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // 👈 Importar la extensión
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.proyect.ocean_words.model.NivelEstado
import com.proyect.ocean_words.repositories.NivelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch // 👈 Importar la función launch
class NivelViewModel : ViewModel() {
    val nivelRepository = NivelRepository()
    private val _especies = MutableStateFlow<List<NivelEstado>>(emptyList())
    val especies: StateFlow<List<NivelEstado>> = _especies

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
//    init {
//        mostrarNiveles()
//    }
    private val _isSplashShown = MutableStateFlow(false)
    val isSplashShown: StateFlow<Boolean> = _isSplashShown.asStateFlow()

    fun markSplashAsShown() {
        _isSplashShown.value = true
    }
    fun mostrarNiveles() {
        viewModelScope.launch {
            _isLoading.value = true // Mostrar spinner/progreso
            nivelRepository.mostrarNiveles()
            _isLoading.value = false // Ocultar spinner/progreso
        }
    }

    // Si tienes que cargar los datos inmediatamente al crear el ViewModel,
    // puedes llamar a la función en un bloque init:

}