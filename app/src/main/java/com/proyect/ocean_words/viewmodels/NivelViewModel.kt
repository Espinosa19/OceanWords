package com.proyect.ocean_words.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // 👈 Importar la extensión

import com.proyect.ocean_words.model.NivelEstado
import com.proyect.ocean_words.repositories.NivelRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch // 👈 Importar la función launch
class NivelViewModel : ViewModel() {
    val nivelRepository = NivelRepository()
    private val _niveles = MutableStateFlow<List<NivelEstado>>(emptyList())
    // Exponer el StateFlow como un StateFlow inmutable para la View.
    val niveles: StateFlow<List<NivelEstado>> = _niveles.asStateFlow()

    private val _isLoading = MutableStateFlow(true) // 3. Inicialmente es TRUE si se carga en init.
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSplashShown = MutableStateFlow(false)
    val isSplashShown: StateFlow<Boolean> = _isSplashShown.asStateFlow()

    // Un ejemplo de manejo de errores
    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    init {
        mostrarNiveles()
    }

    fun markSplashAsShown() {
        _isSplashShown.value = true
    }
    fun mostrarNiveles() {
        viewModelScope.launch {
            nivelRepository.mostrarNiveles()
                .onStart {
                    _isLoading.value = true
                }
                .catch { exception ->
                    _error.emit("Error al cargar los niveles: ${exception.message}")
                    _niveles.value = emptyList()
                }
                .collect { listaOriginal ->
                    val listaConEspecieAleatoria = listaOriginal.map { nivel ->
                        val especieAleatoria = nivel.especies_id.orEmpty().randomOrNull()

                        nivel.copy(especies_id = especieAleatoria?.let { listOf(it) }
                            ?: emptyList())
                    }

                    _niveles.value = listaConEspecieAleatoria

                    _isLoading.value = false
                }
        }
    }
}

    // Si tienes que cargar los datos inmediatamente al crear el ViewModel,
    // puedes llamar a la función en un bloque init:

