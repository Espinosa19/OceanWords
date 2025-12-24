package com.proyect.ocean_words.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope // 👈 Importar la extensión

import com.proyect.ocean_words.model.NivelEstado
import com.proyect.ocean_words.domain.repositories.NivelRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class NivelViewModel : ViewModel() {
    val nivelRepository = NivelRepository()
    private val _niveles = MutableStateFlow<List<NivelEstado>>(emptyList())
    val niveles: StateFlow<List<NivelEstado>> = _niveles.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _showGameCompletionEvent = MutableStateFlow(false)
    val showGameCompletionEvent: StateFlow<Boolean> = _showGameCompletionEvent.asStateFlow()

    private val _isSplashShown = MutableStateFlow(false)
    val isSplashShown: StateFlow<Boolean> = _isSplashShown.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    init {
        mostrarNiveles()
    }

    // Temporizador de recarga



    fun markSplashAsShown() {
        _isSplashShown.value = true
    }fun mostrarNiveles() {
        viewModelScope.launch {
            nivelRepository.mostrarNiveles()
                .onStart {
                    _isLoading.value = true
                }
                .catch { exception ->
                    _error.emit("Error al cargar los niveles: ${exception.message}")
                    _niveles.value = emptyList()
                    _isLoading.value = false
                }
                .collect { listaOriginal ->

                    // Para cada nivel, seleccionamos una especie aleatoria de su lista
                    val listaConEspecieAleatoria = listaOriginal.map { nivel ->
                        val especieAleatoria = nivel.especies_id.randomOrNull()

                        nivel.copy(
                            especies_id = especieAleatoria?.let { listOf(it) } ?: emptyList()
                        )
                    }

                    // Actualizamos el estado con la nueva lista procesada
                    _niveles.value = listaConEspecieAleatoria
                    _isLoading.value = false
                }
        }
    }
    fun triggerGameCompletionDialog() {
        _showGameCompletionEvent.value = true
    }

    /**
     * Llama a esta función desde CaminoNivelesRoute cuando el diálogo
     * de felicitación ha sido mostrado y cerrado, para 'consumir' el evento.
     */
    fun consumeGameCompletionEvent() {
        _showGameCompletionEvent.value = false
    }

}

    // Si tienes que cargar los datos inmediatamente al crear el ViewModel,
    // puedes llamar a la función en un bloque init:

